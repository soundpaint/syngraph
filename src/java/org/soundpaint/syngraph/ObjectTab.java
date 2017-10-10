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
 * $Id: ObjectTab.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

/**
 * The ObjectTab supports arbitrary graphical objects and places them
 * using a ProportionalLayout manager.
 */
class ObjectTab extends JPanel implements DocumentChangeListener
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    protected final static Cursor POINTER =
	Cursor.getPredefinedCursor (Cursor.HAND_CURSOR);

    protected Main synGraph;
    private JPanel objectPanel;
    private ContextMenu<String> contextMenu;
    private String tabID, label, comment;
    private HashMap<String, JLabel> labels;
    private HashMap<String, ObjectTabListener> listeners;
    private JPanel panel;

    protected ObjectTab (Main synGraph, String tabID)
    {
	this.synGraph = synGraph;
	this.tabID = tabID;
	label = null;
	comment = null;
	labels = new HashMap<String, JLabel> ();
	listeners = new HashMap<String, ObjectTabListener> ();

	synGraph.addDocumentChangeListener (this);
	setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	objectPanel = createPanel ();
	add (objectPanel);
	add (Box.createVerticalGlue ());
	JPanel buttonRow = new JPanel ();
	buttonRow.setLayout (new BoxLayout (buttonRow, BoxLayout.X_AXIS));
	JButton printButton =
	    new JButton (bundle.getString ("button.text.print"));
	printButton.
	    setMnemonic (bundle.getKeyEvent ("button.mnemonic.print"));
	printButton.
	    setToolTipText (bundle.getString ("button.tooltip.printTab"));
	printButton.addActionListener (new PrintButtonListener ());
	buttonRow.add (printButton);
	buttonRow.add (Box.createHorizontalGlue ());
	JButton exportButton =
	    new JButton (bundle.getString ("button.text.export"));
	exportButton.
	    setMnemonic (bundle.getKeyEvent ("button.mnemonic.export"));
	exportButton.
	    setToolTipText (bundle.getString ("button.tooltip.exportTab"));
	exportButton.addActionListener (new ExportButtonListener ());
	buttonRow.add (exportButton);
	buttonRow.add (Box.createHorizontalGlue ());
	JButton infoButton =
	    new JButton (bundle.getString ("button.text.info"));
	infoButton.
	    setMnemonic (bundle.getKeyEvent ("button.mnemonic.info"));
	infoButton.
	    setToolTipText (bundle.getString ("button.tooltip.tabInfo"));
	infoButton.addActionListener (new InfoButtonListener ());
	buttonRow.add (infoButton);
	add (buttonRow);
	contextMenu = new ContextMenu<String> ();
	JMenuItem itemChangeColor =
	    new JMenuItem (bundle.getString ("label.text.changeColor"));
	itemChangeColor.
	    setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemChangeColor"));
	itemChangeColor.addActionListener (new ChangeColorListener ());
	contextMenu.add (itemChangeColor);
	JMenuItem itemChangeComment =
	    new JMenuItem (bundle.getString ("label.text.changeComment"));
	itemChangeComment.
	    setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemChangeComment"));
	itemChangeComment.addActionListener (new ChangeCommentListener ());
	contextMenu.add (itemChangeComment);
    }

    private class ChangeColorListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    String objectID = contextMenu.getContext ();
	    JLabel label = labels.get (objectID);
	    Color newColor =
		GUI.getInstance ().getObjColorChooser ().
		showDialog (bundle.getString ("frame.title.colorChooser",
					      label.getText ()),
			    label.getText (),
			    label.getFont (),
			    label.getForeground ());
	    if (newColor != null)
		synGraph.getDocument ().setObjectColor (objectID, newColor);
	}
    }

    private class ChangeCommentListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    String objectID = contextMenu.getContext ();
	    GUI.getInstance ().getObjectPropertiesDialog ().
		showDialog (objectID);
	}
    }

    private JPanel createPanel ()
    {
	if (panel == null)
	    {
		panel = new JPanel ();
		panel.setLayout (new ProportionalLayout (panel));
	    }
	return panel;
    }

    public void titleChanged (String title) {}
    public void authorChanged (String author) {}
    public void commentChanged (String comment) {}

    public void objectLabelChanged (String objectID)
    {
	JLabel jLabel = labels.get (objectID);
	if (jLabel != null)
	    {
		Document document = synGraph.getDocument ();
		jLabel.setText ((document != null) ?
				document.getObjectLabel (objectID) : "");
	    }
    }

    public void objectCommentChanged (String objectID)
    {
	JLabel jLabel = labels.get (objectID);
	if (jLabel != null)
	    {
		Document document = synGraph.getDocument ();
		jLabel.setToolTipText ((document != null) ?
				       document.getObjectComment (objectID) : "");
	    }
    }

    public void objectColorChanged (String objectID, Color color)
    {
	JLabel jLabel = labels.get (objectID);
	if (jLabel != null)
	    jLabel.setForeground (color);
    }

    public void bgColorChanged (Color color)
    {
	objectPanel.setBackground (color);
    }

    public void fontChanged (Font font)
    {
	for (JLabel label : labels.values ())
	    {
		label.setFont (font);
	    }
    }

    public void tabAdded (String tabID) {}
    public void tabRemoved (String tabID) {}
    public void tabLabelChanged (String tabID) {}

    public void tabCommentChanged (String tabID)
    {
	if (!this.tabID.equals (tabID))
	    return;
	Document document = synGraph.getDocument ();
	String comment =
	    (document != null) ? document.getTabComment (tabID) : null;
	this.comment = comment;
    }

    public void tabObjectAlignChanged (String tabID, String objectID,
				       float x, float y)
    {
	if (!this.tabID.equals (tabID))
	    return;
	JLabel label = labels.get (objectID);
	if ((x < 0.0f) || (y < 0.0f)) // remove label from tab
	    {
		if (label != null)
		    {
			labels.remove (objectID);
			panel.remove (label);
		    }
		return;
	    }
	if (label == null)
	    {
		Document document = synGraph.getDocument ();
		label = new JLabel ((document != null) ? document.getObjectLabel (objectID) : "");
		label.setCursor (POINTER);
		label.addMouseListener (new ObjectTabListener (objectID));
		labels.put (objectID, label);
		panel.add (label);
	    }
	label.setAlignmentX (x);
	label.setAlignmentY (y);
    }

    public void selectedTabChanged (String tabID) {}
    public void functionAdded (String functionID) {}
    public void functionRemoved (String functionID) {}
    public void functionLabelChanged (String functionID) {}
    public void functionCommentChanged (String functionID) {}
    public void setAdded (String setID) {}
    public void setRemoved (String setID) {}
    public void setLabelChanged (String setID) {}
    public void setCommentChanged (String setID) {}

    public String getTabID ()
    {
	return tabID;
    }

    private String getLabel ()
    {
	return (label != null) ? label : tabID;
    }

    private String getInfo ()
    {
	return (comment != null) ?
	    comment : bundle.getString ("label.text.noTabInfo");
    }

    private class ContextMenu<T> extends JPopupMenu
    {
	private final static long serialVersionUID = 0000000000000000000L;

	private T context;

	public void setContext (T context)
	{
	    this.context = context;
	}

	public T getContext () { return context; }
    }

    private class ObjectTabListener extends MouseAdapter
    {
	private String objectID;

	private ObjectTabListener () {}

	ObjectTabListener (String objectID)
	{
	    this.objectID = objectID;
	}

	public void mousePressed(MouseEvent event)
	{
	    if (event.isPopupTrigger ())
		{
		    contextMenu.setContext (objectID);
		    // context menu temporarily disabled since
		    // properties dialog is partially broken.
		    /*
		    contextMenu.show (event.getComponent (),
				      event.getX (), event.getY ());
		    */
		}
	    else
		{
		    JLabel label = (JLabel)event.getSource ();
		    Color newColor =
			GUI.getInstance ().getObjColorChooser ().
			showDialog(bundle.getString ("frame.title.colorChooser",
						     label.getText ()),
				   label.getText (),
				   label.getFont (),
				   label.getForeground ());
		    if (newColor != null)
			synGraph.getDocument ().setObjectColor (objectID, newColor);
		}
	}
    }

    private class PrintButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getObjTabPrintDialog ().
		showDialog (objectPanel);
	}
    }

    private class ExportButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    try
		{
		    GUI.getInstance ().getObjTabExportDialog ().
			showDialog (objectPanel);
		}
	    catch (UnsupportedOperationException e)
		{
		    JOptionPane.
			showMessageDialog (ObjectTab.this,
					   bundle.getString ("error.imgnodriver"),
					   bundle.getString ("frame.title.errorImgWrite"),
					   JOptionPane.ERROR_MESSAGE);
		}
	}
    }

    private class InfoButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    JOptionPane.
		showMessageDialog (ObjectTab.this,
				   getInfo (),
				   bundle.getString ("frame.title.info"),
				   JOptionPane.INFORMATION_MESSAGE);
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
