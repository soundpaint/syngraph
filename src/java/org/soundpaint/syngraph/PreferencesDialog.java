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
 * $Id: PreferencesDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

public class PreferencesDialog extends JDialog
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private Main synGraph;
    private JCheckBox toolBarCheck;
    private JCheckBox toolTipsCheck;
    private JList lafList;
    private JTextField authorField;

    private PreferencesDialog () {}

    PreferencesDialog (Frame owner, Main synGraph)
    {
	super (owner);
	this.synGraph = synGraph;
	setModal (true);
	setTitle (bundle.getString ("frame.title.preferences"));
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));

	// GUI preferences
	getContentPane ().add (new ToolBarCheckPane ());
	getContentPane ().add (new ToolTipsCheckPane ());
	getContentPane ().add (new LookAndFeelPane ());

	// document preferences
	getContentPane ().add (new DefaultDocumentAuthorPane ());

	getContentPane ().add (new ButtonRow ());
	loadInputFields ();
	pack ();
    }

    private class ToolBarCheckPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	ToolBarCheckPane ()
	{
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.toolBar")));
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.toolBarCheck"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.toolBarCheck"));
	    toolBarCheck = new JCheckBox ();
	    label.setLabelFor (toolBarCheck);
	    toolBarCheck.
		setToolTipText (bundle.
				getString ("checkbox.tooltip.toolBarCheck"));
	    labelPane.add (toolBarCheck);
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	}
    }

    private class ToolTipsCheckPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	ToolTipsCheckPane ()
	{
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.toolTips")));
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.toolTipsCheck"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.toolTipsCheck"));
	    toolTipsCheck = new JCheckBox ();
	    label.setLabelFor (toolTipsCheck);
	    toolTipsCheck.
		setToolTipText (bundle.
				getString ("checkbox.tooltip.toolTipsCheck"));
	    labelPane.add (toolTipsCheck);
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	}
    }

    private class LookAndFeelPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	private JList createLafList ()
	{
	    UIManager.LookAndFeelInfo[] lafInfo =
		UIManager.getInstalledLookAndFeels();
	    String lafName[] = new String[lafInfo.length];
	    for (int i = 0; i < lafInfo.length; i++)
		{
		    lafName[i] = lafInfo[i].getName ().intern ();
		}
	    return new JList (lafName);
	}

	// TODO: Maybe add a Look&Feel "Preview" button?
	LookAndFeelPane ()
	{
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.lookAndFeel")));
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.lookAndFeel"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.lookAndFeel"));
	    //labelPane.add (label);
	    add (labelPane);
	    lafList = createLafList ();
	    lafList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
	    label.setLabelFor (lafList);
	    lafList.
		setToolTipText (bundle.
				getString ("list.tooltip.lookAndFeel"));
	    labelPane.add (lafList);
	    labelPane.add (Box.createHorizontalGlue ());
	}
    }

    private class DefaultDocumentAuthorPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	DefaultDocumentAuthorPane ()
	{
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.defaultDocumentAuthor")));
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.defaultDocumentAuthor"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.defaultDocumentAuthor"));
	    labelPane.add (label);
	    authorField = new JTextField ();
	    label.setLabelFor (authorField);
	    authorField.
		setToolTipText (bundle.
				getString ("textfield.tooltip.defaultDocumentAuthor"));
	    //authorField.setMaximumSize (new java.awt.Dimension (500,100));
	    labelPane.add (authorField);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    add (Box.createVerticalGlue ());
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
				getString ("button.tooltip.cancelPreferences"));
	    buttonCancel.addActionListener (new ButtonCancelListener ());
	    add (buttonCancel);
	    add (Box.createHorizontalGlue ());
	    JButton buttonOk =
		new JButton (bundle.getString ("button.text.approve"));
	    buttonOk.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.approve"));
	    buttonOk.
		setToolTipText (bundle.
				getString ("button.tooltip.approvePreferences"));
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
	    saveInputFields ();
	    setVisible (false);
	}
    }

    private void saveInputFields ()
    {
	// TODO: Maybe check backing store availability by calling
	// "preferences.flush ()"?
	Preferences preferences = synGraph.getPreferences ();
	boolean toolBar = toolBarCheck.isSelected ();
	preferences.setToolBar (toolBar);
	AppFrame appFrame = GUI.getInstance ().getAppFrame ();
	appFrame.setToolBarVisible (toolBar);
	boolean toolTips = toolTipsCheck.isSelected ();
	preferences.setToolTips (toolTips);
	ToolTipManager.sharedInstance ().setEnabled (toolTips);
	String laf = (String)lafList.getSelectedValue ();
	preferences.setLaf (laf);
	appFrame.setLookAndFeelByName (laf);
	String author = authorField.getText ();
	preferences.setAuthor (author);
    }

    private void loadInputFields ()
    {
	Preferences preferences = synGraph.getPreferences ();
	AppFrame appFrame = GUI.getInstance ().getAppFrame ();
	toolBarCheck.setSelected (appFrame.isToolBarVisible ());
	toolTipsCheck.setSelected (ToolTipManager.sharedInstance ().
				   isEnabled ());
	lafList.setSelectedValue (UIManager.getLookAndFeel ().
				  getName ().intern (), true);
	authorField.setText (preferences.getAuthor ());
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
