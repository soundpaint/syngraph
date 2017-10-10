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
 * $Id: FileChooser.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * An abstract dialog for selecting a file for loading/saving/exporting.
 *
 * @author Jürgen Reuter
 * @see OpenDialog
 * @see SaveDialog
 * @see ExportDialog
 */
public abstract class FileChooser extends JFileChooser
{
    /**
     * TODO: Store selected file filter in preferences.
     */

    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    protected Main synGraph;
    private Frame owner;
    private FileFilter sygFilter;
    private FileView fileView;
    private JDialog dialog;

    private FileChooser () {}

    // TODO: Maybe introduce an abstract subclass ImgFileChooser in
    // order to eliminate image specific stuff from this class?

    public final static FileFilter SYG_FILTERS[] =
    {
	new FileFilter (new String[] {"syg"},
			bundle.getString ("label.text.fileTypeSyg"))
    };

    public final static FileFilter IMG_FILTERS[];

    static // IMG_FILTER
    {
	String mimeTypes[] = ImageIO.getWriterMIMETypes ();
	IMG_FILTERS = new FileFilter[mimeTypes.length];
	for (int i = 0; i < mimeTypes.length; i++)
	    {
		TreeSet<String> suffixes = new TreeSet<String> ();
		for (Iterator<ImageWriter> writers =
			 ImageIO.getImageWritersByMIMEType (mimeTypes[i]);
		     writers.hasNext ();)
		    {
			ImageWriter writer = writers.next ();
			ImageWriterSpi spi = writer.getOriginatingProvider();
			String suffixArray[] = spi.getFileSuffixes ();
			for (String suffix: suffixArray)
			    suffixes.add (suffix);
		    }
		IMG_FILTERS[i] =
		    new FileFilter (suffixes.toArray (new String[0]),
				    mimeTypes[i]);
	    }
    }

    public FileChooser (Frame owner, Main synGraph, String title,
			FileFilter fileFilters[])
    {
	super ();
	this.synGraph = synGraph;
	this.owner = owner;
	dialog = super.createDialog (owner);
	dialog.setTitle (title);
	setDialogType (CUSTOM_DIALOG);

	/*
	 * Workaround for I18N bug in JFileChooser: set "cancel"
	 * button text manually.  This workaround should be removed,
	 * as soon as JFileChooser is fixed.
	 */
	//setCancelButtonText (bundle.getString ("button.text.cancel"));

	resetChoosableFileFilters();
	setAcceptAllFileFilterUsed (true);
	for (int i = 0; i < fileFilters.length; i++)
	    addChoosableFileFilter (fileFilters[i]);
	fileView = new FileView ();
	Image image =
	    ResourceBundle.loadImage ("/media/syngraph.png", synGraph);
	if (image != null)
	    fileView.putIcon ("syg", new ImageIcon (image));
	setFileView (fileView);
	setFileSelectionMode (FILES_ONLY);
	setSelectedFile (null);
	setMultiSelectionEnabled (false);
	setFileHidingEnabled (true);
	setControlButtonsAreShown (true);
    }

    protected JDialog createDialog ()
    {
	return dialog;
    }

    public void setBounds (Rectangle bounds)
    {
	dialog.setBounds (bounds);
    }

    public Rectangle getBounds ()
    {
	return dialog.getBounds ();
    }

    /**
     * Saves this dialog's path in the preferences.
     */
    abstract protected void savePath (String path);

    /**
     * Saves the description of this dialog's filter in the
     * preferences.
     */
    abstract protected void saveFilterDescription (String filterDescription);

    protected void saveInputFields ()
    {
	File file = getSelectedFile ();
	if (file != null)
	    savePath (file.getPath ());
	javax.swing.filechooser.FileFilter filter = getFileFilter ();
	if (filter != null)
	    saveFilterDescription (filter.getDescription ());
    }

    /**
     * Fetches a proper filter description from the preferences in
     * order to set this dialog's initial filter.
     */
    abstract protected String loadPath ();

    /**
     * Fetches a proper path from the preferences in order to set this
     * dialog's initial path.
     */
    abstract protected String loadFilterDescription ();

    protected void loadInputFields ()
    {
	String path = loadPath ();
	File file = (path != null) ? new File (path) : null;
	setSelectedFile (file);
	FileFilter filter = null;
	String description = loadFilterDescription ();
	if (description != null)
	    {
		javax.swing.filechooser.FileFilter filters[] =
		    getChoosableFileFilters ();
		for (javax.swing.filechooser.FileFilter candidateFilter :
			 filters)
		    {
			if (!(candidateFilter instanceof FileFilter))
			    // the "*" all files filter
			    continue;
			FileFilter hotCandidateFilter =
			    (FileFilter)candidateFilter;
			if (description.equals (hotCandidateFilter.
						getDescription ()))
			    {
				filter = hotCandidateFilter;
				break;
			    }
		    }
	    }
	if (filter != null)
	    setFileFilter (filter);
    }

    public int showFileDialog ()
    {
	return showDialog (owner, null);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
