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
package net.sf.l2j.mmocore;

import java.nio.BufferOverflowException;

/**
 * @author Forsaiken
 */
public final class NioNetStringBuffer
{
	private final char[] _buf;
	
	private final int _size;
	
	private int _len;
	
	public NioNetStringBuffer(final int size)
	{
		_buf = new char[size];
		_size = size;
		_len = 0;
	}
	
	public final void clear()
	{
		_len = 0;
	}
	
	public final void append(final char c)
	{
		if (_len < _size)
		{
			_buf[_len++] = c;
		}
		else
		{
			throw new BufferOverflowException();
		}
	}
	
	@Override
	public final String toString()
	{
		return new String(_buf, 0, _len);
	}
}