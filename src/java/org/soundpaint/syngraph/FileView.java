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
 * $Id: FileView.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.io.File;
import java.util.Hashtable;
import javax.swing.Icon;

/**
 * A file extension suffix based model for files.  It associates
 * descriptions, icons and suffixes with file types.  This file is
 * derived from example code in the Java swing tutorial.
 *
 * @author Jürgen Reuter
 */
public class FileView extends javax.swing.filechooser.FileView
{
    private Hashtable <String, Icon> icons;
    private Hashtable <File, String> fileDescriptions;
    private Hashtable <String, String> typeDescriptions;

    public FileView ()
    {
	icons = new Hashtable <String, Icon> (5);
	fileDescriptions = new Hashtable <File, String> (5);
	typeDescriptions = new Hashtable <String, String> (5);
    }

    /**
     * The name of the file.  Do nothing special here. Let the system
     * file view handle this.
     *
     * @see javax.swing.filechooser.FileView#getName(File)
     */
    public String getName (File file)
    {
	return null;
    }

    /**
     * Adds a human readable description of the file.
     */
    public void putDescription (File file, String fileDescription)
    {
	fileDescriptions.put (file, fileDescription);
    }

    /**
     * A human readable description of the file.
     *
     * @see javax.swing.filechooser.FileView#getDescription(File)
     */
    public String getDescription (File file)
    {
	return fileDescriptions.get (file);
    };

    /**
     * Adds a human readable type description for files. Based on
     * "dot" extension strings, e.g: ".gif". Case is ignored.
     */
    public void putTypeDescription (String extension, String typeDescription)
    {
	typeDescriptions.put (typeDescription, extension);
    }

    /**
     * Adds a human readable type description for files of the type of
     * the passed in file. Based on "dot" extension strings, e.g:
     * ".gif".  Case is ignored.
     */
    public void putTypeDescription (File file, String typeDescription)
    {
	putTypeDescription (getExtension (file), typeDescription);
    }

    /**
     * A human readable description of the type of the file.
     *
     * @see javax.swing.filechooser.FileView#getTypeDescription(File)
     */
    public String getTypeDescription (File file)
    {
	return typeDescriptions.get (getExtension (file));
    }

    /**
     * Conveinience method that returns a the "dot" extension for the
     * given file.
     */
    private static String getExtension (File file)
    {
	String name = file.getName ();
	if (name != null)
	    {
		int extensionIndex = name.lastIndexOf ('.');
		if (extensionIndex < 0)
		    return null;
		return name.substring (extensionIndex + 1).toLowerCase ();
	    }
	return null;
    }

    /**
     * Adds an icon based on the file type "dot" extension string,
     * e.g: ".gif". Case is ignored.
     */
    public void putIcon (String extension, Icon icon)
    {
	icons.put (extension, icon);
    }

    /**
     * Icon that reperesents this file. Default implementation returns
     * null. You might want to override this to return something more
     * interesting.
     *
     * @see javax.swing.filechooser.FileView#getIcon(File)
     */
    public Icon getIcon (File file)
    {
	String extension = getExtension (file);
	return (extension != null) ? icons.get (extension) : null;
    }

    /**
     * Whether the directory is traversable or not. Generic
     * implementation returns @code{null} for all directories and
     * special folders, in order to use the default from
     * FileSystemView.  If a directory is not traversable,
     * Boolean.FALSE should be returned.
     *
     * You might want to subtype ExampleFileView to do somethimg more
     * interesting, such as recognize compound documents directories;
     * in such a case you might return a special icon for the diretory
     * that makes it look like a regular document, and return false
     * for isTraversable to not allow users to descend into the
     * directory.
     *
     * @see javax.swing.filechooser.FileView#isTraversable(File)
     */
    public Boolean isTraversable (File f)
    {
	return null; // Use default from FileSystemView
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
