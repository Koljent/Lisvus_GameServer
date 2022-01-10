# Made by disKret (adapted for L2JLisvus by roko91)
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "15_SweetWhisper"

#NPC
VLADIMIR = 8302
HIERARCH = 8517
M_NECROMANCER = 8518

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   if event == "8302-1.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   if event == "8518-1.htm" :
     if cond == 1 :
       st.set("cond","2")
   if event == "8517-1.htm" :
     if cond == 2 :
       st.addExpAndSp(60217,0)
       st.set("cond","0")
       st.playSound("ItemSound.quest_finish")
       st.setState(State.COMPLETED)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   cond = st.getInt("cond")
   id = st.getState()
   level = st.getPlayer().getLevel()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == VLADIMIR and st.getInt("cond") == 0 :
     if level >= 60 :
       htmltext = "8302-0.htm"
       return htmltext
     if id == State.COMPLETED :
       htmltext = JQuest.getAlreadyCompletedMsg()
       return htmltext
     else:
       htmltext = "8302-0a.htm"
       st.exitQuest(1)
   if npcId == VLADIMIR and cond == 1 :
       htmltext = "8302-1a.htm"
   if id == State.STARTED :
       if npcId == M_NECROMANCER and cond == 1 :
         htmltext = "8518-0.htm"
       elif npcId == M_NECROMANCER and cond == 2 :
         htmltext = "8518-1a.htm"
       elif npcId == HIERARCH and cond == 2 :
         htmltext = "8517-0.htm"
   return htmltext

QUEST = Quest(15,qn,"Sweet Whisper")
QUEST.addStartNpc(8302)

QUEST.addTalkId(8302)
QUEST.addTalkId(8517)
QUEST.addTalkId(8518)