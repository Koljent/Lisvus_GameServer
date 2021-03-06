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

import java.util.concurrent.Future;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

/**
 * A damage zone
 *
 * @author  durgus
 */
public class L2DamageZone extends L2ZoneType
{
    private int _damagePerSec;
    private Future<?> _task;

    public L2DamageZone(int id)
    {
        super(id);

        // Setup default damage
        _damagePerSec = 100;
    }

    @Override
    public void setParameter(String name, String value)
    {
        if (name.equals("dmgSec"))
            _damagePerSec = Integer.parseInt(value);
        else
            super.setParameter(name, value);
    }

    @Override
    protected void onEnter(L2Character character)
    {
        if (_task == null)
            _task = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new ApplyDamage(this), 10, 1000);
    }

    @Override
    protected void onExit(L2Character character)
    {
        if (_characterList.isEmpty())
        {
            _task.cancel(true);
            _task = null;
        }
    }

    protected int getDamagePerSecond()
    {
        return _damagePerSec;
    }

    class ApplyDamage implements Runnable
    {
        private L2DamageZone _dmgZone;
        ApplyDamage(L2DamageZone zone)
        {
            _dmgZone = zone;
        }

        @Override
		public void run()
        {
            for (L2Character temp : _dmgZone.getCharacterList())
            {
                if (temp == null)
                    continue;

                if (temp instanceof L2PlayableInstance && !temp.isDead())
                    temp.reduceCurrentHp(_dmgZone.getDamagePerSecond(), null);
            }
        }
    }
}