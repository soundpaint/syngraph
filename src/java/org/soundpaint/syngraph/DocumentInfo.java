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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

/**
 * This class holds information about a syngraph document, such as it
 * has been modified, and a reference to the actual document (if
 * loaded).
 *
 * @author Jürgen Reuter
 */
class DocumentInfo
{
    private final static String TEMPLATE_DOCUMENT = "/media/template.syg";

    private Vector<DocumentChangeListener> listeners;
    private Vector<DocumentInfoChangeListener> infoListeners;
    private Document document;
    private String path;
    private String author;
    private boolean hasUnsavedChanges;

    public DocumentInfo (String author)
    {
	infoListeners = new Vector<DocumentInfoChangeListener> ();
	listeners = new Vector<DocumentChangeListener> ();
	this.author = author;
    }

    /**
     * Makes the specified document the target of subsequent editing
     * operations.  The document is assumed to initially have no
     * unsaved changes.
     */
    private void setDocument (Document document, String path)
    {
	boolean haveNewDocument =
	    (this.document == null) && (document != null);
	boolean haveNoMoreDocument =
	    (this.document != null) && (document == null);
	if (this.document != null)
	    this.document.detachFromGUI ();
	this.document = document;
	if (document != null)
	    document.attachToGUI ();
	setPath (path);
	if (haveNewDocument)
	    for (DocumentInfoChangeListener listener : infoListeners)
		listener.haveDocumentChanged (true);
	if (haveNoMoreDocument)
	    for (DocumentInfoChangeListener listener : infoListeners)
		listener.haveDocumentChanged (false);
	setUnsavedChanges (false);
    }

    /**
     * Loads a document from the specified file path.
     */
    public void loadDocument (String path) throws IOException
    {
	Document document = new Document (author, this);
	document.load (path);
	setDocument (document, path);
    }

    /**
     * Loads a default document template.
     */
    public void loadDocument () throws IOException
    {
	URL url = Main.class.getResource (TEMPLATE_DOCUMENT);
	if (url != null)
	    {
		Document document = new Document (author, this);
		document.load (url.openStream ());
		setDocument (document, null);
	    }
	else // template document not found
	    throw new FileNotFoundException (TEMPLATE_DOCUMENT);
    }

    public void removeDocument ()
    {
	setDocument (null, null);
    }

    public void saveDocument () throws IOException
    {
	document.save (path);
    }

    public void saveDocumentAs (String path) throws IOException
    {
	document.save (path);
	setPath (path);
    }

    public Document getDocument ()
    {
	return document;
    }

    public boolean haveDocument ()
    {
	return document != null;
    }

    public String getPath ()
    {
	return path;
    }

    private void setPath (String path)
    {
	this.path = path;
	for (DocumentInfoChangeListener listener : infoListeners)
	    listener.pathChanged (path);
    }

    public boolean hasUnsavedChanges ()
    {
	return hasUnsavedChanges;
    }

    public void setUnsavedChanges (boolean hasUnsavedChanges)
    {
	this.hasUnsavedChanges = hasUnsavedChanges;
	for (DocumentInfoChangeListener listener : infoListeners)
	    listener.hasUnsavedChangesChanged (hasUnsavedChanges);
    }

    void addDocumentInfoChangeListener (DocumentInfoChangeListener listener)
    {
	infoListeners.add (listener);
    }

    void addDocumentChangeListener (DocumentChangeListener listener)
    {
	listeners.add (listener);
    }

    Vector<DocumentChangeListener> getListeners ()
    {
	return listeners;
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
