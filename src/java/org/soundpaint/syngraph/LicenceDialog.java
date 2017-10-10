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
 * $Id: LicenceDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.BevelBorder;

/**
 * A dialog that displays the licence text.
 *
 * @author Jürgen Reuter
 */
public class LicenceDialog extends JDialog
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    private final static Dimension PREFERRED_PANE_SIZE =
	new Dimension (500, 350);

    private LicenceDialog () {}

    LicenceDialog (Frame owner, String path) throws IOException
    {
	super (owner);
	setModal (false);
	setTitle (bundle.getString ("frame.title.licence"));
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	URL url = LicenceDialog.class.getResource (path);
	JEditorPane editorPane = new JEditorPane (url);
	editorPane.setEditable (false);
	JScrollPane scroller = new JScrollPane ();
	scroller.setPreferredSize (PREFERRED_PANE_SIZE);
	scroller.setBorder (new BevelBorder (BevelBorder.LOWERED,
					     Color.white, Color.gray));
	JViewport viewport = scroller.getViewport ();
	viewport.add (editorPane);
	viewport.setScrollMode (JViewport.BACKINGSTORE_SCROLL_MODE);
	getContentPane ().add (scroller);
	getContentPane ().add (Box.createVerticalGlue ());
	getContentPane ().add (new ButtonRow ());
	pack ();
    }

    public void showDialog ()
    {
	setVisible (true);
    }

    private class ButtonRow extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;
	public ButtonRow ()
	{
	    add (Box.createHorizontalGlue ());
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JButton buttonOk =
		new JButton (bundle.getString ("button.text.close"));
	    buttonOk.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.close"));
	    buttonOk.
		setToolTipText (bundle.
				getString ("button.tooltip.closeLicence"));
	    buttonOk.addActionListener (new ButtonOkListener ());
	    add (buttonOk);
	    add (Box.createHorizontalGlue ());
	}
    }

    private class ButtonOkListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    setVisible (false);
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
