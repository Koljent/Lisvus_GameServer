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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.6.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestSocialAction extends L2GameClientPacket
{
	private static final String _C__1B_REQUESTSOCIALACTION = "[C] 1B RequestSocialAction";
	private static Logger _log = Logger.getLogger(RequestSocialAction.class.getName());

	// format cd
	private int _actionId;

	@Override
	protected void readImpl()
	{
		_actionId = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		// You cannot do anything else while fishing
		if (activeChar.isFishing())
		{
			SystemMessage sm = new SystemMessage(SystemMessage.CANNOT_DO_WHILE_FISHING_3);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}
		
		// check if its the actionId is allowed
		if ((_actionId < 2) || (_actionId > 13))
		{
			Util.handleIllegalPlayerAction(activeChar, "Warning!! Character " + activeChar.getName() + " of account " + activeChar.getAccountName() + " requested an internal Social Action.", Config.DEFAULT_PUNISH);
			return;
		}
		
		if (!activeChar.isInStoreMode() && activeChar.getActiveRequester() == null && !activeChar.isAlikeDead() && !activeChar.isAllSkillsDisabled() && (activeChar.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE))
		{
			if (Config.DEBUG)
			{
				_log.fine("Social Action:" + _actionId);
			}

			activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), _actionId));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__1B_REQUESTSOCIALACTION;
	}
}