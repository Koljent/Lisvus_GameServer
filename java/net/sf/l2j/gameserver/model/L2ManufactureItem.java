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
package net.sf.l2j.gameserver.model;

import net.sf.l2j.gameserver.RecipeController;

/**
 * This class ...
 * 
 * @version $Revision: 1.1.2.2.2.1 $ $Date: 2005/03/27 15:29:32 $
 */
public class L2ManufactureItem 
{
    private int _recipeId;
    private int _cost;
    private boolean _isDwarven;

    public L2ManufactureItem(int recipeId, int cost)
    {
        _recipeId = recipeId;
        _cost = cost;
        _isDwarven = RecipeController.getInstance().getRecipeById(_recipeId).isDwarvenRecipe();
    }
    
    public int getRecipeId()
    {
        return _recipeId;
    }
    
    public int getCost()
    {
        return _cost;
    }

    public boolean isDwarven()
    {
        return _isDwarven;
    }
}