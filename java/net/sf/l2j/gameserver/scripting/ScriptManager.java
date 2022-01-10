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
package net.sf.l2j.gameserver.scripting;

/**
 *
 * @author  KenM
 * @param <S> 
 */
public abstract class ScriptManager<S extends ManagedScript>
{
    public abstract Iterable<S> getAllManagedScripts();

    public boolean reload(S ms)
    {
        return ms.reload();
    }
    
    public void unload(S ms)
    {
    	ms.unload(true);
    }
    
    public void setActive(S ms, boolean status)
    {
        ms.setActive(status);
    }

    public abstract String getScriptManagerName();
}