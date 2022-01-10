# Made by disKret (adapted for L2JLisvus by roko91)
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "13_ParcelDelivery"

#NPC
FUNDIN = 8274
VULCAN = 8539

#QUEST ITEM
PACKAGE = 7263

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [PACKAGE]

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   if event == "8274-2.htm" :
     if cond == 0 :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.giveItems(PACKAGE,1)
       st.playSound("ItemSound.quest_accept")
   if event == "8539-1.htm" :
     if cond == 1 and st.getQuestItemsCount(PACKAGE) == 1 :
       st.takeItems(PACKAGE,1)
       st.giveItems(57,82656)
       st.setState(State.COMPLETED)
       st.set("cond","0")
       st.playSound("ItemSound.quest_finish")
     else :
       htmltext = "You don't have required items"
   return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   level = st.getPlayer().getLevel()
   cond = st.getInt("cond")
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == FUNDIN and cond == 0 :
     if id == State.COMPLETED :
       htmltext = JQuest.getAlreadyCompletedMsg()
     elif level < 74 : 
       htmltext = "8274-1.htm"
       st.exitQuest(1)
     elif level >= 74 : 
       htmltext = "8274-0.htm"
   elif npcId == FUNDIN and cond == 1 :
     htmltext = "8274-2.htm"
   elif npcId == VULCAN and cond == 1 and id == State.STARTED:
     htmltext = "8539-0.htm"
   return htmltext

QUEST = Quest(13,qn,"Parcel Delivery")
QUEST.addStartNpc(8274)

QUEST.addTalkId(8274)
QUEST.addTalkId(8539)