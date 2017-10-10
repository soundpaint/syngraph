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
 * $Date: 2008-01-04 01:11:51 +0100 (Fri, 04 Jan 2008) $
 * $Id: AppFrame.java 310 2008-01-04 00:11:51Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

/**
 * The application main window.
 *
 * @author Jürgen Reuter
 */
class AppFrame extends JFrame implements DocumentInfoChangeListener
{
    private final static String FRAME_TITLE_SPLASH = "Splash Frame";
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    private final static UIManager.LookAndFeelInfo[] lafInfo =
	UIManager.getInstalledLookAndFeels();

    private MenuBar menuBar;
    private JPanel toolBarLinePanel;
    private ImageIcon modifiedMark;
    private JLabel modifiedLabel;
    private JLabel statusLine;
    private ObjectTabbedPane objectTabbedPane;
    private Image imageUnmodified, imageModified;

    public void setLookAndFeelByName (String name)
    {
	if ((name == null) || (name.length () == 0))
	    // no L&F to set
	    return;
	String className = null;
	for (int i = 0; i < lafInfo.length; i++)
	    {
		if (lafInfo[i].getName ().equals (name))
		    {
			className = lafInfo[i].getClassName ();
			break;
		    }
	    }
	if (className == null)
	    {
		String message = bundle.getString ("label.text.invalidLaf");
		String title = bundle.getString ("frame.title.errorLaf");
		JOptionPane.showMessageDialog (null, message, title,
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	try
	    {
		UIManager.setLookAndFeel (className);
		for (Frame frame : Frame.getFrames ())
		    {
			String title = frame.getTitle ();
			if ((title != null) &&
			    title.equals (FRAME_TITLE_SPLASH))
			    /*
			     * Don't try to update the splash frame or
			     * window, because they will eventually be
			     * destroyed by a concurrent thread.
			     */
			    continue;

			SwingUtilities.updateComponentTreeUI (frame);
			for (Window window : frame.getOwnedWindows ())
			    SwingUtilities.updateComponentTreeUI (window);
		    }
	    }
	catch (Exception e)
	    {
		// FIXME: Splash screen may hide the message forever.
		String message = e.getLocalizedMessage ();
		if (message == null)
		    message = e.getMessage ();
		if (message == null)
		    message = e.toString ();
		String title = bundle.getString ("frame.title.errorLaf");
		JOptionPane.showMessageDialog (this, message, title,
					       JOptionPane.ERROR_MESSAGE);
	    }
    }

    private AppFrame () {}

    AppFrame (Main synGraph)
    {
	super (bundle.getString ("label.text.programTitle"));
	JFrame.setDefaultLookAndFeelDecorated (true);
	imageUnmodified = ResourceBundle.loadImage ("/media/unmodified.png", synGraph);
	imageModified = ResourceBundle.loadImage ("/media/modified.png", synGraph);
	setIconImage (ResourceBundle.loadImage ("/media/syngraph.png", synGraph));
	menuBar = new MenuBar (synGraph.getDocumentInfo ());
	synGraph.addDocumentInfoChangeListener (menuBar);
	synGraph.addDocumentChangeListener (menuBar);
	setJMenuBar (menuBar);
	addWindowListener (new WindowListener ());
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	/*
	 * Put tool bar into a JPanel of its own such that when
	 * drag&dropping the tool bar on this frame, it always will
	 * end up in one and the same location right-ragged beneath
	 * the menu bar (rather than being put as last component in a
	 * container with many other components).
	 */
	JPanel toolBarPanel = new JPanel ();
	toolBarPanel.setLayout (new BoxLayout (toolBarPanel,
					       BoxLayout.Y_AXIS));
	ToolBar toolBar = new ToolBar (synGraph);
	synGraph.addDocumentInfoChangeListener (toolBar);
	toolBarPanel.add (toolBar);
	toolBarLinePanel = new JPanel ();
	toolBarLinePanel.setLayout (new BoxLayout (toolBarLinePanel,
						   BoxLayout.X_AXIS));
	toolBarLinePanel.add (toolBarPanel);
	toolBarLinePanel.add (Box.createHorizontalGlue ());
	getContentPane ().add (toolBarLinePanel);
	objectTabbedPane = new ObjectTabbedPane (synGraph);
	/*
	 * Add this as DocumentChangeListener only *after*
	 * objectTabbedPane has been initialized (and thus added),
	 * because when the font changes, packing of AppFrame must be
	 * performed, after all tabs have their objects updated.
	 */
	synGraph.addDocumentInfoChangeListener (this);
	getContentPane ().add (objectTabbedPane);
	statusLine = new JLabel ();
	statusLine.setBorder (new BevelBorder (BevelBorder.LOWERED));
	modifiedMark = new ImageIcon ();
	JPanel statusLinePanel = new JPanel ();
	statusLinePanel.setLayout (new BoxLayout (statusLinePanel,
						  BoxLayout.X_AXIS));
	modifiedLabel = new JLabel ();
	modifiedLabel.setBorder (new BevelBorder (BevelBorder.LOWERED));
	modifiedLabel.setIcon (modifiedMark);
	statusLinePanel.add (modifiedLabel);
	statusLinePanel.add (Box.createHorizontalGlue ());
	statusLinePanel.add (statusLine);
	getContentPane ().add (statusLinePanel);
	logLine (bundle.getString ("label.text.appFrameUsage"));
    }

    public boolean isToolBarVisible ()
    {
	return toolBarLinePanel.isVisible ();
    }

    public void setToolBarVisible (boolean visible)
    {
	toolBarLinePanel.setVisible (visible);
    }

    private String visuallyTrimPath (String path)
    {
	// TODO: shorten path (e.g. "/home/reuter/.../untitled.syg"),
	// if full path is too long to be displayed in the application
	// frame's title bar.
	return path;
    }

    public void pathChanged (String path)
    {
	String programID =
	    Main.getProgramName () + " " + Main.getProgramVersion ();
	if (path != null)
	    {
		setTitle (programID + " - " + visuallyTrimPath (path));
	    }
	else
	    {
		setTitle (programID);
	    }
    }

    public void haveDocumentChanged (boolean haveDocument)
    {
	statusLine.setVisible (haveDocument);
    }

    public void hasUnsavedChangesChanged (boolean hasUnsavedChanges)
    {
	Image image = hasUnsavedChanges ? imageModified : imageUnmodified;
	if (image == null) // no icon available
	    return;
	modifiedMark.setImage (image);
	modifiedLabel.repaint ();
	modifiedLabel.
	    setToolTipText (bundle.
			    getString ("label.text.modified" +
				       (hasUnsavedChanges ? "True" : "False")));
    }

    public void logLine (String message)
    {
	statusLine.setText (message);
	statusLine.repaint ();
    }

    public void logClear ()
    {
	statusLine.setText ("");
	statusLine.repaint ();
    }

    private class WindowListener extends WindowAdapter
    {
	public void windowClosed (WindowEvent e)
	{
	    System.exit (0);
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
