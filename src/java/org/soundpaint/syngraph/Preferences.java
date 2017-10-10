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
 * $Id: Preferences.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.util.prefs.BackingStoreException;
import java.awt.Rectangle;

public class Preferences
{
    private final static char SEPARATOR_CHAR = ',';
    private final static String OPEN_FILTER_DESCRIPTION = "openFilterDescription";
    private final static String SAVE_FILTER_DESCRIPTION = "saveFilterDescription";
    private final static String DOCUMENT_PATH = "documentPath";
    private final static String AUTHOR = "author";
    private final static String TOOL_BAR = "toolBar";
    private final static String TOOL_TIPS = "toolTips";
    private final static String LAF = "laf";

    private static Preferences defaultPreferences = null;

    private java.util.prefs.Preferences prefs;

    public static synchronized Preferences getDefault ()
    {
	if (defaultPreferences == null)
	    {
		defaultPreferences = new Preferences ();
	    }
	return defaultPreferences;
    }

    private Preferences ()
    {
	prefs = java.util.prefs.Preferences.userNodeForPackage (getClass ());
    }

    public String getOpenFilterDescription ()
    {
	return prefs.get (OPEN_FILTER_DESCRIPTION, "");
    }

    public void setOpenFilterDescription (String filterDescription)
    {
	prefs.put (OPEN_FILTER_DESCRIPTION, filterDescription);
    }

    public String getSaveFilterDescription ()
    {
	return prefs.get (SAVE_FILTER_DESCRIPTION, "");
    }

    public void setSaveFilterDescription (String filterDescription)
    {
	prefs.put (SAVE_FILTER_DESCRIPTION, filterDescription);
    }

    public String getDocumentPath ()
    {
	return prefs.get (DOCUMENT_PATH, "");
    }

    public void setDocumentPath (String path)
    {
	prefs.put (DOCUMENT_PATH, path);
    }

    public String getAuthor ()
    {
	return prefs.get (AUTHOR, System.getProperty ("user.name"));
    }

    public void setAuthor (String author)
    {
	prefs.put (AUTHOR, author);
    }

    public boolean getToolBar ()
    {
	return prefs.getBoolean (TOOL_BAR, true);
    }

    public void setToolBar (boolean toolBar)
    {
	prefs.putBoolean (TOOL_BAR, toolBar);
    }

    public boolean getToolTips ()
    {
	return prefs.getBoolean (TOOL_TIPS, true);
    }

    public void setToolTips (boolean toolTips)
    {
	prefs.putBoolean (TOOL_TIPS, toolTips);
    }

    public String getLaf ()
    {
	return prefs.get (LAF, "");
    }

    public void setLaf (String laf)
    {
	prefs.put (LAF, laf);
    }

    private int[] parseIntList (String s, int length)
    {
	assert length >= 0;
	if (s == null)
	    return null;
	int list[] = new int[length];
	int segmentStart = 0, segmentEnd = 0;
	for (int i = 0; i < length; i++)
	    {
		segmentEnd = s.indexOf (SEPARATOR_CHAR, segmentStart);
		if ((i < length - 1) && (segmentEnd < 0))
		    // less integers in list than expected
		    return null;
		String segment =
		    (segmentEnd >= 0) ?
		    s.substring (segmentStart, segmentEnd) :
		    s.substring (segmentStart);
		try
		    {
			list[i] = Integer.parseInt (segment);
		    }
		catch (NumberFormatException e)
		    {
			// not a valid integer
			return null;
		    }
		segmentStart = segmentEnd + 1;
	    }
	if (segmentEnd != -1)
	    // more integers than expected
	    return null;
	return list;
    }

    public String getString (String id)
    {
	return prefs.get (id, "");
    }

    public void setString (String id, String s)
    {
	prefs.put (id, s);
    }

    public Rectangle getBounds (String dialogId)
    {
	int ints[] = parseIntList (prefs.get (dialogId + "Bounds", ""), 4);
	if (ints == null) // parse error
	    return null;
	else
	    return new Rectangle (ints[0], ints[1], ints[2], ints[3]);
    }

    public void putBounds (String dialogId, Rectangle r)
    {
	if (r != null)
	    prefs.put (dialogId + "Bounds",
		       "" +
		       r.x + SEPARATOR_CHAR +
		       r.y + SEPARATOR_CHAR +
		       r.width + SEPARATOR_CHAR +
		       r.height);
    }

    public void remove (String key)
    {
	prefs.remove (key);
    }

    public void flush () throws BackingStoreException
    {
	prefs.flush ();
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
