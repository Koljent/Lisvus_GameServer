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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.geoengine.geoeditorcon.GeoEditorListener;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 * @author Luno, Dezmond
 */
public class AdminGeoEditor implements IAdminCommandHandler
{
    private static String[] _adminCommands = 
    {
        "admin_ge_status",
        "admin_ge_mode",
        "admin_ge_join",
        "admin_ge_leave"
    };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar) 
    {
        String target = (activeChar.getTarget() != null) ? activeChar.getTarget().getName() : "no-target";
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");

        if (!Config.ACCEPT_GEOEDITOR_CONN)
        {
            activeChar.sendMessage("Server does not accept geoeditor connections now.");
            return true;
        }

        if (command.startsWith("admin_ge_status"))
            activeChar.sendMessage(GeoEditorListener.getInstance().getStatus());
        else if(command.startsWith("admin_ge_mode"))
        {
            if (GeoEditorListener.getInstance().getThread() == null)
            {
                activeChar.sendMessage("Geoeditor is not connected.");
                return true;
            }

            try
            {
                String val = command.substring("admin_ge_mode".length());
                StringTokenizer st = new StringTokenizer(val);

                if (st.countTokens() < 1)
                {
                    activeChar.sendMessage("Usage: //ge_mode X");
                    activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
                    activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
                    activeChar.sendMessage("Mode 2: Send coordinates each second.");
                    return true;
                }

                int m = Integer.parseInt(st.nextToken());
                GeoEditorListener.getInstance().getThread().setMode(m);
                activeChar.sendMessage("Geoeditor connection mode set to "+m+".");
            }
            catch (Exception e)
            {
                activeChar.sendMessage("Usage: //ge_mode X");
                activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
                activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
                activeChar.sendMessage("Mode 2: Send coordinates each second.");
                e.printStackTrace();
            }
        }
        else if (command.equals("admin_ge_join"))
        {
            if (GeoEditorListener.getInstance().getThread() == null)
            {
                activeChar.sendMessage("Geoeditor is not connected.");
                return true;
            }
            GeoEditorListener.getInstance().getThread().addGM(activeChar);
            activeChar.sendMessage("You have been added to geoeditor's list.");
        }
        else if (command.equals("admin_ge_leave"))
        {
            if (GeoEditorListener.getInstance().getThread() == null)
            {
                activeChar.sendMessage("Geoeditor is not connected.");
                return true;
            }
            GeoEditorListener.getInstance().getThread().removeGM(activeChar);
            activeChar.sendMessage("You have been removed from geoeditor's list.");
        }
        return true;
    }

    @Override
	public String[] getAdminCommandList() 
    {
        return _adminCommands;
    }
}