# Made by mtrix
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ADENA = 57
LIENRIK_EGG1 = 5860
LIENRIK_EGG2 = 5861
CHANCE = 30
CHANCE2 = 7

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [LIENRIK_EGG1, LIENRIK_EGG2]

 def onEvent (self,event,st) :
     htmltext = event
     if event == "8067-04.htm" :
         st.set("cond","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_accept")
     elif event == "8067-09.htm" :
         st.playSound("ItemSound.quest_finish")
         st.exitQuest(1)
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     level = st.getPlayer().getLevel()
     cond = st.getInt("cond")
     eggs1 = st.getQuestItemsCount(LIENRIK_EGG1)
     eggs2 = st.getQuestItemsCount(LIENRIK_EGG2)
     if id == State.CREATED :
        if level>=39 :
            htmltext = "8067-01.htm"
        else :
            htmltext = "<html><body>(This is a quest that can only be performed by players of level 39 and above.)</body></html>"
            st.exitQuest(1)
     elif cond==1 :
        if not eggs1 and not eggs2 :
          htmltext = "8067-05.htm"
        elif eggs1 and not eggs2 :
          htmltext = "8067-06.htm"
          st.giveItems(ADENA,eggs1*209)
          st.takeItems(LIENRIK_EGG1,-1)
          st.playSound("ItemSound.quest_itemget")
        elif not eggs1 and eggs2 :
          htmltext = "8067-08.htm"
          st.giveItems(ADENA,eggs2*2050)
          st.takeItems(LIENRIK_EGG2,-1)
          st.playSound("ItemSound.quest_itemget")
        elif eggs1 and eggs2 :
          htmltext = "8067-08.htm"
          st.giveItems(ADENA,eggs1*209+eggs2*2050)
          st.takeItems(LIENRIK_EGG1,-1)
          st.takeItems(LIENRIK_EGG2,-1)
          st.playSound("ItemSound.quest_itemget")
     return htmltext

 def onKill (self,npc,player,isPet):
     st = player.getQuestState("352_HelpRoodRaiseANewPet")
     if st :
         if st.getState() != State.STARTED : return
         npcId = npc.getNpcId()
         random = st.getRandom(100)
         if random<=CHANCE :
             st.giveItems(LIENRIK_EGG1,1)
         if random<=CHANCE2 :
             st.giveItems(LIENRIK_EGG2,1)
     return

QUEST = Quest(352,"352_HelpRoodRaiseANewPet","Help Rood Raise A New Pet")
QUEST.addStartNpc(8067)

QUEST.addTalkId(8067)

QUEST.addKillId(786)
QUEST.addKillId(787)