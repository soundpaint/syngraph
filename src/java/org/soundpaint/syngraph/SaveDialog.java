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
 * $Id: SaveDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * A dialog for selecting a file for saving the current document.
 *
 * @author Jürgen Reuter
 */
public class SaveDialog extends FileChooser
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    public SaveDialog (Frame owner, Main synGraph)
    {
	super (owner, synGraph, bundle.getString ("frame.title.saveAs"),
	       SYG_FILTERS);
	setApproveButtonText (bundle.getString ("button.text.save"));
	setApproveButtonMnemonic (bundle.getKeyEvent ("button.mnemonic.save"));
	setApproveButtonToolTipText (bundle.getString ("button.tooltip.save"));
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
     * @return true if and only if the file was actually saved
     */
    public boolean showDialog ()
    {
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
				DocumentInfo documentInfo =
				    synGraph.getDocumentInfo ();
				documentInfo.saveDocumentAs (file.getPath ());
			    }
			catch (IOException e)
			    {
				String message =
				    bundle.getString ("label.text.errorSave",
						      e.getLocalizedMessage ());
				JOptionPane.
				    showMessageDialog (this,
						       message,
						       bundle.getString ("frame.title.errorSave"),
						       JOptionPane.ERROR_MESSAGE);
				return false; // save failed
			    }
			return true; // successful save
		    }
		return false; // no file selected for save
	    }
	else
	    return false; // save cancelled
    }

    protected void savePath (String path)
    {
	synGraph.getPreferences ().setDocumentPath (path);
    }

    protected void saveFilterDescription (String filterDescription)
    {
	synGraph.getPreferences ().
	    setSaveFilterDescription (filterDescription);
    }

    protected String loadPath ()
    {
	return synGraph.getPreferences ().getDocumentPath ();
    }

    protected String loadFilterDescription ()
    {
	return synGraph.getPreferences ().getSaveFilterDescription ();
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
