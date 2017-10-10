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
 * $Id: Main.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * After the splash window, this class is the main entry point of the
 * application.
 *
 * @author Jürgen Reuter
 */
public class Main implements MessageDisplay
{
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private DocumentInfo documentInfo;
    private Preferences preferences;

    void addDocumentInfoChangeListener (DocumentInfoChangeListener listener)
    {
	documentInfo.addDocumentInfoChangeListener (listener);
    }
    
    void addDocumentChangeListener (DocumentChangeListener listener)
    {
	documentInfo.addDocumentChangeListener (listener);
    }
    
    public static String getProgramName ()
    {
	return "SynGraph";
    }

    public static String getProgramVersion ()
    {
	return "0.2";
    }

    public static String getFileFormatVersion ()
    {
	return "0.2";
    }

    DocumentInfo getDocumentInfo ()
    {
	return documentInfo;
    }

    Document getDocument ()
    {
	return documentInfo.getDocument ();
    }

    Preferences getPreferences ()
    {
	return preferences;
    }

    public void exitApp ()
    {
	GUI.getInstance ().storeWindowBounds ();
	System.exit (0);
    }


    public Main (String argv[])
    {
	if (argv.length > 1)
	    {
		showMessage (bundle.getString ("frame.title.errorBadParams"),
			     bundle.getString ("label.text.errorBadParams"),
			     JOptionPane.ERROR_MESSAGE);
		System.exit (-1);
	    }
	preferences = Preferences.getDefault ();
	documentInfo = new DocumentInfo (preferences.getAuthor ());
	GUI.initInstance (this);
	AppFrame appFrame = GUI.getInstance ().getAppFrame ();
	appFrame.setToolBarVisible (preferences.getToolBar ());
	ToolTipManager.sharedInstance ().
	    setEnabled (preferences.getToolTips ());
	SwingUtilities.invokeLater (
	    new Runnable()
	    {
		public void run ()
		{
		    GUI.getInstance ().getAppFrame ().
			setLookAndFeelByName (preferences.getLaf ());
		}
	    }
	);
	try
	    {
		if (argv.length == 1)
		    {
			documentInfo.loadDocument (argv[0]);
		    }
		else
		    {
			documentInfo.loadDocument ();
		    }
	    }
	catch (IOException e)
	    {
		String message =
		    bundle.getString ("label.text.errorLoad",
				      e.getLocalizedMessage ());
		showMessage (bundle.getString ("frame.title.errorLoad"),
			     message,
			     JOptionPane.ERROR_MESSAGE);
		documentInfo.removeDocument ();
	    }
	appFrame.pack ();
	appFrame.setVisible (true);
    }

    public void showMessage (String title, String message, int type)
    {
	AppFrame appFrame = GUI.getInstance ().getAppFrame ();
	JOptionPane.showMessageDialog (appFrame, message, title, type);
    }

    public static void main (String argv[])
    {
	try
	    {
		new Main (argv);
	    }
	catch (Throwable t)
	    // should not happen, but who knows...
	    {
		/*
		 * Ensure that the AWT thread is killed, such that the
		 * application really exits.
		 */
		t.printStackTrace (System.err);
		System.exit (-1);
	    }
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
