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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;

public class castle implements IVoicedCommandHandler
{
    private static String[] _voicedCommands =
    {
        "open doors",
        "close doors",
        "ride wyvern"
    }; 

    @Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
        if (target.equals("castle") && activeChar.isClanLeader())
        {
            L2DoorInstance door = (L2DoorInstance) activeChar.getTarget();
            Castle castle = CastleManager.getInstance().getCastleById(activeChar.getClan().getHasCastle());

            if (door == null || castle == null)
                    return false;

    	    if (command.startsWith("open doors"))
            {
                if (castle.checkIfInZone(door.getX(), door.getY(), door.getZ()))
	            door.openMe();
            }
    	    else if (command.startsWith("close doors"))
            {
                if (castle.checkIfInZone(door.getX(), door.getY(), door.getZ()))
                    door.closeMe();
            }
            else if (command.startsWith("ride wyvern"))
            {
                if (activeChar.getClan().getHasCastle() > 0 && activeChar.isClanLeader())
                    activeChar.mount(12621, 0, true);
            }
    	}
    	return true;
    }

    @Override
	public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}