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
package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.network.serverpackets.NpcInfo;
import net.sf.l2j.gameserver.network.serverpackets.ServerObjectInfo;

public class L2WaterZone extends L2ZoneType
{
    public L2WaterZone(int id)
    {
        super(id);
    }

    @Override
    protected void onEnter(L2Character character)
    {
        character.setInsideZone(L2Character.ZONE_WATER, true);

        if (character instanceof L2PcInstance)
        {
            L2PcInstance player = (L2PcInstance)character;
            player.broadcastUserInfo();
        }
        else if (character instanceof L2NpcInstance)
        {
            for (L2PcInstance player : character.getKnownList().getKnownPlayers().values())
            {
                if (player != null)
                {
                    if (character.getMoveSpeed() == 0)
                        player.sendPacket(new ServerObjectInfo((L2NpcInstance)character, player));
                    else
                        player.sendPacket(new NpcInfo((L2NpcInstance)character, player));
                }
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(L2Character.ZONE_WATER, false);

        // TODO: update to only send speed status when that packet is known
        if (character instanceof L2PcInstance)
            ((L2PcInstance)character).broadcastUserInfo();
        else if (character instanceof L2NpcInstance)
        {
            for (L2PcInstance player : character.getKnownList().getKnownPlayers().values())
            {
                if (player != null)
                {
                    if (character.getMoveSpeed() == 0)
                        player.sendPacket(new ServerObjectInfo((L2NpcInstance)character, player));
                    else
                        player.sendPacket(new NpcInfo((L2NpcInstance)character, player));
                }
            }
        }
    }

    public int getWaterZ()
    {
        return getZone().getHighZ();
    }
}