# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

KENDNELLS_ORDER1_ID = 1836
KENDNELLS_ORDER2_ID = 1837
KENDNELLS_ORDER3_ID = 1838
KENDNELLS_ORDER4_ID = 1839
KENDNELLS_ORDER5_ID = 1840
KENDNELLS_ORDER6_ID = 1841
KENDNELLS_ORDER7_ID = 1842
KENDNELLS_ORDER8_ID = 1843
KABOO_CHIEF_TORC1_ID = 1844
KABOO_CHIEF_TORC2_ID = 1845
RED_SUNSET_SWORD_ID = 981
RED_SUNSET_STAFF_ID = 754
SPIRITSHOT_FOR_BEGINNERS = 5790
SOULSHOT_FOR_BEGINNERS = 5789

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [KENDNELLS_ORDER1_ID, KENDNELLS_ORDER2_ID, KENDNELLS_ORDER3_ID, KENDNELLS_ORDER4_ID, KENDNELLS_ORDER5_ID, KENDNELLS_ORDER6_ID, KENDNELLS_ORDER7_ID, KENDNELLS_ORDER8_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("id","0")
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      htmltext = "7218-03.htm"
      if st.getQuestItemsCount(KENDNELLS_ORDER1_ID)+st.getQuestItemsCount(KENDNELLS_ORDER2_ID)+st.getQuestItemsCount(KENDNELLS_ORDER3_ID)+st.getQuestItemsCount(KENDNELLS_ORDER4_ID) == 0 :
        n = st.getRandom(100)
        if n < 25 :
          st.giveItems(KENDNELLS_ORDER1_ID,1)
        elif n < 50 :
          st.giveItems(KENDNELLS_ORDER2_ID,1)
        elif n < 75 :
          st.giveItems(KENDNELLS_ORDER3_ID,1)
        else:
          st.giveItems(KENDNELLS_ORDER4_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7218 and id == State.CREATED :
      if st.getPlayer().getLevel() >= 10 and st.getPlayer().getRace().ordinal() == 1 :
        htmltext = "7218-02.htm"
        return htmltext
      elif st.getPlayer().getRace().ordinal() != 1 :
        htmltext = "7218-00.htm"
        st.exitQuest(1)
      else:
        htmltext = "7218-10.htm"
        st.exitQuest(1)
   elif npcId == 7218 and st.getInt("cond") :
      if st.getQuestItemsCount(KABOO_CHIEF_TORC1_ID) :
        htmltext = "7218-06.htm"
        if st.getQuestItemsCount(KENDNELLS_ORDER1_ID) :
          st.takeItems(KENDNELLS_ORDER1_ID,1)
        if st.getQuestItemsCount(KENDNELLS_ORDER2_ID) :
          st.takeItems(KENDNELLS_ORDER2_ID,1)
        if st.getQuestItemsCount(KENDNELLS_ORDER3_ID) :
          st.takeItems(KENDNELLS_ORDER3_ID,1)
        if st.getQuestItemsCount(KENDNELLS_ORDER4_ID) :
          st.takeItems(KENDNELLS_ORDER4_ID,1)
        st.takeItems(KABOO_CHIEF_TORC1_ID,1)
        n = st.getRandom(100)
        if n < 25 :
          st.giveItems(KENDNELLS_ORDER5_ID,1)
        elif n < 50 :
          st.giveItems(KENDNELLS_ORDER6_ID,1)
        elif n < 75 :
          st.giveItems(KENDNELLS_ORDER7_ID,1)
        else:
          st.giveItems(KENDNELLS_ORDER8_ID,1)
      elif st.getQuestItemsCount(KENDNELLS_ORDER1_ID) or st.getQuestItemsCount(KENDNELLS_ORDER2_ID) or st.getQuestItemsCount(KENDNELLS_ORDER3_ID) or st.getQuestItemsCount(KENDNELLS_ORDER4_ID) :
        htmltext = "7218-05.htm"
      elif st.getQuestItemsCount(KABOO_CHIEF_TORC2_ID) :
        if st.getInt("id") != 105 :
          st.set("id","105")
          htmltext = "7218-08.htm"
          if st.getQuestItemsCount(KENDNELLS_ORDER5_ID) :
            st.takeItems(KENDNELLS_ORDER5_ID,1)
          if st.getQuestItemsCount(KENDNELLS_ORDER6_ID) :
            st.takeItems(KENDNELLS_ORDER6_ID,1)
          if st.getQuestItemsCount(KENDNELLS_ORDER7_ID) :
            st.takeItems(KENDNELLS_ORDER7_ID,1)
          if st.getQuestItemsCount(KENDNELLS_ORDER8_ID) :
            st.takeItems(KENDNELLS_ORDER8_ID,1)
          st.takeItems(KABOO_CHIEF_TORC2_ID,1)
          mage = st.getPlayer().getClassId().isMage()
          if mage :
		    st.giveItems(RED_SUNSET_STAFF_ID,1)
          else :
            st.giveItems(RED_SUNSET_SWORD_ID,1)
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
          st.set("cond","0")
          qs = st.getPlayer().getQuestState("255_Tutorial")
          if qs :
             newbiegift=qs.getInt("newbiegift")
             if newbiegift != 2 and st.getPlayer().isNewbie() :
                st.showQuestionMark(26)
                if st.getPlayer().getClassId().isMage() :
                   st.playTutorialVoice("tutorial_voice_027")
                   st.giveItems(SPIRITSHOT_FOR_BEGINNERS,3000)
                else :
                   st.playTutorialVoice("tutorial_voice_026")
                   st.giveItems(SOULSHOT_FOR_BEGINNERS,7000)
                qs.set("newbiegift","2")
      elif st.getQuestItemsCount(KENDNELLS_ORDER5_ID) or st.getQuestItemsCount(KENDNELLS_ORDER6_ID) or st.getQuestItemsCount(KENDNELLS_ORDER7_ID) or st.getQuestItemsCount(KENDNELLS_ORDER8_ID) :
        htmltext = "7218-07.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("105_SkirmishWithOrcs")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 5059 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER1_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC1_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC1_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5060 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER2_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC1_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC1_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5061 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER3_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC1_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC1_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5062 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER4_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC1_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC1_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5064 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER5_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC2_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC2_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5065 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER6_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC2_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC2_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5067 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER7_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC2_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC2_ID,1)
         st.playSound("ItemSound.quest_middle")
      elif npcId == 5068 :
       st.set("id","0")
       if st.getInt("cond") == 1 :
        if st.getQuestItemsCount(KENDNELLS_ORDER8_ID) and st.getQuestItemsCount(KABOO_CHIEF_TORC2_ID) == 0 :
         st.giveItems(KABOO_CHIEF_TORC2_ID,1)
         st.playSound("ItemSound.quest_middle")
   return

QUEST = Quest(105,"105_SkirmishWithOrcs","Skirmish With Orcs")
QUEST.addStartNpc(7218)

QUEST.addTalkId(7218)

QUEST.addKillId(5059)
QUEST.addKillId(5060)
QUEST.addKillId(5061)
QUEST.addKillId(5062)
QUEST.addKillId(5064)
QUEST.addKillId(5065)
QUEST.addKillId(5067)
QUEST.addKillId(5068)