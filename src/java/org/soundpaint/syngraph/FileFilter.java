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
 * $Id: FileFilter.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A simple FileFilter implementation based on file extension
 * suffixes.  This file is derived from example code in the Java swing
 * tutorial.
 *
 * @author Jürgen Reuter
 */
public class FileFilter extends javax.swing.filechooser.FileFilter
{

    private Hashtable<String, FileFilter> filters;
    private String description;
    private String fullDescription;
    private boolean useExtensionsInDescription;

    /**
     * Creates a file filter. If no filters are added, then all files
     * are accepted.
     *
     * @see #addExtension(String)
     */
    public FileFilter ()
    {
	this.filters = new Hashtable<String, FileFilter> ();
	description = null;
	fullDescription = null;
	useExtensionsInDescription = true;
    }

    /**
     * Creates a file filter that accepts files with the given
     * extension.  Example: new FileFilter ("jpg");
     *
     * Note that the "." before the extension is not needed. If
     * provided, it will be ignored.
     *
     * @see #addExtension(String)
     */
    public FileFilter (String extension)
    {
	this (extension, null);
    }

    /**
     * Creates a file filter that accepts the given file type.
     * Example: new FileFilter ("jpg", "JPEG Image Images");
     *
     * Note that the "." before the extension is not needed. If
     * provided, it will be ignored.
     *
     * @see #addExtension(String)
     */
    public FileFilter (String extension, String description)
    {
	this ();
	if (extension != null)
	    addExtension (extension);
	if (description != null)
	    setDescription (description);
    }

    /**
     * Creates a file filter from the given string array.  Example:
     * new FileFilter (String {"gif", "jpg"});
     *
     * Note that the "." before the extension is not needed and will
     * be ignored.
     *
     * @see #addExtension(String)
     */
    public FileFilter (String[] filters)
    {
	this (filters, null);
    }

    /**
     * Creates a file filter from the given string array and
     * description.  Example: new FileFilter (String {"gif", "jpg"},
     * "Gif and JPG Images");
     *
     * Note that the "." before the extension is not needed and will
     * be ignored.
     *
     * @see #addExtension(String)
     */
    public FileFilter (String[] filters, String description)
    {
	this ();
	for (int i = 0; i < filters.length; i++)
	    {
		// add filters one by one
		addExtension (filters[i]);
	    }
	if (description != null)
	    setDescription (description);
    }

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * Files that begin with "." are ignored.
     *
     * @see #getExtension(File)
     * @see javax.swing.filechooser.FileFilter#accept(File)
     */
    public boolean accept (File file)
    {
	if (file != null)
	    {
		if (file.isDirectory ())
		    return true;
		String extension = getExtension (file);
		if ((extension != null) &&
		    (filters.get (getExtension (file)) != null))
		    return true;
	    }
	return false;
    }

    /**
     * Return the extension portion of the file's name.
     */
    public static String getExtension (File file)
    {
	if (file != null)
	    {
		String filename = file.getName ();
		int extPos = filename.lastIndexOf ('.');
		if ((extPos > 0) && (extPos < filename.length () - 1))
		    return filename.substring (extPos + 1).toLowerCase ();
	    }
	return null;
    }

    /**
     * Adds a filetype "dot" extension to filter against.
     *
     * For example: the following code will create a filter that
     * filters out all files except those that end in ".jpg" and
     * ".tif":
     *
     *   FileFilter filter = new FileFilter ();
     *   filter.addExtension ("jpg");
     *   filter.addExtension ("tif");
     *
     * Note that the "." before the extension is not needed and will
     * be ignored.
     */
    public void addExtension (String extension)
    {
	filters.put (extension.toLowerCase (), this);
	fullDescription = null;
    }

    /**
     * Returns all filetype "dot" extensions that have been added to
     * filter against.
     *
     * @see #addExtension(String)
     */
    public Enumeration getExtensions ()
    {
	return filters.keys ();
    }

    /**
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     *
     * @see #setDescription(String)
     * @see #setExtensionListInDescription(boolean)
     * @see #isExtensionListInDescription()
     * @see javax.swing.filechooser.FileFilter#accept(File)
     */
    public String getDescription ()
    {
	if (fullDescription == null)
	    if (description == null || isExtensionListInDescription ())
		{
		    fullDescription = (description == null) ?
			"(" : description + " (";
		    // build the description from the extension list
		    Enumeration extensions = filters.keys ();
		    if (extensions != null)
			{
			    fullDescription +=
				"." + (String)extensions.nextElement ();
			    while (extensions.hasMoreElements ())
				fullDescription +=
				    ", ." + (String)extensions.nextElement ();
			}
		    fullDescription += ")";
		}
	    else
		fullDescription = description;
	return fullDescription;
    }

    /**
     * Sets the human readable description of this filter. For
     * example: filter.setDescription ("Gif and JPG Images");
     *
     * @see #setDescription(String)
     * @see #setExtensionListInDescription(boolean)
     * @see #isExtensionListInDescription()
     */
    public void setDescription (String description)
    {
	this.description = description;
	fullDescription = null;
    }

    /**
     * Determines whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription ();
     *
     * @see #getDescription()
     * @see #setDescription(String)
     * @see #isExtensionListInDescription()
     */
    public void setExtensionListInDescription (boolean flag)
    {
	useExtensionsInDescription = flag;
	fullDescription = null;
    }

    /**
     * Returns whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription ();
     *
     * @see #getDescription()
     * @see #setDescription(String)
     * @see #setExtensionListInDescription(boolean)
     */
    public boolean isExtensionListInDescription ()
    {
	return useExtensionsInDescription;
    }

}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
