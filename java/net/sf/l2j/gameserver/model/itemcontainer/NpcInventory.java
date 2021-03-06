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
package net.sf.l2j.gameserver.model.itemcontainer;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ItemInstance.ItemLocation;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;

public class NpcInventory extends Inventory
{
   private final L2NpcInstance _owner;

    public boolean sshotInUse = false;
    public boolean bshotInUse = false;

    public NpcInventory(L2NpcInstance owner)
    {
        _owner = owner;
    }

    public void reset()
    {
        destroyAllItems("Reset", null, null);
        if (_owner.getTemplate().ss > 0)
            addItem("Reset", 1835, _owner.getTemplate().ss, null, null);

        if (_owner.getTemplate().bss > 0)
            addItem("Reset", 3947, _owner.getTemplate().bss, null, null);
    }

    @Override
    public L2NpcInstance getOwner()
    {
        return _owner;
    }

    @Override
    protected ItemLocation getBaseLocation()
    {
        return ItemLocation.NPC;
    }

    @Override
    protected ItemLocation getEquipLocation()
    {
        return ItemLocation.NPC;
    }

    /**
     * Returns the list of all items in inventory that have a given item id.
     * @param itemId 
     * @return L2ItemInstance[] : matching items from inventory
     */
    public L2ItemInstance[] getAllItemsByItemId(int itemId)
    {
        List<L2ItemInstance> list = new ArrayList<>();
        for (L2ItemInstance item : _items)
        {
            if (item.getItemId() == itemId)
                list.add(item);
        }

        return list.toArray(new L2ItemInstance[list.size()]);
    }
}