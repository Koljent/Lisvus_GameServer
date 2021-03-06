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
package net.sf.l2j.loginserver.serverpackets;

import net.sf.l2j.loginserver.L2LoginClient;

public final class Init extends L2LoginServerPacket
{
	private final int _sessionId;
	private final byte[] _publicKey;
	private final byte[] _blowfishKey;
	
	public Init(L2LoginClient client)
	{
		this(client.getScrambledModulus(), client.getBlowfishKey(), client.getSessionId());
	}
	
	public Init(byte[] publickey, byte[] blowfishkey, int sessionId)
	{
		_sessionId = sessionId;
		_publicKey = publickey;
		_blowfishKey = blowfishkey;
	}

	/**
	 * @see net.sf.l2j.mmocore.SendablePacket#write()
	 */
	@Override
	protected void write()
	{
		writeC(0x00);
		writeD(_sessionId);
		writeD(0x0000c621); // protocol revision

		writeB(_publicKey); // RSA Public Key

		// Definitely GG related
		writeD(0x29DD954E);
		writeD(0x77C39CFC);
		writeD(0x97ADB620);
		writeD(0x07BDE0F7);
		
		if (_blowfishKey != null)
		{
			writeB(_blowfishKey); // BlowFish key
			writeC(0x00); // null termination ;)
		}
	}
}