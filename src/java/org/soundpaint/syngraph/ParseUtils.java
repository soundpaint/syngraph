/*
 * SynGraph -- A tool for documenting graphemic synaesthesia.
 * (C) 2006, 2007, 2008 by
 * Jürgen Reuter <http://www.juergen-reuter.de/>
 *
 * Project Website: http://www.soundpaint.org/syngraph/
 * Jürgen Reuter, Rheinstr. 86, 76185 Karlsruhe, Germany.
 *
 * This file is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * $Author: reuter $
 * $Date: 2008-01-03 08:28:53 +0100 (Thu, 03 Jan 2008) $
 * $Id: ParseUtils.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Properties;

/**
 * A collection of static methods for parsing and dumping various data
 * types.
 *
 * @author Jürgen Reuter
 */
class ParseUtils
{
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    static void dumpBoolean (Properties props, String name, boolean value)
    {
	props.setProperty (name, "" + value);
    }

    static void dumpFont (Properties props, String name, Font font)
    {
	props.setProperty (name + ".name", "" + font.getFontName ());
	props.setProperty (name + ".style", "" + font.getStyle ());
	props.setProperty (name + ".size", "" + font.getSize ());
    }

    private static char int2Hex1 (int value)
	throws IOException
    {
	if ((value < 0) || (value > 0xf))
	    throw new IOException ();
	return
	    (char)((value < 0xa) ? '0' + value : 'a' + (value - 0xa));
    }

    private static String int2Hex2 (int value)
	throws IOException
    {
	if ((value < 0) || (value > 0xff))
	    throw new IOException ();
	return
	    "" + int2Hex1 (value >>> 4) + int2Hex1 (value & 0xf);
    }

    static String color2String (Color color) throws IOException
    {
	return
	    "#" +
	    int2Hex2 (color.getRed ()) +
	    int2Hex2 (color.getGreen ()) +
	    int2Hex2 (color.getBlue ());
    }

    private static int parseHex1 (String hex) throws IOException
    {
	if (hex.length () != 1)
	    throw new IOException ("file corrupted");
	char hexChar = hex.charAt (0);
	if ((hexChar >= '0') && (hexChar <= '9'))
	    return hexChar - '0';
	else if ((hexChar >= 'a') && (hexChar <= 'f'))
	    return 10 + hexChar - 'a';
	else if ((hexChar >= 'A') && (hexChar <= 'F'))
	    return 10 + hexChar - 'A';
	else
	    throw new IOException ("file corrupted");
    }

    private static int parseHex2 (String hex) throws IOException
    {
	if (hex.length () != 2)
	    throw new IOException ("file corrupted");
	return
	    parseHex1 (hex.substring (0, 1)) << 4 |
	    parseHex1 (hex.substring (1, 2));
    }

    static Color parseColor (String colorStr) throws IOException
    {
	if (!colorStr.startsWith ("#"))
	    throw new IOException ("file corrupted");
	if (colorStr.length () != 7)
	    throw new IOException ("file corrupted");
	int red = parseHex2 (colorStr.substring (1, 3));
	int green = parseHex2 (colorStr.substring (3, 5));
	int blue = parseHex2 (colorStr.substring (5, 7));
	if ((red < 0) || (red > 255) ||
	    (green < 0) || (green > 255) ||
	    (blue < 0) || (blue > 255))
	    throw new IOException ("file corrupted");
	return new Color (red, green, blue);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
