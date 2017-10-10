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
 * $Id: ExportDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;

/**
 * A dialog for exporting a screenshot of an AWT component to a bitmap
 * graphics file.
 *
 * @author Jürgen Reuter
 */
public class ExportDialog extends FileChooser
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private String exportPathId;
    private String exportFilterDescriptionId;

    public ExportDialog (Frame owner, Main synGraph, String dialogId)
    {
	super (owner, synGraph, bundle.getString ("frame.title.exportAs"),
	       IMG_FILTERS);
	setApproveButtonText (bundle.getString ("button.text.export"));
	setApproveButtonMnemonic (bundle.getKeyEvent ("button.mnemonic.export"));
	setApproveButtonToolTipText (bundle.getString ("button.tooltip.export"));
	exportPathId = dialogId + "Path";
	exportFilterDescriptionId = dialogId + "FilterDescription";
    }

    private ImageWriter findMatchingImageWriterForSuffix (String suffix)
    {
	if (suffix == null)
	    return null;
	Iterator<ImageWriter> writers =
	    ImageIO.getImageWritersBySuffix (suffix);
	if (writers == null)
	    return null;
	if (!writers.hasNext ())
	    return null;
	return writers.next ();
    }

    private String findMatchingFormatNameForFile (File file)
    {
	String suffix = FileFilter.getExtension (file);
	ImageWriter writer = findMatchingImageWriterForSuffix (suffix);
	if (writer == null)
	    // failed determining writer by file suffix
	    {
		// try to determine writer by file filter
		Object obj = getFileFilter ();
		if (obj instanceof FileFilter)
		    {
			FileFilter filter = (FileFilter)obj;
			suffix =
			    (String)(filter.getExtensions ().nextElement ());
			writer = findMatchingImageWriterForSuffix (suffix);
		    }
		else
		    // "all files" filter
		    {
			// Have no clue for what type to choose =>
			// return null.
		    }
	    }
	return (writer != null) ? suffix : null;
    }

    private void export (File file, Component component) throws IOException
    {
	// TODO: If file filter is set to, say, ".jpeg", but filename
	// is, say, "foo.png", then save as jpeg or as png?
	// (Currently, the filename wins.)

	String formatName = findMatchingFormatNameForFile (file);
	if (formatName == null)
	    throw new IOException (bundle.getString ("label.text.errorNoProperExportDriver"));
	int width = component.getWidth ();
	int height = component.getHeight ();
	BufferedImage image =
	    new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d = image.createGraphics ();
	RepaintManager repaintManager =
	    RepaintManager.currentManager (component);
	repaintManager.setDoubleBufferingEnabled (false);
	component.paint (g2d);
	repaintManager.setDoubleBufferingEnabled (true);
	ImageIO.write (image, formatName, file);
    }

    private boolean confirmOverwrite ()
    {
	int result =
	    JOptionPane.
	    showConfirmDialog (null,
			       bundle.getString ("label.text.queryOverwrite"),
			       bundle.getString ("frame.title.confirm"),
			       JOptionPane.YES_NO_OPTION);
	return result == JOptionPane.YES_OPTION;
    }

    /**
     * @return true if and only if the file was actually exported
     */
    public boolean showDialog (Component component)
    {
	if (IMG_FILTERS.length == 0)
	    // no image driver available => no image export supported
	    {
		JOptionPane.
		    showMessageDialog (this,
				       bundle.getString ("label.text.errorNoExportDriver"),
				       bundle.getString ("frame.title.errorExport"),
				       JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	loadInputFields ();
	int result = showFileDialog ();
	if (result == APPROVE_OPTION)
	    {
		File file = getSelectedFile ();
		if (file != null)
		    {
			if (file.exists ())
			    {
				if (!confirmOverwrite ())
				    return false; // overwrite denied
			    }
			saveInputFields ();
			try
			    {
				export (file, component);
			    }
			catch (IOException e)
			    {
				String message =
				    bundle.getString ("label.text.errorExport",
						      e.getLocalizedMessage ());
				JOptionPane.
				    showMessageDialog (this,
						       message,
						       bundle.getString ("frame.title.errorExport"),
						       JOptionPane.ERROR_MESSAGE);
				return false; // export failed
			    }
			return true; // successful export
		    }
		return false; // no file selected for export
	    }
	else
	    return false; // export cancelled
    }

    protected void savePath (String path)
    {
	synGraph.getPreferences ().setString (exportPathId, path);
    }

    protected void saveFilterDescription (String filterDescription)
    {
	synGraph.getPreferences ().
	    setString (exportFilterDescriptionId, filterDescription);
    }

    protected String loadPath ()
    {
	return synGraph.getPreferences ().getString (exportPathId);
    }

    protected String loadFilterDescription ()
    {
	return synGraph.getPreferences ().
	    getString (exportFilterDescriptionId);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
