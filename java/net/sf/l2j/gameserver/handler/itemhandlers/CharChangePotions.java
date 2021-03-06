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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

/**
 * Itemhhandler for Character Appearance Change Potions
 * @author Tempy
 */
public class CharChangePotions implements IItemHandler
{
	private static int[] _itemIds =
	{
		5235,
		5236,
		5237, // Face
		5238,
		5239,
		5240,
		5241, // Hair Color
		5242,
		5243,
		5244,
		5245,
		5246,
		5247,
		5248 // Hair Style
	};

	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		int itemId = item.getItemId();

		if (!(playable instanceof L2PcInstance))
		{
			return;
		}

		L2PcInstance activeChar = (L2PcInstance) playable;

		if (activeChar.isAllSkillsDisabled())
		{
			activeChar.sendPacket(new ActionFailed());
			return;
		}

		switch (itemId)
		{
			case 5235:
				activeChar.getAppearance().setFace(0);
				break;
			case 5236:
				activeChar.getAppearance().setFace(1);
				break;
			case 5237:
				activeChar.getAppearance().setFace(2);
				break;
			case 5238:
				activeChar.getAppearance().setHairColor(0);
				break;
			case 5239:
				activeChar.getAppearance().setHairColor(1);
				break;
			case 5240:
				activeChar.getAppearance().setHairColor(2);
				break;
			case 5241:
				activeChar.getAppearance().setHairColor(3);
				break;
			case 5242:
				activeChar.getAppearance().setHairStyle(0);
				break;
			case 5243:
				activeChar.getAppearance().setHairStyle(1);
				break;
			case 5244:
				activeChar.getAppearance().setHairStyle(2);
				break;
			case 5245:
				activeChar.getAppearance().setHairStyle(3);
				break;
			case 5246:
				activeChar.getAppearance().setHairStyle(4);
				break;
			case 5247:
				activeChar.getAppearance().setHairStyle(5);
				break;
			case 5248:
				activeChar.getAppearance().setHairStyle(6);
				break;
		}

		// Create a summon effect
		MagicSkillUse MSU = new MagicSkillUse(playable, activeChar, 2003, 1, 1, 0);
		activeChar.broadcastPacket(MSU);

		// Update the changed stat for the character in the DB.
		activeChar.store();

		// Remove the item from inventory.
		activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);

		// Broadcast the changes to the char and all those nearby.
		activeChar.broadcastPacket(new UserInfo(activeChar));
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}