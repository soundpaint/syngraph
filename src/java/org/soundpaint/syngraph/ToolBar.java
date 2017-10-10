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
 * $Id: ToolBar.java 310 2008-01-04 00:11:51Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.event.ActionListener;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * This class holds the tool bar of the application and related logic.
 *
 * @author Jürgen Reuter
 */
class ToolBar extends JToolBar implements DocumentInfoChangeListener
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private JButton buttonSave;
    private boolean haveDocument, hasUnsavedChanges;

    private ToolBar () {}

    ToolBar (Main synGraph)
    {
	super (bundle.getString ("frame.title.toolbar"));
	GUI gui = GUI.getInstance ();
	add (createButton ("/media/open.png",
			   "button.tooltip.open",
			   "button.text.open",
			   gui.getOpenListener (), synGraph));
	add (buttonSave =
	     createButton ("/media/save.png",
			   "button.tooltip.save",
			   "button.text.save",
			   gui.getSaveListener (), synGraph));
	add (createButton ("/media/exit.png",
			   "button.tooltip.exit",
			   "button.text.exit",
			   gui.getExitListener (), synGraph));
    }

    public void pathChanged (String documentPath) {}

    public void haveDocumentChanged (boolean haveDocument)
    {
	this.haveDocument = haveDocument;
	buttonSave.setEnabled (haveDocument && hasUnsavedChanges);
    }

    public void hasUnsavedChangesChanged (boolean hasUnsavedChanges)
    {
	this.hasUnsavedChanges = hasUnsavedChanges;
	buttonSave.setEnabled (haveDocument && hasUnsavedChanges);
    }

    JButton createButton (String iconPath, String toolTipKey,
			  String altText, ActionListener listener,
			  Main synGraph)
    {
	Image image = ResourceBundle.loadImage (iconPath, synGraph);
	String label = bundle.getString (altText);
	JButton button;
	if (image != null)
	    button = new JButton (new ImageIcon (image));
	else
	    button = new JButton (label);
	button.setToolTipText (bundle.getString (toolTipKey));
	button.addActionListener (listener);
	return button;
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
