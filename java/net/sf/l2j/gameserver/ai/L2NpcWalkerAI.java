/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.ai;

import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcWalkerRoutesTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2NpcWalkerNode;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcWalkerInstance;

public class L2NpcWalkerAI extends L2CharacterAI implements Runnable
{
    private static final Logger _log = Logger.getLogger(L2NpcWalkerAI.class.getName());

    private static final int DEFAULT_MOVE_DELAY = 0;

    private long _nextMoveTime;

    private boolean _walkingToNextPoint = false;

    /**
     * home points for xyz
     */
    int _homeX, _homeY, _homeZ;

    /**
     * route of the current npc
     */
    private List<L2NpcWalkerNode> _route;

    /**
     * current node
     */
    private int _currentPos;

    /**
     * Constructor of L2CharacterAI.<BR><BR>
     *
     * @param accessor The AI accessor of the L2Character
     */
    public L2NpcWalkerAI(L2Character.AIAccessor accessor)
    {
        super(accessor);

        if (!Config.ALLOW_NPC_WALKERS)
            return;

        _route = NpcWalkerRoutesTable.getInstance().getRouteForNpc(getActor().getNpcId());

        // Here we need 1 second initial delay cause getActor().hasAI() will return null...
        // Constructor of L2NpcWalkerAI is called faster then ai object is attached in L2NpcWalkerInstance
        ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 1000, 1000);
    }

    @Override
	public void run()
    {
        onEvtThink();
    }

    @Override
	protected void onEvtThink()
    {
        if (!Config.ALLOW_NPC_WALKERS)
            return;

        if (isWalkingToNextPoint())
        {
            checkArrived();
            return;
        }

        if (_nextMoveTime < System.currentTimeMillis())
            walkToLocation();
    }

    /**
     * If npc can't walk to it's target then just teleport to next point
     * @param loc ignoring it
     */
    @Override
	protected void onEvtArrivedBlocked(Location loc)
    {
    	_log.warning("NpcWalker ID: " + getActor().getNpcId() + ": Blocked at rote position [" + _currentPos + "], coords: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ". Teleporting to next point.");

        int destinationX = _route.get(_currentPos).getMoveX();
        int destinationY = _route.get(_currentPos).getMoveY();
        int destinationZ = _route.get(_currentPos).getMoveZ();

        getActor().teleToLocation(destinationX, destinationY, destinationZ, false);
        super.onEvtArrivedBlocked(loc);
    }

    private void checkArrived()
    {
        int destinationX = _route.get(_currentPos).getMoveX();
        int destinationY = _route.get(_currentPos).getMoveY();
        int destinationZ = _route.get(_currentPos).getMoveZ();

        if (getActor().getX() == destinationX && getActor().getY() == destinationY && getActor().getZ() == destinationZ)
        {
            String chat = _route.get(_currentPos).getChatText();
            if (chat != null && !chat.isEmpty())
            {
                try
                {
                    getActor().broadcastChat(chat);
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    _log.info("L2NpcWalkerInstance: Error, " + e);
                }
            }

            // time in millis
            long delay = _route.get(_currentPos).getDelay()*1000;

            // sleeps between each move
            if (delay <= 0 && delay != DEFAULT_MOVE_DELAY)
            {
                delay = DEFAULT_MOVE_DELAY;
                if (Config.DEVELOPER)
                    _log.warning("Wrong Delay Set in Npc Walker Functions = " + delay + " secs, using default delay: " + DEFAULT_MOVE_DELAY + " secs instead.");
            }

            _nextMoveTime = System.currentTimeMillis() + delay;
            setWalkingToNextPoint(false);
        }
    }

    private void walkToLocation()
    {
        if (_currentPos < (_route.size() - 1))
            _currentPos++;
        else
            _currentPos = 0;

        boolean moveType = _route.get(_currentPos).getRunning();

        /**
         * false - walking
         * true - Running
         */
        if (moveType)
            getActor().setRunning();
        else
            getActor().setWalking();

        // now we define destination
        int destinationX = _route.get(_currentPos).getMoveX();
        int destinationY = _route.get(_currentPos).getMoveY();
        int destinationZ = _route.get(_currentPos).getMoveZ();

        // notify AI of MOVE_TO
        setWalkingToNextPoint(true);

        setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(destinationX, destinationY, destinationZ));
    }

    @Override
	public L2NpcWalkerInstance getActor()
    {
        return (L2NpcWalkerInstance) super.getActor();
    }

    public int getHomeX()
    {
        return _homeX;
    }

    public int getHomeY()
    {
        return _homeY;
    }

    public int getHomeZ()
    {
        return _homeZ;
    }

    public void setHomeX(int homeX)
    {
        _homeX = homeX;
    }

    public void setHomeY(int homeY)
    {
        _homeY = homeY;
    }

    public void setHomeZ(int homeZ)
    {
        _homeZ = homeZ;
    }

    public boolean isWalkingToNextPoint()
    {
        return _walkingToNextPoint;
    }

    public void setWalkingToNextPoint(boolean value)
    {
        _walkingToNextPoint = value;
    }
}