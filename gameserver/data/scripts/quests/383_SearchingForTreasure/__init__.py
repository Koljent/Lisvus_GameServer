# Made by mtrix & DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

SHARK=314
PIRATES_TREASURE_MAP = 5915
PIRATES_CHEST = 8148
ESPEN = 7890

#itemid:[maxqty,chance in 1000].
REWARDS={1338:[2,150],3452:[1,140],1337:[1,130],3455:[1,120],4409:[1,220],4408:[1,220],4418:[1,220],4419:[1,220],956:[1,15],952:[1,8],2451:[1,2],2450:[1,2]}

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st) :
     htmltext = event
     if event == "7890-03.htm" :
        st.set("cond","1")
        st.setState(State.STARTED)
     elif event == "7890-07.htm" :
        if st.getQuestItemsCount(PIRATES_TREASURE_MAP) :
           st.set("cond","2")
           st.takeItems(PIRATES_TREASURE_MAP,1)
           st.addSpawn(PIRATES_CHEST,106583,197747,-4209,900000)
           st.addSpawn(SHARK,106570,197740,-4209,900000)
           st.addSpawn(SHARK,106580,197747,-4209,900000)
           st.addSpawn(SHARK,106590,197743,-4209,900000)
           st.playSound("ItemSound.quest_accept")
        else:
           htmltext="You don't have required items"
           st.exitquest(1)
     elif event == "7890-02b.htm":
        if st.getQuestItemsCount(PIRATES_TREASURE_MAP) :
           st.takeItems(PIRATES_TREASURE_MAP,1)
           st.giveItems(57,1000)
           st.playSound("ItemSound.quest_finish")
        else:
           htmltext="You don't have required items"
        st.exitQuest(1)
     elif event == "8148-02.htm":
        if st.getQuestItemsCount(1661):
           st.takeItems(1661,1)
           st.giveItems(57,500+(st.getRandom(5)*300))
           count=0
           while count < 1 :
             for item in REWARDS.keys() :
              qty,chance=REWARDS[item]
              if st.getRandom(1000) < chance and count < 2 :
                 st.giveItems(item,st.getRandom(qty)+1)
                 count+=1
              if count < 2 :
                for i in range(4481,4505) :
                  if st.getRandom(500) == 1 and count < 2 :
                     st.giveItems(i,1)
                     count+=1
           st.playSound("ItemSound.quest_finish")
           st.exitQuest(1)
        else :
           htmltext = "8148-03.htm"
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     if id == State.CREATED :
       if st.getPlayer().getLevel() >= 42:  
          if st.getQuestItemsCount(PIRATES_TREASURE_MAP) :
            htmltext = "7890-01.htm"
          else :
            htmltext = "7890-00.htm"
            st.exitQuest(1)
       else :
          htmltext = "7890-01a.htm"
          st.exitQuest(1)
     elif npcId == ESPEN :
        htmltext = "7890-03a.htm"
     elif npcId == PIRATES_CHEST and st.getInt("cond") == 2 :
        htmltext = "8148-01.htm"
     return htmltext

QUEST = Quest(383,"383_SearchingForTreasure","Searching For Treasure")
QUEST.addStartNpc(ESPEN)

QUEST.addTalkId(ESPEN)
QUEST.addTalkId(PIRATES_CHEST)