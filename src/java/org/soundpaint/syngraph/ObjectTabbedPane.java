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
 * $Id: ObjectTabbedPane.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.HashMap;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class ObjectTabbedPane extends JTabbedPane
    implements ChangeListener, DocumentChangeListener
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static Dimension PREFERRED_PANE_SIZE =
	new Dimension (480, 480);

    private Main synGraph;
    private HashMap<String, ObjectTab> genericObjectTabs;

    private ObjectTabbedPane () { throw new RuntimeException (); }

    ObjectTabbedPane (Main synGraph)
    {
	this.synGraph = synGraph;
	synGraph.addDocumentChangeListener (this);
	addChangeListener (this);
	setPreferredSize (PREFERRED_PANE_SIZE);
	genericObjectTabs = new HashMap<String, ObjectTab> ();
    }

    public void stateChanged (ChangeEvent event)
    {
	Component component = getSelectedComponent ();
	if (component == null)
	    return;
	assert component instanceof ObjectTab;
	if (!(component instanceof ObjectTab))
	    return;
	ObjectTab tab = (ObjectTab)component;
	String tabID = tab.getTabID ();
	Document document = synGraph.getDocument ();
	assert document != null;
	if (document == null)
	    return;
	document.setSelectedTabID (tabID);
    }

    public void titleChanged (String title) {}
    public void authorChanged (String author) {}
    public void commentChanged (String comment) {}
    public void objectLabelChanged (String objectID) {}
    public void objectCommentChanged (String objectID) {}
    public void objectColorChanged (String objectID, Color color) {}
    public void bgColorChanged (Color color) {}
    public void fontChanged (Font font) {}

    public void tabAdded (String tabID)
    {
	ObjectTab tab = new ObjectTab (synGraph, tabID);
	genericObjectTabs.put (tabID, tab);
	addTab (tabID, tab);
    }

    public void tabRemoved (String tabID)
    {
	ObjectTab tab = genericObjectTabs.get (tabID);
	if (tab != null)
	    {
		remove (tab);
		genericObjectTabs.remove (tabID);
	    }
    }

    public void tabLabelChanged (String tabID)
    {
	ObjectTab tab = genericObjectTabs.get (tabID);
	if (tab == null)
	    return; // no such object tab
	Document document = synGraph.getDocument ();
	String label = (document != null) ?
	    document.getTabLabel (tabID) : null;
	if (label != null)
	    setTitleAt (indexOfComponent (tab), label);
	else
	    setTitleAt (indexOfComponent (tab), tabID);
    }

    public void tabCommentChanged (String tabID) {}
    public void tabObjectAlignChanged (String tabID, String objectID,
				       float x, float y) {}

    public void selectedTabChanged (String tabID)
    {
	ObjectTab tab = genericObjectTabs.get (tabID);
	setSelectedComponent (tab);
    }

    public void functionAdded (String functionID) {}
    public void functionRemoved (String functionID) {}
    public void functionLabelChanged (String functionID) {}
    public void functionCommentChanged (String functionID) {}
    public void setAdded (String setID) {}
    public void setRemoved (String setID) {}
    public void setLabelChanged (String setID) {}
    public void setCommentChanged (String setID) {}
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
