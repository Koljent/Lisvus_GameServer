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
package net.sf.l2j.gameserver.model.actor.knownlist;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;

public class AttackableKnownList extends NpcKnownList
{
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public AttackableKnownList(L2Attackable activeChar)
	{
		super(activeChar);
	}
	
	// =========================================================
	// Method - Public
	@Override
	public boolean removeKnownObject(L2Object object)
	{
		if (!super.removeKnownObject(object))
		{
			return false;
		}
		
		if (object instanceof L2Character)
		{
			// Remove the L2Object from the _aggrolist of the L2Attackable
			getActiveChar().removeFromAggroList((L2Character) object);

			// Check if the _aggroList of the L2GuardInstance is Empty
			L2CharacterAI ai = getActiveChar().getAI();
			if (ai != null && getActiveChar().noTarget())
			{
				ai.setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
			}
		}
		return true;
	}
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
	@Override
	public L2Attackable getActiveChar()
	{
		return (L2Attackable) super.getActiveChar();
	}
	
	@Override
	public int getDistanceToForgetObject(L2Object object)
	{
		if (getActiveChar().getAggroList().containsKey(object))
		{
			return 3000;
		}

		return Math.min(2200, 2 * getDistanceToWatchObject(object));
	}
	
	@Override
	public int getDistanceToWatchObject(L2Object object)
	{
		if ((object instanceof L2FolkInstance) || !(object instanceof L2Character))
		{
			return 0;
		}
		
		if (object instanceof L2PlayableInstance)
		{
			return object.getKnownList().getDistanceToWatchObject(getActiveObject());
		}
		
		if (getActiveChar().getAggroRange() > getActiveChar().getFactionRange())
		{
			return getActiveChar().getAggroRange();
		}
		
		if (getActiveChar().getFactionRange() > 300)
		{
			return getActiveChar().getFactionRange();
		}
		
		return 300;
	}
}