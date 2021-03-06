# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPC
TALLOTH = 7141

#ITEMS
ONYX_BEAST_EYE = 1081
TAINT_STONE    = 1082
SUCCUBUS_BLOOD = 1083

#MOBS
OMEN_BEAST            = 31
TAINTED_ZOMBIE        = 41
STINK_ZOMBIE          = 46
LESSER_SUCCUBUS       = 48
LESSER_SUCCUBUS_TUREN = 52
LESSER_SUCCUBUS_TILFO = 57

#REWARDS
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ONYX_BEAST_EYE, TAINT_STONE, SUCCUBUS_BLOOD]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "7141-03.htm" :
     st.set("cond","1")
     st.set("id","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   cond = st.getInt("cond")
   if id == State.COMPLETED :
     htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == TALLOTH and cond == 0 :
     if cond<15 :
       if st.getPlayer().getRace().ordinal() != 2 :
         htmltext = "7141-00.htm"
         st.exitQuest(1)
       elif st.getPlayer().getLevel() >= 16 :
         htmltext = "7141-02.htm"
       else:
         htmltext = "7141-01.htm"
         st.exitQuest(1)
     else:
       htmltext = "7141-01.htm"
       st.exitQuest(1)
   elif npcId == TALLOTH and cond == 1 :
     htmltext = "7141-04.htm"
   elif npcId == TALLOTH and cond == 2 :
     htmltext = "7141-06.htm"
     st.takeItems(ONYX_BEAST_EYE,-1)
     st.takeItems(TAINT_STONE,-1)
     st.takeItems(SUCCUBUS_BLOOD,-1)
     st.giveItems(ADENA,4900)
     st.addExpAndSp(5000,0)
     st.set("cond","0")
     st.setState(State.COMPLETED)
     st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("3_WillTheSealBeBroken")
   if st:
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      cond = st.getInt("cond")
      if cond == 1 :
        if npcId == OMEN_BEAST and st.getQuestItemsCount(ONYX_BEAST_EYE) == 0 :
          st.giveItems(ONYX_BEAST_EYE,1)
          st.playSound("ItemSound.quest_itemget")
        elif npcId == TAINTED_ZOMBIE and st.getQuestItemsCount(TAINT_STONE) == 0 :
          st.giveItems(TAINT_STONE,1)
          st.playSound("ItemSound.quest_itemget")
        elif npcId == STINK_ZOMBIE and st.getQuestItemsCount(TAINT_STONE) == 0 :
          st.giveItems(TAINT_STONE,1)
          st.playSound("ItemSound.quest_itemget")
        elif npcId == LESSER_SUCCUBUS and st.getQuestItemsCount(SUCCUBUS_BLOOD) == 0 :
          st.giveItems(SUCCUBUS_BLOOD,1)
          st.playSound("ItemSound.quest_itemget")
        elif npcId == LESSER_SUCCUBUS_TUREN and st.getQuestItemsCount(SUCCUBUS_BLOOD) == 0 :
          st.giveItems(SUCCUBUS_BLOOD,1)
          st.playSound("ItemSound.quest_itemget")
        elif npcId == LESSER_SUCCUBUS_TILFO and st.getQuestItemsCount(SUCCUBUS_BLOOD) == 0 :
          st.giveItems(SUCCUBUS_BLOOD,1)
          st.playSound("ItemSound.quest_itemget")
        elif st.getQuestItemsCount(ONYX_BEAST_EYE) >= 1 and st.getQuestItemsCount(TAINT_STONE) >= 1 and st.getQuestItemsCount(SUCCUBUS_BLOOD) >= 1 :
          st.set("cond","2")
          st.set("id","2")       
          st.playSound("ItemSound.quest_middle")
   return

QUEST = Quest(3,"3_WillTheSealBeBroken","Will the Seal be Broken?")
QUEST.addStartNpc(TALLOTH)

QUEST.addTalkId(TALLOTH)

QUEST.addKillId(OMEN_BEAST)
QUEST.addKillId(TAINTED_ZOMBIE)
QUEST.addKillId(STINK_ZOMBIE)
QUEST.addKillId(LESSER_SUCCUBUS)
QUEST.addKillId(LESSER_SUCCUBUS_TUREN)
QUEST.addKillId(LESSER_SUCCUBUS_TILFO)