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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;
import net.sf.l2j.util.StringUtil;

public class AdminFightCalculator implements IAdminCommandHandler
{
    private static String[] _adminCommands =
    {
        "admin_fight_calculator",
        "admin_fight_calculator_show",
        "admin_fcs"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        try
        {
    		if (command.startsWith("admin_fight_calculator_show"))
    			handleShow(command.substring("admin_fight_calculator_show".length()), activeChar);
    		else if (command.startsWith("admin_fcs"))
    			handleShow(command.substring("admin_fcs".length()), activeChar);
    		else if (command.startsWith("admin_fight_calculator"))
    			handleStart(command.substring("admin_fight_calculator".length()), activeChar);
        }
        catch (StringIndexOutOfBoundsException e) {}
        return true;
    }
	
	private void handleStart(String params, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(params);
		int lvl1 = 0;
		int lvl2 = 0;
		int mid1 = 0;
		int mid2 = 0;
		while (st.hasMoreTokens())
		{
			String s = st.nextToken();
			if (s.equals("lvl1")) { lvl1 = Integer.parseInt(st.nextToken()); continue; }
			if (s.equals("lvl2")) { lvl2 = Integer.parseInt(st.nextToken()); continue; }
			if (s.equals("mid1")) { mid1 = Integer.parseInt(st.nextToken()); continue; }
			if (s.equals("mid2")) { mid2 = Integer.parseInt(st.nextToken()); continue; }
		}
		
		L2NpcTemplate npc1 = null;
		if (mid1 != 0)
			npc1 = NpcTable.getInstance().getTemplate(mid1);
		L2NpcTemplate npc2 = null;
		if (mid2 != 0)
			npc2 = NpcTable.getInstance().getTemplate(mid2);

		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		
		StringBuilder replyMSG = new StringBuilder(500);
		if (npc1 != null && npc2 != null)
		{
			StringUtil.append(replyMSG, "<html><title>Selected mobs to fight</title>");
			StringUtil.append(replyMSG, "<body>");
			StringUtil.append(replyMSG, "<table>");
			StringUtil.append(replyMSG, "<tr><td>First</td><td>Second</td></tr>");
			StringUtil.append(replyMSG, "<tr><td>level "+lvl1+"</td><td>level "+lvl2+"</td></tr>");
			StringUtil.append(replyMSG, "<tr><td>id "+npc1.npcId+"</td><td>id "+npc2.npcId+"</td></tr>");
			StringUtil.append(replyMSG, "<tr><td>"+npc1.name+"</td><td>"+npc2.name+"</td></tr>");
			StringUtil.append(replyMSG, "</table>");
			StringUtil.append(replyMSG, "<center><br><br><br>");
			StringUtil.append(replyMSG, "<button value=\"OK\" action=\"bypass -h admin_fight_calculator_show "+npc1.npcId+" "+npc2.npcId+"\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			StringUtil.append(replyMSG, "</center>");
			StringUtil.append(replyMSG, "</body></html>");
		}
		else if (lvl1 != 0 && npc1 == null)
		{
			StringUtil.append(replyMSG, "<html><title>Select first mob to fight</title>");
			StringUtil.append(replyMSG, "<body><table>");
			L2NpcTemplate[] npcs = NpcTable.getInstance().getAllOfLevel(lvl1);
			for (L2NpcTemplate n : npcs)
			{
				StringUtil.append(replyMSG, "<tr><td><a action=\"bypass -h admin_fight_calculator lvl1 "+lvl1+" lvl2 "+lvl2+" mid1 "+n.npcId+" mid2 "+mid2+"\">"+n.name+"</a></td></tr>");
			}
			StringUtil.append(replyMSG, "</table></body></html>");
		}
		else if (lvl2 != 0 && npc2 == null)
		{
			StringUtil.append(replyMSG, "<html><title>Select second mob to fight</title>");
			StringUtil.append(replyMSG, "<body><table>");
			L2NpcTemplate[] npcs = NpcTable.getInstance().getAllOfLevel(lvl2);
			for (L2NpcTemplate n : npcs)
			{
				StringUtil.append(replyMSG, "<tr><td><a action=\"bypass -h admin_fight_calculator lvl1 "+lvl1+" lvl2 "+lvl2+" mid1 "+mid1+" mid2 "+n.npcId+"\">"+n.name+"</a></td></tr>");
			}
			StringUtil.append(replyMSG, "</table></body></html>");
		}
		else
		{
			StringUtil.append(replyMSG, "<html><title>Select mobs to fight</title>");
			StringUtil.append(replyMSG, "<body>");
			StringUtil.append(replyMSG, "<table>");
			StringUtil.append(replyMSG, "<tr><td>First</td><td>Second</td></tr>");
			StringUtil.append(replyMSG, "<tr><td><edit var=\"lvl1\" width=80></td><td><edit var=\"lvl2\" width=80></td></tr>");
			StringUtil.append(replyMSG, "</table>");
			StringUtil.append(replyMSG, "<center><br><br><br>");
			StringUtil.append(replyMSG, "<button value=\"OK\" action=\"bypass -h admin_fight_calculator lvl1 $lvl1 lvl2 $lvl2\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			StringUtil.append(replyMSG, "</center>");
			StringUtil.append(replyMSG, "</body></html>");
		}
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);	
    }

	private void handleShow(String params, L2PcInstance activeChar)
	{
		Formulas f = Formulas.getInstance();
		params = params.trim();

		L2Character npc1 = null;
		L2Character npc2 = null;
		if (params.length() == 0)
		{
			npc1 = activeChar;
			npc2 = (L2Character)activeChar.getTarget();
			if(npc2 == null)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.INCORRECT_TARGET));
				return;
			}
		}
		else
		{
			int mid1 = 0;
			int mid2 = 0;
			StringTokenizer st = new StringTokenizer(params);
			mid1 = Integer.parseInt(st.nextToken());
			mid2 = Integer.parseInt(st.nextToken());

			npc1 = new L2MonsterInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(mid1));
			npc2 = new L2MonsterInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(mid2));
		}

		int miss1 = 0;
		int miss2 = 0;
		int shld1 = 0;
		int shld2 = 0;
		int crit1 = 0;
		int crit2 = 0;
		double patk1 = 0;
		double patk2 = 0;
		double pdef1 = 0;
		double pdef2 = 0;
		double dmg1 = 0;
		double dmg2 = 0;
		
		// ATTACK speed in milliseconds
		int sAtk1 = npc1.calculateTimeBetweenAttacks(npc2, null);
		int sAtk2 = npc2.calculateTimeBetweenAttacks(npc1, null);
		// number of ATTACK per 100 seconds
		sAtk1 = 100000 / sAtk1; 
		sAtk2 = 100000 / sAtk2; 
		
		for (int i=0; i < 10000; i++)
		{
			boolean _miss1 = f.calcHitMiss(npc1, npc2);
			if (_miss1) miss1++;
			boolean _shld1 = f.calcShldUse(npc1, npc2);
			if (_shld1) shld1++;
			boolean _crit1 = f.calcCrit(npc1.getCriticalHit(npc2, null));
			if (_crit1) crit1++;
			
			double _patk1 = npc1.getPAtk(npc2);
			_patk1 += Rnd.nextDouble()* npc1.getRandomDamage(npc2);
			patk1 += _patk1;
			
			double _pdef1 = npc1.getPDef(npc2);
			pdef1 += _pdef1;

			if (!_miss1)
			{
				double _dmg1 = f.calcPhysDam(npc1, npc2, null, _shld1, _crit1, false, false);
				dmg1 += _dmg1;
				npc1.abortAttack();
			}
		}

		for (int i=0; i < 10000; i++)
		{
			boolean _miss2 = f.calcHitMiss(npc2, npc1);
			if (_miss2) miss2++;
			boolean _shld2 = f.calcShldUse(npc2, npc1);
			if (_shld2) shld2++;
			boolean _crit2 = f.calcCrit(npc2.getCriticalHit(npc1, null));
			if (_crit2) crit2++;
			
			double _patk2 = npc2.getPAtk(npc1);
			_patk2 += Rnd.nextDouble()* npc2.getRandomDamage(npc1);
			patk2 += _patk2;
			
			double _pdef2 = npc2.getPDef(npc1);
			pdef2 += _pdef2;

			if (!_miss2)
			{
				double _dmg2 = f.calcPhysDam(npc2, npc1, null, _shld2, _crit2, false, false);
				dmg2 += _dmg2;
				npc2.abortAttack();
			}
		}

		miss1 /= 100;
		miss2 /= 100;
		shld1 /= 100;
		shld2 /= 100;
		crit1 /= 100;
		crit2 /= 100;
		patk1 /= 10000;
		patk2 /= 10000;
		pdef1 /= 10000;
		pdef2 /= 10000;
		dmg1  /= 10000;
		dmg2  /= 10000;
		
		// total damage per 100 seconds
		int tdmg1 = (int)(sAtk1 * dmg1);
		int tdmg2 = (int)(sAtk2 * dmg2);
		// HP restored per 100 seconds
		double maxHp1 = npc1.getMaxHp();
		int hp1 = (int)(f.calcHpRegen(npc1) * 100000 / f.getRegeneratePeriod(npc1));

		double maxHp2 = npc2.getMaxHp();
		int hp2 = (int)(f.calcHpRegen(npc2) * 100000 / f.getRegeneratePeriod(npc2));

		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		
		StringBuilder replyMSG = new StringBuilder(500);
		StringUtil.append(replyMSG, "<html><title>Selected mobs to fight</title>");
		StringUtil.append(replyMSG, "<body>");
		StringUtil.append(replyMSG, "<table>");
		if (params.length() == 0)
		{
			StringUtil.append(replyMSG, "<tr><td width=140>Parameter</td><td width=70>me</td><td width=70>target</td></tr>");
		}
		else
		{
			StringUtil.append(replyMSG, "<tr><td width=140>Parameter</td><td width=70>"+((L2NpcTemplate)npc1.getTemplate()).name+
					"</td><td width=70>"+((L2NpcTemplate)npc2.getTemplate()).name+"</td></tr>");
		}
		StringUtil.append(replyMSG, "<tr><td>miss</td><td>"+miss1+"%</td><td>"+miss2+"%</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>shld</td><td>"+shld2+"%</td><td>"+shld1+"%</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>crit</td><td>"+crit1+"%</td><td>"+crit2+"%</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>pAtk / pDef</td><td>"+((int)patk1)+" / "+((int)pdef1)+"</td><td>"+((int)patk2)+" / "+((int)pdef2)+"</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>made hits</td><td>"+sAtk1+"</td><td>"+sAtk2+"</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>dmg per hit</td><td>"+((int)dmg1)+"</td><td>"+((int)dmg2)+"</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>got dmg</td><td>"+tdmg2+"</td><td>"+tdmg1+"</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>got regen</td><td>"+hp1+"</td><td>"+hp2+"</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>had HP</td><td>"+(int)maxHp1+"</td><td>"+(int)maxHp2+"</td></tr>");
		StringUtil.append(replyMSG, "<tr><td>die</td>");
		if (tdmg2 - hp1 > 1)
			StringUtil.append(replyMSG, "<td>"+(int)(100*maxHp1/(tdmg2 - hp1))+" sec</td>");
		else
			StringUtil.append(replyMSG, "<td>never</td>");
		if (tdmg1 - hp2 > 1)
			StringUtil.append(replyMSG, "<td>"+(int)(100*maxHp2/(tdmg1 - hp2))+" sec</td>");
		else
			StringUtil.append(replyMSG, "<td>never</td>");
		StringUtil.append(replyMSG, "</tr>");
		StringUtil.append(replyMSG, "</table>");
		StringUtil.append(replyMSG, "<center><br>");
		if (params.length() == 0)
		{
			StringUtil.append(replyMSG, "<button value=\"Retry\" action=\"bypass -h admin_fight_calculator_show\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		}
		else
		{
			StringUtil.append(replyMSG, "<button value=\"Retry\" action=\"bypass -h admin_fight_calculator_show "+((L2NpcTemplate)npc1.getTemplate()).npcId+" "+((L2NpcTemplate)npc2.getTemplate()).npcId+"\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		}
		StringUtil.append(replyMSG, "</center>");
		StringUtil.append(replyMSG, "</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);

		if (params.length() != 0)
		{
			((L2MonsterInstance)npc1).deleteMe();
			((L2MonsterInstance)npc2).deleteMe();
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}