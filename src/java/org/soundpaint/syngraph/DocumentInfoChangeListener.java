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
 * $Date: 2006-11-19 21:50:30 +0100 (Sun, 19 Nov 2006) $
 * $Id: MenuBar.java 149 2006-11-19 20:50:30Z reuter $
 */
package org.soundpaint.syngraph;

/**
 * A class that wants to listen to changes in a DocumentInfo has to
 * implement this interface.
 *
 * @author Jürgen Reuter
 */
public interface DocumentInfoChangeListener
{
    /**
     * This method is called when the file path of the document has
     * changed.
     */
    public void pathChanged (String documentPath);

    /**
     * This method is called, when DocumentInfo.haveDocument () may
     * return a different than the last time this method was called.
     */
    public void haveDocumentChanged (boolean haveDocument);

    /**
     * This method is called, when DocumentInfo.haveUnsavedChanges ()
     * may return a different value than the last time this method was
     * called.
     */
    public void hasUnsavedChangesChanged (boolean hasUnsavedChanges);
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
