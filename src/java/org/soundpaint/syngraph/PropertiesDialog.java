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
 * $Date: 2008-01-04 04:19:55 +0100 (Fri, 04 Jan 2008) $
 * $Id: PropertiesDialog.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PropertiesDialog extends JDialog implements DocumentChangeListener
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    private final static Dimension PREFERRED_COMMENT_SIZE =
	new Dimension (280, 120);

    private Main synGraph;
    private JTextField title;
    private JTextField author;
    private JTextArea comment;

    private PropertiesDialog () {}

    PropertiesDialog (Frame owner, Main synGraph)
    {
	super (owner);
	this.synGraph = synGraph;
	synGraph.addDocumentChangeListener (this);
	setModal (true);
	setTitle (bundle.getString ("frame.title.properties"));
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	getContentPane ().add (new TitlePane ());
	getContentPane ().add (new AuthorPane ());
	getContentPane ().add (new CommentPane ());
	getContentPane ().add (new ButtonRow ());
	loadInputFields ();
	pack ();
    }

    private class TitlePane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	TitlePane ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.propertyTitle"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.propertyTitle"));
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    title = new JTextField ();
	    label.setLabelFor (title);
	    title.
		setToolTipText (bundle.
				getString ("textfield.tooltip.documentTitle"));
	    add (title);
	}
    }

    private class AuthorPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	AuthorPane ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.propertyAuthor"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.propertyAuthor"));
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    author = new JTextField ();
	    label.setLabelFor (author);
	    author.
		setToolTipText (bundle.
				getString ("textfield.tooltip.documentAuthor"));
	    author.setText (System.getProperty ("user.name"));
	    add (author);
	}
    }

    private class CommentPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	CommentPane ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.propertyComment"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.propertyComment"));
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    comment = new JTextArea ();
	    label.setLabelFor (comment);
	    comment.
		setToolTipText (bundle.
				getString ("textarea.tooltip.documentComment"));
	    comment.setPreferredSize (PREFERRED_COMMENT_SIZE);
	    add (comment);
	}
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

    public void titleChanged (String titleStr)
    {
	if (titleStr == null) // derive default title from path
	    {
		String path = synGraph.getPreferences ().getDocumentPath ();
		titleStr = (path != null) ?
		    new File (path).getName () : null;
	    }
	if (titleStr != null)
	    title.setText (titleStr);
	else
	    title.setText ("");
    }

    public void authorChanged (String authorStr)
    {
	author.setText (authorStr);
    }

    public void commentChanged (String commentStr)
    {
	comment.setText (commentStr);
    }

    public void objectLabelChanged (String objectID) {}
    public void objectCommentChanged (String objectID) {}
    public void objectColorChanged (String objectID, Color color) {}
    public void bgColorChanged (Color color) {}
    public void fontChanged (Font font) {}
    public void tabAdded (String tabID) {}
    public void tabRemoved (String tabID) {}
    public void tabLabelChanged (String tabID) {}
    public void tabCommentChanged (String tabID) {}
    public void tabObjectAlignChanged (String tabID, String objectID,
				       float x, float y) {}
    public void selectedTabChanged (String tabID) {}
    public void functionAdded (String functionID) {}
    public void functionRemoved (String functionID) {}
    public void functionLabelChanged (String functionID) {}
    public void functionCommentChanged (String functionID) {}
    public void setAdded (String setID) {}
    public void setRemoved (String setID) {}
    public void setLabelChanged (String setID) {}
    public void setCommentChanged (String setID) {}

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
	    saveInputFields ();
	    setVisible (false);
	}
    }

    private void saveInputFields ()
    {
	Document document = synGraph.getDocument ();
	String titleStr = title.getText ();
	document.setTitle ((titleStr.length () > 0) ? titleStr : null);
	String authorStr = author.getText ();
	document.setAuthor ((authorStr.length () > 0) ? authorStr : null);
	String commentStr = comment.getText ();
	document.setComment ((commentStr.length () > 0) ? commentStr : null);
    }

    private void loadInputFields ()
    {
	Document document = synGraph.getDocument ();
	titleChanged (synGraph.getDocument ().getTitle ());
	String authorStr = synGraph.getDocument ().getAuthor ();
	if (authorStr != null)
	    author.setText (authorStr);
	String commentStr = synGraph.getDocument ().getComment ();
	if (commentStr != null)
	    comment.setText (commentStr);
    }

    void showDialog ()
    {
	loadInputFields ();
	setVisible (true);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
