# Made by DooMIta (ethernaly@email.it) and DrLecter (adapted for L2JLisvus by roko91).
# Visit http://www.l2jdp.com/trac if you find a bug and wish to report it.
# Visit http://forum.l2jdp.com for more details about our community and the project.

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "27_ChestCaughtWithABaitOfWind"

#NPC
LANOSCO = 8570
SHALING = 8434

#QUEST ITEMs and REWARD
BLUE_TREASURE_BOX    = 6500
STRANGE_BLUESPRINT   = 7625
BLACK_PEARL_RING     = 880

class Quest (JQuest) :

  def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)
  
  def onEvent(self, event, st):
    htmltext = event
    if event== "8570-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "8570-05.htm" and st.getQuestItemsCount(BLUE_TREASURE_BOX) :
      htmltext="8570-06.htm"
      st.playSound("ItemSound.quest_middle")
      st.giveItems(STRANGE_BLUESPRINT, 1)
      st.takeItems(BLUE_TREASURE_BOX,-1)
      st.set("cond","2")
    elif event == "8434-02.htm" and st.getQuestItemsCount(STRANGE_BLUESPRINT) :
      htmltext = "8434-01.htm"
      st.playSound("ItemSound.quest_finish")
      st.giveItems(BLACK_PEARL_RING, 1)
      st.takeItems(STRANGE_BLUESPRINT,-1)
      st.unset("cond")
      st.setState(State.COMPLETED)
    return htmltext

  def onTalk(self,npc,st):
    htmltext=JQuest.getNoQuestMsg()
    npcId=npc.getNpcId()
    id = st.getState()
    if id == State.CREATED :
      req = st.getPlayer().getQuestState("50_LanoscosSpecialBait")
      if req : reqst = req.getState()
      if st.getPlayer().getLevel() >= 27 and req and reqst.getName() == 'Completed' :
        htmltext = "8570-01.htm"
      else :
        st.exitQuest(1)
        htmltext = "8570-02.htm"
    elif id == State.STARTED :
      cond = st.getInt("cond")
      if npcId == LANOSCO :
        if cond == 1 :
          if st.getQuestItemsCount(BLUE_TREASURE_BOX) :
            htmltext = "8570-04.htm"
          else :
            htmltext = "8570-05.htm"
        else :
          htmltext = "8570-07.htm"
      else :
        htmltext = "8434-00.htm"
    elif id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
    return htmltext

QUEST = Quest(27,qn,"Chest Caught With A Bait Of Wind")
QUEST.addStartNpc(LANOSCO)

QUEST.addTalkId(LANOSCO)
QUEST.addTalkId(SHALING)