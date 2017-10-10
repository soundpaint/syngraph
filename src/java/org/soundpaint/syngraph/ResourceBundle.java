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
 * $Date: 2008-01-04 02:14:43 +0100 (Fri, 04 Jan 2008) $
 * $Id: ResourceBundle.java 311 2008-01-04 01:14:43Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Locale;
import java.text.MessageFormat;
import javax.swing.JOptionPane;

/**
 * A wrapper around java.util.ResourceBundle with some customized
 * functionality.  In particular, it supports parameterized i18n
 * messages.
 *
 * @author Jürgen Reuter
 */
class ResourceBundle
{
    private final static String LS = System.getProperty ("line.separator");
    private static ResourceBundle instance;

    synchronized static ResourceBundle getBundle ()
    {
	if (instance == null)
	    instance = new ResourceBundle ();
	return instance;
    }

    private java.util.ResourceBundle bundle;

    private ResourceBundle ()
    {
	//Locale.setDefault (Locale.GERMAN);//DEBUG
	//Locale.setDefault (Locale.US);//DEBUG
	/*
	Locale locale = Locale.getDefault ();
	System.out.printf ("Locale: %s_%s_%s%n",
			   locale.getLanguage (),
			   locale.getCountry (),
			   locale.getVariant ()); //DEBUG
	*/
	bundle =
	    java.util.ResourceBundle.
	    getBundle ("org.soundpaint.syngraph.Resources");
    }

    String getString (String key, String ... params)
    {
	String encoded = bundle.getString (key);
	String decoded = expandMacros (encoded);
	return MessageFormat.format (decoded, (Object[])params);
    }

    private static int parseCharLiteral (String literal)
    {
	if (literal.length () < 3)
	    throw new IllegalArgumentException ();
	if (literal.charAt (0) != '\'')
	    throw new IllegalArgumentException ();
	char ch = literal.charAt (1);
	if (ch != '\\')
	    {
		if (literal.charAt (2) != '\'')
		    throw new IllegalArgumentException ();
		if (literal.length () > 3)
		    throw new IllegalArgumentException ();
		return ch;
	    }
	if (literal.charAt (3) != '\'')
	    throw new IllegalArgumentException ();
	if (literal.length () > 4)
	    throw new IllegalArgumentException ();
	ch = literal.charAt (2);
	switch (ch)
	    {
	    case 'n' : return '\n';
	    case 'r' : return '\r';
	    case 't' : return '\t';
	    case '\'' : return '\'';
	    case '\\' : return '\\';
	    default: return ch;
	    }
    }

    private static int parseIntLiteral (String literal)
    {
	if (literal.startsWith ("0x"))
	    return Integer.parseInt (literal.substring (2), 16);
	else
	    return Integer.parseInt (literal);
    }

    int getKeyEvent (String key)
    {
	String keyEventStr = bundle.getString (key).trim ();
	int keyEvent;
	try
	    {
		if (keyEventStr.startsWith ("'"))
		    keyEvent = parseCharLiteral (keyEventStr);
		else
		    keyEvent = parseIntLiteral (keyEventStr);
	    }
	catch (Exception e)
	    {
		keyEvent = KeyEvent.VK_UNDEFINED;
	    }
	return keyEvent;
    }

    private static String expandMacros (String encoded)
    {
	StringBuffer decoded = new StringBuffer ();
	int hashPos1 = encoded.indexOf ('#');
	int hashPos2 = -1;
	while (hashPos1 >= 0)
	    {
		decoded.append (encoded.substring (hashPos2 + 1, hashPos1));
		hashPos2 = encoded.indexOf ('#', hashPos1 + 1);
		if (hashPos2 >= 0)
		    {
			String expandedMacro;
			String macroName =
			    encoded.substring (hashPos1 + 1, hashPos2);
			if (macroName.equals ("PROGRAM_NAME"))
			    expandedMacro = Main.getProgramName ();
			else if (macroName.equals ("PROGRAM_VERSION"))
			    expandedMacro = Main.getProgramVersion ();
			else if (macroName.equals ("n"))
			    expandedMacro = LS;
			else if (macroName.equals (""))
			    expandedMacro = "#"; // "##" -> "#"
			else // unknown macro => do not expand
			    expandedMacro = "#" + macroName + "#";
			decoded.append (expandedMacro);
			hashPos1 = encoded.indexOf ('#', hashPos2 + 1);
		    }
		else
		    {
			// missing closing '#' => leave unexpanded
			hashPos2 = hashPos1 - 1;
			hashPos1 = -1;
		    }
	    }
	decoded.append (encoded.substring (hashPos2 + 1));
	return decoded.toString ();
    }

    /**
     * This is a utility method to help in loading icon images.  It
     * takes the name of a resource file associated with the current
     * object's class file and loads an image object from that file.
     *
     * @param resourceName  A pathname relative to the directory
     *		holding the class file of the current class.  For example,
     *		"wombat.gif".
     * @return  an image object.  May be null if the load failed.
     */
    public static Image loadImage (String resourceName,
				   MessageDisplay display)
    {
	ResourceBundle bundle = getBundle ();
	try
	    {
		URL url = Resources.class.getResource (resourceName);
		Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit ();
		return toolkit.createImage ((ImageProducer)url.getContent ());
	    }
	catch (Exception e)
	    {
		display.showMessage (bundle.getString ("frame.title.errorIO"),
				     bundle.getString ("label.text.errorMissingResource",
						       resourceName),
				     JOptionPane.ERROR_MESSAGE);
		return null;
	    }
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
