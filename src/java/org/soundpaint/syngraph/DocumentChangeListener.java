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
 * $Id: DocumentChangeListener.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Font;

/**
 * A class that wants to listen to changes in a Document has to
 * implement this interface.
 *
 * @author Jürgen Reuter
 * @see DocumentChangeAdapter
 */
public interface DocumentChangeListener
{
    public void titleChanged (String title);
    public void authorChanged (String author);
    public void commentChanged (String comment);
    public void objectLabelChanged (String objectID);
    public void objectCommentChanged (String objectID);
    public void objectColorChanged (String objectID, Color color);
    public void bgColorChanged (Color color);
    public void fontChanged (Font font);

    /**
     * @param tabID the identifier of the tab that is to be added.
     */
    public void tabAdded (String tabID);

    /**
     * @param tabID the identifier of the tab that is to be removed.
     */
    public void tabRemoved (String tabID);

    /**
     * @param tabID the identifier of the tab.
     */
    public void tabLabelChanged (String tabID);

    /**
     * @param tabID the identifier of the tab.
     */
    public void tabCommentChanged (String tabID);

    /**
     * @param tabID the identifier of the tab.
     * @param objectID the identifier of the object that changes its
     * alignment.
     * @param x new x coordinate of object or -1, if the object is to
     * be removed from the tab.
     * @param y new y coordinate of object or -1, if the object is to
     * be removed from the tab.
     */
    public void tabObjectAlignChanged (String tabID, String objectID,
				       float x, float y);

    public void selectedTabChanged (String tabID);

    /**
     * @param functionID the identifier of the function that is to be
     * added.
     */
    public void functionAdded (String functionID);

    /**
     * @param functionID the identifier of the function that is to be
     * removed.
     */
    public void functionRemoved (String functionID);

    /**
     * @param functionID the identifier of the function.
     */
    public void functionLabelChanged (String functionID);

    /**
     * @param functionID the identifier of the function.
     */
    public void functionCommentChanged (String functionID);

    // TODO: Do we need a method "functionValueChanged (String
    // functionID, String keyID, String valueID)"?

    /**
     * @param setID the identifier of the set that is to be added.
     */
    public void setAdded (String setID);

    /**
     * @param setID the identifier of the set that is to be removed.
     */
    public void setRemoved (String setID);

    /**
     * @param setID the identifier of the set.
     */
    public void setLabelChanged (String setID);

    /**
     * @param setID the identifier of the set.
     */
    public void setCommentChanged (String setID);
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
