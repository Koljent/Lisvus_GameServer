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

import java.util.Iterator;

import net.sf.l2j.gameserver.model.L2ManufactureItem;
import net.sf.l2j.gameserver.model.L2ManufactureList;
import net.sf.l2j.gameserver.model.L2RecipeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * dd d(dd) d(ddd)
 * @version $Revision: 1.1.2.2.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class RecipeShopManageList extends L2GameServerPacket
{
	private static final String _S__D8_RecipeShopManageList = "[S] d8 RecipeShopManageList";
	private final L2PcInstance _seller;
	private final boolean _isDwarven;
	private L2RecipeList[] _recipes;
	
	public RecipeShopManageList(L2PcInstance seller, boolean isDwarven)
	{
		_seller = seller;
		_isDwarven = isDwarven;
		
		if (_isDwarven && _seller.hasDwarvenCraft())
		{
			_recipes = _seller.getDwarvenRecipeBook();
		}
		else
		{
			_recipes = _seller.getCommonRecipeBook();
		}
		
		// clean previous recipes
		if (seller.getCreateList() != null)
		{
			final Iterator<L2ManufactureItem> it = seller.getCreateList().getList().iterator();
			while (it.hasNext())
			{
				L2ManufactureItem item = it.next();
				if (item.isDwarven() != _isDwarven || !seller.hasRecipeList(item.getRecipeId()))
				{
					it.remove();
				}
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xd8);
		writeD(_isDwarven ? 0x00 : 0x01);
		writeD((int) _seller.getCurrentMp());
		writeD(_seller.getMaxMp());
		
		if (_recipes == null)
		{
			writeD(0);
		}
		else
		{
			writeD(_recipes.length);// number of items in recipe book
			
			for (int i = 0; i < _recipes.length; i++)
			{
				L2RecipeList temp = _recipes[i];
				writeD(temp.getId());
				writeD(i + 1);
			}
		}
		
		if (_seller.getCreateList() == null)
		{
			writeD(0);
		}
		else
		{
			L2ManufactureList list = _seller.getCreateList();
			writeD(list.size());
			
			for (L2ManufactureItem item : list.getList())
			{
				writeD(item.getRecipeId());
				writeD(0x00);
				writeD(item.getCost());
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__D8_RecipeShopManageList;
	}
}