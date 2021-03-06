# Created by CubicVirtuoso
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPCs
ROXXY      = 7006
BAULRO     = 7033
SIR_COLLIN = 7311

#ITEM
BAULRO_LETTER = 7571

#REWARDS
SCROLL_OF_ESCAPE_GIRAN = 7559
MARK_OF_TRAVELER       = 7570

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BAULRO_LETTER]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "7006-03.htm" :
     st.set("cond","1")
     st.set("id","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "7033-02.htm" :
     st.giveItems(BAULRO_LETTER,1)
     st.set("cond","2")
     st.set("id","2")
     st.playSound("ItemSound.quest_middle")
   elif event == "7311-03.htm" :
     st.takeItems(BAULRO_LETTER,-1)
     st.set("cond","3")
     st.set("id","3")
     st.playSound("ItemSound.quest_middle")
   elif event == "7006-06.htm" :
     st.giveItems(SCROLL_OF_ESCAPE_GIRAN,1)
     st.giveItems(MARK_OF_TRAVELER, 1)
     st.unset("cond")
     st.setState(State.COMPLETED)
     st.playSound("ItemSound.quest_finish")
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   cond  = st.getInt("cond")
   id    = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
     st.set("id","0")
     if st.getPlayer().getRace().ordinal() == 0 :
       if st.getPlayer().getLevel() >= 3 :
         htmltext = "7006-02.htm"
       else :
         htmltext = "<html><body>Quest for characters level 3 and above.</body></html>"
         st.exitQuest(1)
     else :
       htmltext = "7006-01.htm"
       st.exitQuest(1)
   elif npcId == ROXXY and id == State.COMPLETED :
     htmltext = "<html><body>I can't supply you with another Giran Scroll of Escape. Sorry traveller.</body></html>"
   elif npcId == ROXXY and cond == 1 :
     htmltext = "7006-04.htm"
   elif npcId == BAULRO :
     if cond == 1:
       htmltext = "7033-01.htm"
     elif cond == 2 and st.getQuestItemsCount(BAULRO_LETTER):
       htmltext = "7033-03.htm"
   elif npcId == SIR_COLLIN and cond == 2 and st.getQuestItemsCount(BAULRO_LETTER) :
       htmltext = "7311-02.htm"
   elif npcId == ROXXY and cond == 3 :
     htmltext = "7006-05.htm"
   return htmltext

QUEST = Quest(6,"6_StepIntoTheFuture","Step into the Future")
QUEST.addStartNpc(ROXXY)

QUEST.addTalkId(ROXXY)
QUEST.addTalkId(BAULRO)
QUEST.addTalkId(SIR_COLLIN)