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

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * the HTML parser in the client knowns these standard and non-standard tags and attributes VOLUMN UNKNOWN UL U TT TR TITLE TEXTCODE TEXTAREA TD TABLE SUP SUB STRIKE SPIN SELECT RIGHT PRE P OPTION OL MULTIEDIT LI LEFT INPUT IMG I HTML H7 H6 H5 H4 H3 H2 H1 FONT EXTEND EDIT COMMENT COMBOBOX CENTER
 * BUTTON BR BODY BAR ADDRESS A SEL LIST VAR FORE READONL ROWS VALIGN FIXWIDTH BORDERCOLORLI BORDERCOLORDA BORDERCOLOR BORDER BGCOLOR BACKGROUND ALIGN VALU READONLY MULTIPLE SELECTED TYP TYPE MAXLENGTH CHECKED SRC Y X QUERYDELAY NOSCROLLBAR IMGSRC B FG SIZE FACE COLOR DEFFON DEFFIXEDFONT WIDTH VALUE
 * TOOLTIP NAME MIN MAX HEIGHT DISABLED ALIGN MSG LINK HREF ACTION
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class NpcHtmlMessage extends L2GameServerPacket
{
	// d S
	// d is usually 0, S is the html text starting with <html> and ending with </html>
	//
	private static final String _S__1B_NPCHTMLMESSAGE = "[S] 0f NpcHtmlMessage";
	private static Logger _log = Logger.getLogger(NpcHtmlMessage.class.getName());
	private final int _npcObjId;
	private String _html;
	
	/**
	 * @param npcObjId 
	 * @param text 
	 */
	public NpcHtmlMessage(int npcObjId, String text)
	{
		_npcObjId = npcObjId;
		setHtml(text);
	}
	
	public NpcHtmlMessage(int npcObjId)
	{
		_npcObjId = npcObjId;
	}
	
	@Override
	public void runImpl()
	{
		if (Config.BYPASS_VALIDATION)
		{
			buildBypassCache();
		}
	}
	
	public void setHtml(String text)
	{
		if (text.length() > 8192)
		{
			_log.warning("Html is too long! This will crash the client!");
			_html = "<html><body>Html was too long!</body></html>";
			return;
		}
		_html = text; // HTML code must not exceed 8192 bytes
	}
	
	public boolean setFile(String path)
	{
		String content = HtmCache.getInstance().getHtm(path);
		if (content == null)
		{
			setHtml("<html><body>My Text is missing:<br>" + path + "</body></html>");
			_log.warning("missing html page " + path);
			return false;
		}
		
		setHtml(content);
		
		return true;
	}
	
	public void replace(String pattern, String value)
	{
		_html = _html.replaceAll(pattern, value.replaceAll("\\$", "\\\\\\$"));
	}
	
	private final void buildBypassCache()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		activeChar.clearBypass();
		
		int len = _html.length();
		for (int i = 0; i < len; i++)
		{
			int start = _html.indexOf("\"bypass ", i);
			int finish = _html.indexOf("\"", start + 1);
			
			if (start < 0 || finish < 0)
			{
				break;
			}
			
			start += _html.substring(start + 8, start + 10).equals("-h") ? 11 : 8;
			i = finish;
			int finish2 = _html.indexOf("$", start);
			if (finish2 < finish && finish2 > 0)
			{
				activeChar.addBypass2(_html.substring(start, finish2).trim());
			}
			else
			{
				activeChar.addBypass(_html.substring(start, finish).trim());
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0f);
		
		writeD(_npcObjId);
		writeS(_html);
		writeD(0x00);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__1B_NPCHTMLMESSAGE;
	}
}