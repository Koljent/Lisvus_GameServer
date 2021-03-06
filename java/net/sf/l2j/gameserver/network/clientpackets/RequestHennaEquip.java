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
import net.sf.l2j.gameserver.datatables.HennaTable;
import net.sf.l2j.gameserver.datatables.HennaTreeTable;
import net.sf.l2j.gameserver.model.L2HennaInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Henna;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision$ $Date$
 */
public class RequestHennaEquip extends L2GameClientPacket
{
	private static final String _C__BC_RequestHennaEquip = "[C] bc RequestHennaEquip";
	// private static Logger _log = Logger.getLogger(RequestHennaEquip.class.getName());
	
	private int SymbolId;
	
	@Override
	protected void readImpl()
	{
		SymbolId = readD();
	}
	
	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		L2Henna template = HennaTable.getInstance().getTemplate(SymbolId);
		
		if (template == null)
		{
			return;
		}
		
		L2HennaInstance henna = new L2HennaInstance(template);
		int _count = 0;
		
		/**
		 * Prevents henna drawing exploit:
		 * 1) talk to L2SymbolMakerInstance
		 * 2) RequestHennaList
		 * 3) Don't close the window and go to a GrandMaster and change your subclass
		 * 4) Get SymbolMaker range again and press draw You could draw any kind of henna just having the required subclass...
		 */
		boolean cheater = true;
		for (L2HennaInstance h : HennaTreeTable.getInstance().getAvailableHenna(activeChar.getClassId()))
		{
			if (h.getSymbolId() == henna.getSymbolId())
			{
				cheater = false;
				break;
			}
		}
		
		try
		{
			_count = activeChar.getInventory().getItemByItemId(henna.getItemIdDye()).getCount();
		}
		catch (Exception e)
		{
		}
		
		if (!cheater && (_count >= henna.getAmountDyeRequire()) && (activeChar.getAdena() >= henna.getPrice()) && activeChar.addHenna(henna))
		{
			activeChar.destroyItemByItemId("Henna", henna.getItemIdDye(), henna.getAmountDyeRequire(), activeChar, true);
			activeChar.getInventory().reduceAdena("Henna", henna.getPrice(), activeChar, activeChar.getLastFolkNPC());
			
			InventoryUpdate iu = new InventoryUpdate();
			iu.addItem(activeChar.getInventory().getAdenaInstance());
			activeChar.sendPacket(iu);
			
			activeChar.sendPacket(new SystemMessage(SystemMessage.SYMBOL_ADDED));
		}
		else
		{
			
			activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_DRAW_SYMBOL));
			if ((!activeChar.isGM()) && (cheater))
			{
				Util.handleIllegalPlayerAction(activeChar, "Exploit attempt: Character " + activeChar.getName() + " of account " + activeChar.getAccountName() + " tried to add a forbidden henna.", Config.DEFAULT_PUNISH);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__BC_RequestHennaEquip;
	}
}