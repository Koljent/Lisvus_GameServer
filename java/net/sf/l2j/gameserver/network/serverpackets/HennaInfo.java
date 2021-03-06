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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.L2HennaInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class HennaInfo extends L2GameServerPacket
{
	private static final String _S__E4_HennaInfo = "[S] E4 HennaInfo";
	
	private final L2PcInstance _player;
	private final L2HennaInstance[] _hennas = new L2HennaInstance[3];
	private final int _count;
	
	public HennaInfo(L2PcInstance player)
	{
		_player = player;
		
		int j = 0;
		for (int i = 0; i < 3; i++)
		{
			L2HennaInstance h = _player.getHenna(i + 1);
			if (h != null)
			{
				_hennas[j++] = h;
			}
		}
		_count = j;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe4);
		
		writeC(_player.getHennaStatINT()); // equip INT
		writeC(_player.getHennaStatSTR()); // equip STR
		writeC(_player.getHennaStatCON()); // equip CON
		writeC(_player.getHennaStatMEN()); // equip MEN
		writeC(_player.getHennaStatDEX()); // equip DEX
		writeC(_player.getHennaStatWIT()); // equip WIT
		
		writeD(3); // slots?
		
		writeD(_count); // size
		for (int i = 0; i < _count; i++)
		{
			writeD(_hennas[i].getSymbolId());
			writeD(_hennas[i].getSymbolId());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__E4_HennaInfo;
	}
}