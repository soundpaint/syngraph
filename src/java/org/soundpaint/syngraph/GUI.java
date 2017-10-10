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
 * $Id: GUI.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * This class holds all frames and dialogs that have data associated
 * which has to be kept consistent across subsequent calls (such as
 * window size and location or input fields).  It also holds
 * cross-dialog listeners for all relevant actions that can be
 * triggered by pressing buttons or selecting menu items.
 *
 * @author Jürgen Reuter
 */
public class GUI
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private final static String APP_FRAME_ID = "appFrame";
    private final static String OBJECT_PROPERTIES_DIALOG_ID = "objectPropertiesDialog";
    private final static String OBJ_COLOR_CHOOSER_ID = "objColorChooser";
    private final static String OBJ_TAB_EXPORT_DIALOG_ID = "objTabExportDialog";
    private final static String OBJ_TAB_PRINT_DIALOG_ID = "objTabPrintDialog";
    private final static String OPEN_DIALOG_ID = "openDialog";
    private final static String SAVE_DIALOG_ID = "saveDialog";
    private final static String PROPERTIES_DIALOG_ID = "propertiesDialog";
    private final static String PREFERENCES_DIALOG_ID = "preferencesDialog";
    private final static String NOTICE_EDITOR_ID = "noticeEditor";
    private final static String NOTICE_EXPORT_DIALOG_ID = "noticeExportDialog";
    private final static String NOTICE_PRINT_DIALOG_ID = "noticePrintDialog";
    private final static String FONT_DIALOG_ID = "fontDialog";
    private final static String DOCUMENTATION_DIALOG_ID = "documentationDialog";
    private final static String LICENCE_DIALOG_ID = "licenceDialog";
    private final static String BG_COLOR_CHOOSER_ID = "bgColorChooser";

    private static GUI gui;

    private Main synGraph;

    // shared dialogs
    private AppFrame appFrame;
    private ColorChooser objColorChooser;
    private ExportDialog objTabExportDialog;
    private PrintDialog objTabPrintDialog;
    private ObjectPropertiesDialog objectPropertiesDialog;
    private OpenDialog openDialog;
    private SaveDialog saveDialog;
    private PropertiesDialog propertiesDialog;
    private PreferencesDialog preferencesDialog;
    private NoticeEditor noticeEditor;
    private ExportDialog noticeExportDialog;
    private PrintDialog noticePrintDialog;
    private FontDialog fontDialog;
    private DocumentationDialog documentationDialog;
    private LicenceDialog licenceDialog;
    private ColorChooser bgColorChooser;

    // shared listeners
    private OpenListener openListener;
    private SaveListener saveListener;
    private SaveAsListener saveAsListener;
    private CloseListener closeListener;
    private PreferencesListener preferencesListener;
    private PropertiesListener propertiesListener;
    private NoticeListener noticeListener;
    private ExitListener exitListener;
    private BlackenListener blackenListener;
    private FontListener fontListener;
    private BGColorListener bgColorListener;
    private CopyListener copyListener;
    private DocumentationListener documentationListener;
    private AboutListener aboutListener;
    private LicenceListener licenceListener;

    private GUI ()
    {
	openListener = new OpenListener ();
	saveListener = new SaveListener ();
	saveAsListener = new SaveAsListener ();
	closeListener = new CloseListener ();
	preferencesListener = new PreferencesListener ();
	propertiesListener = new PropertiesListener ();
	noticeListener = new NoticeListener ();
	exitListener = new ExitListener ();
	blackenListener = new BlackenListener ();
	fontListener = new FontListener ();
	bgColorListener = new BGColorListener ();
	copyListener = new CopyListener ();
	documentationListener = new DocumentationListener ();
	aboutListener = new AboutListener ();
	licenceListener = new LicenceListener ();
    }

    synchronized public static void initInstance (Main synGraph)
    {
	assert gui == null;
	gui = new GUI ();
	gui.synGraph = synGraph;
    }

    synchronized public static GUI getInstance ()
    {
	assert gui != null;
	return gui;
    }

    public synchronized AppFrame getAppFrame ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (appFrame == null)
	    {
		appFrame = new AppFrame (synGraph);
		Rectangle bounds = preferences.getBounds (APP_FRAME_ID);
		if (bounds != null)
		    {
			/*
			 * Only take window location (x, y) from
			 * preferences, but do not resize (i.e. change
			 * width or height) window, because the ideal
			 * size of this AppFrame depends on the
			 * currently loaded document rather than on
			 * the latest known size of this frame.
			 */
			bounds.width = appFrame.getBounds ().width;
			bounds.height = appFrame.getBounds ().height;

			appFrame.setBounds (bounds);
		    }
	    }
	return appFrame;
    }

    public synchronized ColorChooser getObjColorChooser ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (objColorChooser == null)
	    {
		objColorChooser =
		    new ColorChooser (getAppFrame (), synGraph,
				      ColorChooser.MODE.OBJ_COLOR);
		Rectangle bounds =
		    preferences.getBounds (OBJ_COLOR_CHOOSER_ID);
		if (bounds != null)
		    objColorChooser.setBounds (bounds);
	    }
	return objColorChooser;
    }

    public synchronized ObjectPropertiesDialog getObjectPropertiesDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (objectPropertiesDialog == null)
	    {
		objectPropertiesDialog =
		    new ObjectPropertiesDialog (getAppFrame (), synGraph);
		Rectangle bounds =
		    preferences.getBounds (OBJECT_PROPERTIES_DIALOG_ID);
		if (bounds != null)
		    objectPropertiesDialog.setBounds (bounds);
	    }
	return objectPropertiesDialog;
    }

    public synchronized ExportDialog getObjTabExportDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (objTabExportDialog == null)
	    {
		objTabExportDialog =
		    new ExportDialog (getAppFrame (), synGraph,
				      OBJ_TAB_EXPORT_DIALOG_ID);
		Rectangle bounds =
		    preferences.getBounds (OBJ_TAB_EXPORT_DIALOG_ID);
		if (bounds != null)
		    objTabExportDialog.setBounds (bounds);
	    }
	return objTabExportDialog;
    }

    public synchronized PrintDialog getObjTabPrintDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (objTabPrintDialog == null)
	    {
		objTabPrintDialog =
		    new PrintDialog (getAppFrame (), synGraph,
				     OBJ_TAB_PRINT_DIALOG_ID);
		Rectangle bounds =
		    preferences.getBounds (OBJ_TAB_PRINT_DIALOG_ID);
		if (bounds != null)
		    objTabPrintDialog.setBounds (bounds);
	    }
	return objTabPrintDialog;
    }

    public synchronized OpenDialog getOpenDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (openDialog == null)
	    {
		openDialog = new OpenDialog (getAppFrame (), synGraph);
		Rectangle bounds = preferences.getBounds (OPEN_DIALOG_ID);
		if (bounds != null)
		    openDialog.setBounds (bounds);
	    }
	return openDialog;
    }

    public synchronized SaveDialog getSaveDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (saveDialog == null)
	    {
		saveDialog = new SaveDialog (getAppFrame (), synGraph);
		Rectangle bounds = preferences.getBounds (SAVE_DIALOG_ID);
		if (bounds != null)
		    saveDialog.setBounds (bounds);
	    }
	return saveDialog;
    }

    public synchronized PropertiesDialog getPropertiesDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (propertiesDialog == null)
	    {
		propertiesDialog = new PropertiesDialog (getAppFrame (),
							 synGraph);
		Rectangle bounds = preferences.getBounds (PROPERTIES_DIALOG_ID);
		if (bounds != null)
		    propertiesDialog.setBounds (bounds);
	    }
	return propertiesDialog;
    }

    public synchronized PreferencesDialog getPreferencesDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (preferencesDialog == null)
	    {
		preferencesDialog = new PreferencesDialog (getAppFrame (),
							   synGraph);
		Rectangle bounds = preferences.getBounds (PREFERENCES_DIALOG_ID);
		if (bounds != null)
		    preferencesDialog.setBounds (bounds);
	    }
	return preferencesDialog;
    }

    public synchronized NoticeEditor getNoticeEditor ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (noticeEditor == null)
	    {
		noticeEditor = new NoticeEditor (getAppFrame (), synGraph);
		Rectangle bounds = preferences.getBounds (NOTICE_EDITOR_ID);
		if (bounds != null)
		    noticeEditor.setBounds (bounds);
	    }
	return noticeEditor;
    }

    public synchronized ExportDialog getNoticeExportDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (noticeExportDialog == null)
	    {
		noticeExportDialog =
		    new ExportDialog (getAppFrame (), synGraph,
				      NOTICE_EXPORT_DIALOG_ID);

		Rectangle bounds =
		    preferences.getBounds (NOTICE_EXPORT_DIALOG_ID);
		if (bounds != null)
		    noticeExportDialog.setBounds (bounds);
	    }
	return noticeExportDialog;
    }

    public synchronized PrintDialog getNoticePrintDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (noticePrintDialog == null)
	    {
		noticePrintDialog =
		    new PrintDialog (getAppFrame (), synGraph,
				     NOTICE_PRINT_DIALOG_ID);

		Rectangle bounds =
		    preferences.getBounds (NOTICE_PRINT_DIALOG_ID);
		if (bounds != null)
		    noticePrintDialog.setBounds (bounds);
	    }
	return noticePrintDialog;
    }

    public synchronized FontDialog getFontDialog ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (fontDialog == null)
	    {
		fontDialog = new FontDialog (getAppFrame (), synGraph);
		Rectangle bounds = preferences.getBounds (FONT_DIALOG_ID);
		if (bounds != null)
		    fontDialog.setBounds (bounds);
	    }
	return fontDialog;
    }

    public synchronized DocumentationDialog getDocumentationDialog ()
	throws IOException
    {
	Preferences preferences = synGraph.getPreferences ();
	if (documentationDialog == null)
	    {
		documentationDialog =
		    new DocumentationDialog (getAppFrame (),
					     bundle.getString ("resource.documentation"));
		Rectangle bounds = preferences.getBounds (DOCUMENTATION_DIALOG_ID);
		if (bounds != null)
		    documentationDialog.setBounds (bounds);
	    }
	return documentationDialog;
    }

    public synchronized LicenceDialog getLicenceDialog ()
	throws IOException
    {
	Preferences preferences = synGraph.getPreferences ();
	if (licenceDialog == null)
	    {
		licenceDialog =
		    new LicenceDialog (getAppFrame (),
				       bundle.getString ("resource.licence")
);
		Rectangle bounds = preferences.getBounds (LICENCE_DIALOG_ID);
		if (bounds != null)
		    licenceDialog.setBounds (bounds);
	    }
	return licenceDialog;
    }

    public synchronized ColorChooser getBgColorChooser ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (bgColorChooser == null)
	    {
		bgColorChooser =
		    new ColorChooser (getAppFrame (),
				      synGraph, ColorChooser.MODE.BG_COLOR);
		Rectangle bounds = preferences.getBounds (BG_COLOR_CHOOSER_ID);
		if (bounds != null)
		    bgColorChooser.setBounds (bounds);
	    }
	return bgColorChooser;
    }

    public synchronized void storeWindowBounds ()
    {
	Preferences preferences = synGraph.getPreferences ();
	if (appFrame != null)
	    preferences.putBounds (APP_FRAME_ID,
				   appFrame.getBounds ());
	if (objectPropertiesDialog != null)
	    preferences.putBounds (OBJECT_PROPERTIES_DIALOG_ID,
				   objectPropertiesDialog.getBounds ());
	if (objColorChooser != null)
	    preferences.putBounds (OBJ_COLOR_CHOOSER_ID,
				   objColorChooser.getBounds ());
	if (objTabExportDialog != null)
	    preferences.putBounds (OBJ_TAB_EXPORT_DIALOG_ID,
				   objTabExportDialog.getBounds ());
	if (objTabPrintDialog != null)
	    preferences.putBounds (OBJ_TAB_PRINT_DIALOG_ID,
				   objTabPrintDialog.getBounds ());
	if (openDialog != null)
	    preferences.putBounds (OPEN_DIALOG_ID,
				   openDialog.getBounds ());
	if (saveDialog != null)
	    preferences.putBounds (SAVE_DIALOG_ID,
				   saveDialog.getBounds ());
	if (propertiesDialog != null)
	    preferences.putBounds (PROPERTIES_DIALOG_ID,
				   propertiesDialog.getBounds ());
	if (preferencesDialog != null)
	    preferences.putBounds (PREFERENCES_DIALOG_ID,
				   preferencesDialog.getBounds ());
	if (noticeEditor != null)
	    preferences.putBounds (NOTICE_EDITOR_ID,
				   noticeEditor.getBounds ());
	if (noticeExportDialog != null)
	    preferences.putBounds (NOTICE_EXPORT_DIALOG_ID,
				   noticeExportDialog.getBounds ());
	if (noticePrintDialog != null)
	    preferences.putBounds (NOTICE_PRINT_DIALOG_ID,
				   noticePrintDialog.getBounds ());
	if (fontDialog != null)
	    preferences.putBounds (FONT_DIALOG_ID,
				   fontDialog.getBounds ());
	if (documentationDialog != null)
	    preferences.putBounds (DOCUMENTATION_DIALOG_ID,
				   documentationDialog.getBounds ());
	if (licenceDialog != null)
	    preferences.putBounds (LICENCE_DIALOG_ID,
				   licenceDialog.getBounds ());
	if (bgColorChooser != null)
	    preferences.putBounds (BG_COLOR_CHOOSER_ID,
				   bgColorChooser.getBounds ());
    }

    public OpenListener getOpenListener ()
    {
	return openListener;
    }

    public SaveListener getSaveListener ()
    {
	return saveListener;
    }

    public SaveAsListener getSaveAsListener ()
    {
	return saveAsListener;
    }

    public CloseListener getCloseListener ()
    {
	return closeListener;
    }

    public PreferencesListener getPreferencesListener ()
    {
	return preferencesListener;
    }

    public PropertiesListener getPropertiesListener ()
    {
	return propertiesListener;
    }

    public NoticeListener getNoticeListener ()
    {
	return noticeListener;
    }

    public ExitListener getExitListener ()
    {
	return exitListener;
    }

    public BlackenListener getBlackenListener ()
    {
	return blackenListener;
    }

    public FontListener getFontListener ()
    {
	return fontListener;
    }

    public BGColorListener getBGColorListener ()
    {
	return bgColorListener;
    }

    public CopyListener getCopyListener ()
    {
	return copyListener;
    }

    public DocumentationListener getDocumentationListener ()
    {
	return documentationListener;
    }

    public AboutListener getAboutListener ()
    {
	return aboutListener;
    }

    public LicenceListener getLicenceListener ()
    {
	return licenceListener;
    }

    private class OpenListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getOpenDialog ().showDialog ();
	}
    }

    /**
     * @return true if and only if the file was actually saved or no
     * save was needed because the document was not modified.
     */
    private boolean save ()
    {
	DocumentInfo documentInfo = synGraph.getDocumentInfo ();
	if (!documentInfo.hasUnsavedChanges ())
	    return true; // nothing to save
	if (documentInfo.getPath () == null)
	    // have no file path so far
	    return GUI.getInstance ().getSaveDialog ().showDialog ();
	else
	    try
		{
		    documentInfo.saveDocument ();
		    return true;
		}
	    catch (IOException e)
		{
		    JOptionPane.
			showMessageDialog (getAppFrame (),
					   e.getLocalizedMessage (),
					   bundle.getString ("frame.title.errorSave"),
					   JOptionPane.ERROR_MESSAGE);
		    return false;
		}
    }

    private class SaveListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    save ();
	}
    }

    private class SaveAsListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getSaveDialog ().showDialog ();
	}
    }

    private class CloseListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    close ();
	}
    }

    private class PreferencesListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getPreferencesDialog ().showDialog ();
	}
    }

    private class PropertiesListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getPropertiesDialog ().showDialog ();
	}
    }

    private class NoticeListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getNoticeEditor ().showDialog ();
	}
    }

    private void doClose ()
    {
	DocumentInfo documentInfo = synGraph.getDocumentInfo ();
	documentInfo.removeDocument ();
    }

    /**
     * @return @code{true}, if the document has actually been closed.
     * Returns @code{false} if no action has been performed
     * (e.g. because the user has cancelled the action due to pending
     * unsaved changes).
     */
    private boolean close ()
    {
	DocumentInfo documentInfo = synGraph.getDocumentInfo ();
	if (!documentInfo.hasUnsavedChanges ())
	    {
		doClose ();
		return true;
	    }
	else
	    {
		int result =
		    JOptionPane.
		    showInternalConfirmDialog(getAppFrame ().getJMenuBar (),
					      bundle.getString ("label.text.querySaveChanges"),
					      bundle.getString ("frame.title.confirm"),
					      JOptionPane.YES_NO_CANCEL_OPTION,
					      JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.YES_OPTION)
		    {
			if (save ())
			    {
				doClose ();
				return true;
			    }
			else
			    return false;
		    }
		else if (result == JOptionPane.NO_OPTION)
		    {
			doClose ();
			return true;
		    }
		else if (result == JOptionPane.CANCEL_OPTION)
		    {
			return false;
		    }
		else
		    {
			assert (false) : "unexpected option value";
			return false;
		    }
	    }
    }

    private class ExitListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    if (close ())
		synGraph.exitApp ();
	}
    }

    private class BGColorListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    Color newColor =
		GUI.getInstance ().getBgColorChooser ().
		showDialog (bundle.getString ("frame.title.selectBgColor"),
			    null,
			    null,
			    synGraph.getDocument ().getBGColor ());
	    if (newColor != null)
		synGraph.getDocument ().setBGColor (newColor);
	}
    }

    private class CopyListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    Object source = e.getSource ();
	    assert source instanceof MenuBar.CopyItem :
		"bad object type of event source: " + source;
	    MenuBar.CopyItem copyItem = (MenuBar.CopyItem)source;
	    int result =
		JOptionPane.
		showConfirmDialog (getAppFrame (),
				   bundle.getString ("label.text.queryCopy",
						     copyItem.getFunctionLabel ()),
				   bundle.getString ("frame.title.confirm"),
				   JOptionPane.YES_NO_OPTION);
	    if (result == JOptionPane.YES_OPTION)
		synGraph.getDocument ().
		    copyObjectColors (copyItem.getFunctionID ());
	}
    }

    private class BlackenListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    int result =
		JOptionPane.
		showConfirmDialog (getAppFrame (),
				   bundle.getString ("label.text.queryBlacken"),
				   bundle.getString ("frame.title.confirm"),
				   JOptionPane.YES_NO_OPTION);
	    if (result == JOptionPane.YES_OPTION)
		synGraph.getDocument ().resetAllObjectColors ();
	}
    }

    private class FontListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getFontDialog ().showDialog ();
	}
    }

    private class DocumentationListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    try
		{
		    GUI.getInstance ().getDocumentationDialog ().showDialog ();
		}
	    catch (IOException e)
		{
		    JOptionPane.
			showMessageDialog (getAppFrame (),
					   e.getLocalizedMessage (),
					   bundle.getString ("frame.title.errorIO"),
					   JOptionPane.ERROR_MESSAGE);
		}
	}
    }
    private class AboutListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    JOptionPane.
		showMessageDialog (getAppFrame (),
				   bundle.getString ("label.text.about"),
				   bundle.getString ("frame.title.about"),
				   JOptionPane.INFORMATION_MESSAGE);
	}
    }

    private class LicenceListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    try
		{
		    GUI.getInstance ().getLicenceDialog ().showDialog ();
		}
	    catch (IOException e)
		{
		    JOptionPane.
			showMessageDialog (getAppFrame (),
					   e.getLocalizedMessage (),
					   bundle.getString ("frame.title.errorIO"),
					   JOptionPane.ERROR_MESSAGE);
		}
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
