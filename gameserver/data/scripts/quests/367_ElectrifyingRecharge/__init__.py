# Electrifying Recharge! - v0.1 by DrLecter (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j.gameserver.datatables import SkillTable
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "367_ElectrifyingRecharge"

#NPC
LORAIN = 7673
#MOBS
CATHEROK=1035

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(5875,5881)

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   lamp = st.getQuestItemsCount(5875)
   if event == "7673-03.htm" and cond == 0 and not lamp:
     if st.getPlayer().getLevel() >= 37 :
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        st.giveItems(5875,1)
     else :
        htmltext = "7673-02.htm"
        st.exitQuest(1)
   elif event == "7673-08.htm" :
     st.playSound("ItemSound.quest_finish")
     st.exitQuest(1)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   cond=st.getInt("cond")
   relic=st.getQuestItemsCount(5879)
   broken=st.getQuestItemsCount(5880)
   if cond == 0 :
      htmltext = "7673-01.htm"
   elif cond == 1 :
     if not relic and not broken :
        htmltext = "7673-04.htm"
     elif broken :
        htmltext = "7673-05.htm"
        st.takeItems(5880,-1)
        st.giveItems(5875,1)
   elif cond == 2 and relic :
     st.takeItems(5879,-1)
     st.giveItems(4553+st.getRandom(12),1)
     st.giveItems(5875,1)
     st.set("cond","1")
     htmltext = "7673-06.htm"
   return htmltext

 def onAttack (self,npc,player,damage,isPet):
   st = player.getQuestState(qn)
   if not st :
      return
   if st.getState() != State.STARTED :
      return
   chance=st.getRandom(100)
   if chance < 3 :
      count = 0
      for item in range(5875,5879):
         if st.getQuestItemsCount(item) :
            count += 1
            st.takeItems(item,-1)
      if count:
         st.giveItems(5880,1)
   elif chance < 7 :
      for item in range(5875,5879):
         if st.getQuestItemsCount(item) :
            npc.doCast(SkillTable.getInstance().getInfo(4072,4))
            st.takeItems(item,-1)
            st.giveItems(item+1,1)
            if item < 5878 :
               st.playSound("ItemSound.quest_itemget")
            elif item == 5878 :
               st.playSound("ItemSound.quest_middle")
               st.set("cond","2")
            break
   return

QUEST = Quest(367,qn,"Electrifying Recharge")
QUEST.addStartNpc(LORAIN)

QUEST.addTalkId(LORAIN)
QUEST.addAttackId(CATHEROK)