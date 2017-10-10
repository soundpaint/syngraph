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
 * $Id: MenuBar.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This class holds the menu bar of the application and related logic.
 *
 * @author Jürgen Reuter
 */
class MenuBar extends JMenuBar
    implements DocumentChangeListener, DocumentInfoChangeListener
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private DocumentInfo documentInfo;
    private JMenuItem itemSave;
    private JMenuItem itemSaveAs;
    private JMenuItem itemClose;
    private JMenuItem itemProperties;
    private EditMenu editMenu;
    private boolean haveDocument, hasUnsavedChanges;

    private MenuBar () {}

    MenuBar (DocumentInfo documentInfo)
    {
	this.documentInfo = documentInfo;
	add (new FileMenu ());
	add (editMenu = new EditMenu ());
	add (new HelpMenu ());
    }

    public void pathChanged (String documentPath) {}

    public void haveDocumentChanged (boolean haveDocument)
    {
	this.haveDocument = haveDocument;
	itemSave.setEnabled (haveDocument && hasUnsavedChanges);
	itemSaveAs.setEnabled (haveDocument);
	itemClose.setEnabled (haveDocument);
	itemProperties.setEnabled (haveDocument);
	editMenu.setEnabled (haveDocument);
    }

    public void hasUnsavedChangesChanged (boolean hasUnsavedChanges)
    {
	this.hasUnsavedChanges = hasUnsavedChanges;
	itemSave.setEnabled (haveDocument && hasUnsavedChanges);
    }

    public void titleChanged (String title) {}
    public void authorChanged (String author) {}
    public void commentChanged (String comment) {}
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
    public void functionAdded (String functionID)
    {
	Document document = documentInfo.getDocument ();
	if (document != null)
	    {
		String label = document.getFunctionLabel (functionID);
		addCopyItem (label, KeyEvent.VK_UNDEFINED, functionID);
	    }
    }

    public void functionRemoved (String functionID)
    {
	removeCopyItem (functionID);
    }

    public void functionLabelChanged (String functionID) {}
    public void functionCommentChanged (String functionID) {}
    public void setAdded (String setID) {}
    public void setRemoved (String setID) {}
    public void setLabelChanged (String setID) {}
    public void setCommentChanged (String setID) {}

    private void addCopyItem (String label, int keyEvent, String functionID)
    {
	editMenu.addCopyItem (label, keyEvent, functionID);
    }

    private void removeCopyItem (String functionID)
    {
	CopyItem copyItem = editMenu.removeCopyItem (functionID);
	assert copyItem != null;
    }

    private class FileMenu extends JMenu
    {
	private final static long serialVersionUID = 0000000000000000000L;

	FileMenu ()
	{
	    super (bundle.getString ("label.text.menuTitleFile"));
	    GUI gui = GUI.getInstance ();
	    setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuTitleFile"));
	    JMenuItem itemOpen =
		new JMenuItem (bundle.getString ("label.text.menuItemOpen"));
	    itemOpen.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemOpen"));
	    itemOpen.addActionListener (gui.getOpenListener ());
	    add (itemOpen);
	    itemSave =
		new JMenuItem (bundle.getString ("label.text.menuItemSave"));
	    itemSave.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemSave"));
	    itemSave.addActionListener (gui.getSaveListener ());
	    add (itemSave);
	    itemSaveAs =
		new JMenuItem (bundle.getString ("label.text.menuItemSaveAs"));
	    itemSaveAs.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemSaveAs"));
	    itemSaveAs.addActionListener (gui.getSaveAsListener ());
	    add (itemSaveAs);
	    itemClose =
		new JMenuItem (bundle.getString ("label.text.menuItemClose"));
	    itemClose.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemClose"));
	    itemClose.addActionListener (gui.getCloseListener ());
	    add (itemClose);
	    addSeparator();
	    JMenuItem itemPreferences =
		new JMenuItem (bundle.getString ("label.text.menuItemPreferences"));
	    itemPreferences.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemPreferences"));
	    itemPreferences.addActionListener (gui.getPreferencesListener ());
	    add (itemPreferences);
	    itemProperties =
		new JMenuItem (bundle.getString ("label.text.menuItemProperties"));
	    itemProperties.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemProperties"));
	    itemProperties.addActionListener (gui.getPropertiesListener ());
	    add (itemProperties);
	    addSeparator();
	    JMenuItem itemNotice =
		new JMenuItem (bundle.getString ("label.text.menuItemNotice"));
	    itemNotice.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemNotice"));
	    itemNotice.addActionListener (gui.getNoticeListener ());
	    add (itemNotice);
	    addSeparator();
	    JMenuItem itemExit =
		new JMenuItem (bundle.getString ("label.text.menuItemExit"));
	    itemExit.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemExit"));
	    itemExit.addActionListener (gui.getExitListener ());
	    add (itemExit);
	}
    }

    class CopyItem extends JMenuItem
    {
	private final static long serialVersionUID = 0000000000000000000L;

	private String functionID, functionLabel;

	private CopyItem () { assert false : "invalid constructor call"; }

	CopyItem (String label, String functionID, String functionLabel)
	{
	    super (label);
	    this.functionID = functionID;
	    this.functionLabel = functionLabel;
	}

	String getFunctionID () { return functionID; }
	String getFunctionLabel () { return functionLabel; }
    }

    private class EditMenu extends JMenu
    {
	private final static long serialVersionUID = 0000000000000000000L;
	private int itemCopyPos;

	EditMenu ()
	{
	    super (bundle.getString ("label.text.menuTitleEdit"));
	    GUI gui = GUI.getInstance ();
	    setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuTitleEdit"));
	    JMenuItem itemBlacken =
		new JMenuItem (bundle.getString ("label.text.menuItemBlacken"));
	    itemBlacken.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemBlacken"));
	    itemBlacken.addActionListener (gui.getBlackenListener ());
	    add (itemBlacken);
	    JMenuItem itemFont =
		new JMenuItem (bundle.getString ("label.text.menuItemFont"));
	    itemFont.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemFont"));
	    itemFont.addActionListener (gui.getFontListener ());
	    add (itemFont);
	    JMenuItem itemBGColor =
		new JMenuItem (bundle.getString ("label.text.menuItemBgColor"));
	    itemBGColor.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemBgColor"));
	    itemBGColor.addActionListener (gui.getBGColorListener ());
	    add (itemBGColor);
	    addSeparator();

	    itemCopyPos = getItemCount ();
	}

	void addCopyItem (String label, int keyEvent, String functionID)
	{
	    GUI gui = GUI.getInstance ();
	    String fullLabel =
		bundle.getString ("label.text.menuItemCopy", label);
	    JMenuItem itemCopy =
		new CopyItem (fullLabel, functionID, label);
	    if (keyEvent != KeyEvent.VK_UNDEFINED)
		itemCopy.setMnemonic (keyEvent);
	    itemCopy.addActionListener (gui.getCopyListener ());
	    insert (itemCopy, itemCopyPos);
	}

	CopyItem removeCopyItem (String functionID)
	{
	    for (int pos = itemCopyPos; pos < getItemCount (); pos++)
		{
		    JMenuItem item = getItem (pos);
		    if (item instanceof CopyItem)
			{
			    CopyItem copyItem = (CopyItem)item;
			    if (copyItem.getFunctionID () == functionID)
				{
				    remove (pos);
				    return copyItem;
				}
			}
		}
	    return null; // no matching copy item found
	}
    }

    private class HelpMenu extends JMenu
    {
	private final static long serialVersionUID = 0000000000000000000L;

	HelpMenu ()
	{
	    super (bundle.getString ("label.text.menuTitleHelp"));
	    GUI gui = GUI.getInstance ();
	    setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuTitleHelp"));
	    JMenuItem itemDocumentation =
		new JMenuItem (bundle.getString ("label.text.menuItemDocumentation"));
	    itemDocumentation.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemDocumentation"));
	    itemDocumentation.addActionListener (gui.getDocumentationListener ());
	    add (itemDocumentation);
	    JMenuItem itemAbout =
		new JMenuItem (bundle.getString ("label.text.menuItemAbout"));
	    itemAbout.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemAbout"));
	    itemAbout.addActionListener (gui.getAboutListener ());
	    add (itemAbout);
	    JMenuItem itemLicence =
		new JMenuItem (bundle.getString ("label.text.menuItemLicence"));
	    itemLicence.
		setMnemonic (bundle.getKeyEvent ("label.mnemonic.menuItemLicence"));
	    itemLicence.addActionListener (gui.getLicenceListener ());
	    add (itemLicence);
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
