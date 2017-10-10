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
 * $Id: OpenDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

public class OpenDialog extends FileChooser
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    public OpenDialog (Frame owner, Main synGraph)
    {
	super (owner, synGraph, bundle.getString ("frame.title.open"),
	       SYG_FILTERS);
	setApproveButtonText (bundle.getString ("button.text.open"));
	setApproveButtonToolTipText (bundle.getString ("button.tooltip.open"));
	setApproveButtonMnemonic (bundle.getKeyEvent ("button.mnemonic.open"));
    }

    private boolean confirmDiscardChanges ()
    {
	int result =
	    JOptionPane.
	    showConfirmDialog (null,
			       bundle.getString ("label.text.queryDiscardChanges"),
			       bundle.getString ("frame.title.confirm"),
			       JOptionPane.YES_NO_OPTION);
	return result == JOptionPane.YES_OPTION;
    }

    /**
     * @return true if and only if a file was actually loaded
     */
    public boolean showDialog ()
    {
	loadInputFields ();
	int result = showFileDialog ();
	if (result == APPROVE_OPTION)
	    {
		File file = getSelectedFile ();
		if (file == null)
		    return false; // no file selected for load
		DocumentInfo documentInfo = synGraph.getDocumentInfo ();
		if (documentInfo.hasUnsavedChanges ())
		    if (!confirmDiscardChanges ())
			return false; // discard changes denied
		saveInputFields ();
		try
		    {
			documentInfo.loadDocument (file.getPath ());
			return true; // successful load
		    }
		catch (IOException e)
		    {
			String message =
			    bundle.getString ("label.text.errorLoad",
					      e.getLocalizedMessage ());
			JOptionPane.
			    showMessageDialog (this,
					       message,
					       bundle.getString ("frame.title.errorLoad"),
					       JOptionPane.ERROR_MESSAGE);
			return false; // load failed
		    }
	    }
	return false; // load cancelled
    }

    protected void savePath (String path)
    {
	synGraph.getPreferences ().setDocumentPath (path);
    }

    protected void saveFilterDescription (String filterDescription)
    {
	synGraph.getPreferences ().
	    setOpenFilterDescription (filterDescription);
    }

    protected String loadPath ()
    {
	return synGraph.getPreferences ().getDocumentPath ();
    }

    protected String loadFilterDescription ()
    {
	return synGraph.getPreferences ().getOpenFilterDescription ();
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
