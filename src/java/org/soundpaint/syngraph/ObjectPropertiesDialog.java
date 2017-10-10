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
 * $Id: ObjectPropertiesDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ObjectPropertiesDialog extends JDialog
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private Main synGraph;
    private I18NPropertyEditor label;
    private I18NPropertyEditor comment;
    private String objectID;

    private ObjectPropertiesDialog () {}

    ObjectPropertiesDialog (Frame owner, Main synGraph)
    {
	super (owner);
	this.synGraph = synGraph;
	setModal (true);
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	label =
	    new I18NPropertyEditor (bundle.getString ("label.text.propertyTitle"),
				    bundle.getKeyEvent ("label.mnemonic.propertyTitle"),
				    bundle.getString ("textfield.tooltip.documentTitle"));
	comment =
	    new I18NPropertyEditor (bundle.getString ("label.text.propertyComment"),
				    bundle.getKeyEvent ("label.mnemonic.propertyComment"),
				    bundle.getString ("textarea.tooltip.documentComment"));
	getContentPane ().add (label);
	getContentPane ().add (comment);
	getContentPane ().add (new ButtonRow ());
    }

    private class ButtonRow extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public ButtonRow ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JButton buttonCancel =
		new JButton (bundle.getString ("button.text.cancel"));
	    buttonCancel.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.cancel"));
	    buttonCancel.
		setToolTipText (bundle.
				getString ("button.tooltip.cancelProperties"));
	    buttonCancel.addActionListener (new ButtonCancelListener ());
	    add (buttonCancel);
	    add (Box.createHorizontalGlue ());
	    JButton buttonOk =
		new JButton (bundle.getString ("button.text.approve"));
	    buttonOk.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.approve"));
	    buttonOk.
		setToolTipText (bundle.
				getString ("button.tooltip.approveProperties"));
	    buttonOk.addActionListener (new ButtonOkListener ());
	    add (buttonOk);
	}
    }

    private class ButtonCancelListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    setVisible (false);
	}
    }

    private class ButtonOkListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    setVisible (false);
	    savePropertyEditors ();
	}
    }

    private void loadPropertyEditors ()
    {
	Document document = synGraph.getDocument ();
	label.clear ();
	comment.clear ();
	for (Locale locale : document.getObjectLabelLocales (objectID))
	    {
		String objectLabel =
		    document.getObjectLabel (objectID, locale);
		label.addValue (objectLabel, locale);
	    }
	for (Locale locale : document.getObjectCommentLocales (objectID))
	    {
		String objectComment =
		    document.getObjectComment (objectID, locale);
		comment.addValue (objectComment, locale);
	    }
    }

    private void savePropertyEditors ()
    {
	Document document = synGraph.getDocument ();
	document.removeObjectLabels (objectID);
	document.removeObjectComments (objectID);
	for (Locale locale : label.getPropertyLocales ())
	    {
		String objectLabel = label.getValue (locale);
		document.setObjectLabel (objectID, locale, objectLabel);
	    }
	for (Locale locale : comment.getPropertyLocales ())
	    {
		String objectComment = comment.getValue (locale);
		document.setObjectComment (objectID, locale, objectComment);
	    }
    }

    void showDialog (String objectID)
    {
	this.objectID = objectID;
	loadPropertyEditors ();
	Document document = synGraph.getDocument ();
	loadPropertyEditors ();
	setTitle (bundle.getString ("frame.title.objectProperties",
				    document.getObjectLabel (objectID)));
	pack ();
	setVisible (true);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
