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
 * $Id: PrintDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
//import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URI;
import java.net.URISyntaxException;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;

public class PrintDialog
{
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private PageFormat pageFormat;
    private PrintRequestAttributeSet attributeSet;
    private String printToFilePathId;
    private Frame owner;
    protected Main synGraph;

    private PrintDialog () {}

    PrintDialog (Frame owner, Main synGraph, String dialogId)
    {
	this.owner = owner;
	this.synGraph = synGraph;
	printToFilePathId = dialogId + "PrintToFilePath";
	attributeSet = new HashPrintRequestAttributeSet ();

	/*
	 * TODO: Disable page range selection and other non-useful
	 * printing options for a one-page printout.  The following
	 * code unfortunately does not work as expected:
	 */
	//attributeSet.add (new javax.print.attribute.standard.PageRanges (1));
    }

    /**
     * @return True, if the component has been sucessfully submitted
     * as a print job.
     */
    boolean showDialog (Component component)
    {
	PrinterJob printerJob = PrinterJob.getPrinterJob ();
	/*
	pageFormat = printerJob.defaultPage ();
	Book book = new Book ();
	book.append (new ComponentPrintable (component), pageFormat);
	printerJob.setPageable (book);
	*/
	loadInputFields ();
	if (printerJob.printDialog (attributeSet))
	    {
		saveInputFields ();
		try
		    {
			printerJob.print ();
		    }
		catch (PrinterException e)
		    {
			JOptionPane.
			    showMessageDialog (component,
					       e.getLocalizedMessage (),
					       bundle.getString ("frame.title.errorPrint"),
					       JOptionPane.ERROR_MESSAGE);
			return false;
		    }
		return true;
	    }
	else // dialog cancelled
	    return false;
    }

    private class ComponentPrintable implements Printable
    {
	private Component component;

	private ComponentPrintable (Component component)
	{
	    this.component = component;
	}

	public int print (Graphics graphics, PageFormat pageFormat, int pageIndex)
	{
	    if (pageIndex > 10)
		return NO_SUCH_PAGE;
	    Graphics2D g2d = (Graphics2D)graphics;
	    g2d.translate (pageFormat.getImageableX (),
			   pageFormat.getImageableY ());
	    RepaintManager repaintManager =
		RepaintManager.currentManager (component);
	    repaintManager.setDoubleBufferingEnabled (false);
	    component.paint (g2d);
	    repaintManager.setDoubleBufferingEnabled (true);
	    return PAGE_EXISTS;
	}
    }

    /**
     * TODO: To be implemented.
     */
    public void setBounds (Rectangle bounds)
    {
    }

    /**
     * TODO: To be implemented.
     */
    public Rectangle getBounds ()
    {
	return null;
    }

    private void saveInputFields ()
    {
	Attribute att = attributeSet.get (Destination.class);
	if (att != null)
	    {
		if (att instanceof Destination)
		    {
			// TODO: Ensure that att is a plain file URL
			// without query.
			String printToFilePath =
			    ((Destination)att).getURI ().getPath ();
			synGraph.getPreferences ().
			    setString (printToFilePathId, printToFilePath);
		    }
		else
		    assert false; // unexpected attribute
	    }
	else
	    synGraph.getPreferences ().remove (printToFilePathId);
	// TODO: Add support for a lot more of attributes.
    }

    private void loadInputFields ()
    {
	try
	    {
		String printToFilePath =
		    synGraph.getPreferences ().getString (printToFilePathId);
		if (printToFilePath.length () > 0)
		    {
			URI uri = new URI ("file:" + printToFilePath);
			attributeSet.add (new Destination (uri));
			// TODO: Check that "add" really replaces any old
			// Destination attribute.
		    }
	    }
	catch (URISyntaxException e)
	    {
		assert false : e.getLocalizedMessage ();
	    }
	// TODO: Add support for a lot more of attributes.
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
