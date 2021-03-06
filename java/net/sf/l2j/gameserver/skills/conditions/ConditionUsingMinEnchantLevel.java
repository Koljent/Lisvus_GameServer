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
package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author DnR
 *
 */
public final class ConditionUsingMinEnchantLevel extends Condition
{
	private final int _enchantLevel;
	
	public ConditionUsingMinEnchantLevel(int enchantLevel)
	{
		_enchantLevel = enchantLevel;
	}
	
	@Override
	public boolean testImpl(Env env, Object owner)
	{
		if (owner == null || !(owner instanceof L2ItemInstance))
		{
			return false;
		}
		
		return ((L2ItemInstance) owner).getEnchantLevel() >= _enchantLevel;
	}
}