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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ManufactureItem;
import net.sf.l2j.gameserver.model.L2ManufactureList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.RecipeShopMsg;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ... cd(dd)
 * @version $Revision: 1.1.2.3.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRecipeShopListSet extends L2GameClientPacket
{
	private static final String _C__B2_RequestRecipeShopListSet = "[C] b2 RequestRecipeShopListSet";
	// private static Logger _log = Logger.getLogger(RequestRecipeShopListSet.class.getName());
	
	private int _count;
	private int[] _items; // count*2
	
	@Override
	protected void readImpl()
	{
		_count = readD();
		_items = new int[_count * 2];
		for (int x = 0; x < _count; x++)
		{
			_items[(x * 2) + 0] = readD(); // recipeId
			_items[(x * 2) + 1] = readD(); // cost
		}
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (player.isAttackingDisabled() || player.isConfused() || player.isImmobilized() || player.isCastingNow())
		{
			return;
		}
		
		// Something is wrong here
		if (player.getPrivateStoreType() != PrivateStoreType.DWARVEN_MANUFACTURE_MANAGE && player.getPrivateStoreType() != PrivateStoreType.GENERAL_MANUFACTURE_MANAGE)
		{
			return;
		}
		
		if (player.isInsideZone(L2Character.ZONE_NO_STORE))
		{
			player.sendPacket(new SystemMessage(SystemMessage.NO_PRIVATE_WORKSHOP_HERE));
			player.sendPacket(new ActionFailed());
			return;
		}
		
		if (_count == 0)
		{
			player.setPrivateStoreType(PrivateStoreType.NONE);
			player.broadcastUserInfo();
		}
		else
		{
			L2ManufactureList createList = new L2ManufactureList();
			for (int x = 0; x < _count; x++)
			{
				int recipeID = _items[(x * 2) + 0];
				int cost = _items[(x * 2) + 1];
				
				if (!player.hasRecipeList(recipeID))
				{
					Util.handleIllegalPlayerAction(player, "Warning!! Player " + player.getName() + " of account " + player.getAccountName() + " tried to set recipe which he dont have.", Config.DEFAULT_PUNISH);
					return;
				}
				
				createList.add(new L2ManufactureItem(recipeID, cost));
			}
			
			createList.setStoreName(player.getCreateList() != null ? player.getCreateList().getStoreName() : "");
			player.setCreateList(createList);
			
			player.setPrivateStoreType(player.getPrivateStoreType() == PrivateStoreType.DWARVEN_MANUFACTURE_MANAGE ? PrivateStoreType.DWARVEN_MANUFACTURE : PrivateStoreType.GENERAL_MANUFACTURE);
			player.sitDown();
			player.broadcastUserInfo();
			player.sendPacket(new RecipeShopMsg(player));
			player.broadcastPacket(new RecipeShopMsg(player));
		}
	}
	
	@Override
	public String getType()
	{
		return _C__B2_RequestRecipeShopListSet;
	}
}