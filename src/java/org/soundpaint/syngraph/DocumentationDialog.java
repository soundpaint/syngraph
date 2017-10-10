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
 * $Id: DocumentationDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

/**
 * A dialog for browsing the html-based user manual.
 *
 * @author Jürgen Reuter
 */
public class DocumentationDialog extends JDialog
{
    /**
     * N.B. Small parts of this code have been derived from examples
     * in the Java tutorial.
     */

    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    private final static Dimension PREFERRED_PANE_SIZE =
	new Dimension (720, 560);
    private final static String PROTOCOL_JAR = "jar";

    private JEditorPane editorPane;
    private JPanel buttonRow;

    private DocumentationDialog () {}

    DocumentationDialog (Frame owner, String path) throws IOException
    {
	super (owner);
	setModal (false);
	setTitle (bundle.getString ("frame.title.documentation"));
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	URL url = DocumentationDialog.class.getResource (path);
	editorPane = new JEditorPane (url);
	editorPane.setEditable (false);
	editorPane.addHyperlinkListener (new LinkListener ());
	JScrollPane scroller = new JScrollPane ();
	scroller.setPreferredSize (PREFERRED_PANE_SIZE);
	scroller.setBorder (new BevelBorder (BevelBorder.LOWERED));
	JViewport viewport = scroller.getViewport ();
	viewport.add (editorPane);
	viewport.setScrollMode (JViewport.BACKINGSTORE_SCROLL_MODE);
	getContentPane ().add (scroller);
	getContentPane ().add (Box.createVerticalGlue ());
	buttonRow = new ButtonRow ();
	getContentPane ().add (buttonRow);
	pack ();
    }

    public void showDialog ()
    {
	setVisible (true);
    }

    private class ButtonRow extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;
	public ButtonRow ()
	{
	    add (Box.createHorizontalGlue ());
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JButton buttonOk =
		new JButton (bundle.getString ("button.text.close"));
	    buttonOk.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.close"));
	    buttonOk.
		setToolTipText (bundle.
				getString ("button.tooltip.closeDoc"));
	    buttonOk.addActionListener (new ButtonOkListener ());
	    add (buttonOk);
	    add (Box.createHorizontalGlue ());
	}
    }

    private class ButtonOkListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    setVisible (false);
	}
    }

    private class LinkListener implements HyperlinkListener
    {
	public void hyperlinkUpdate(HyperlinkEvent e)
	{
	    if (e.getEventType () == HyperlinkEvent.EventType.ACTIVATED)
		followLink (e.getURL ());
	}
    }

    /**
     * Follows the reference in an link.  The given url is the
     * requested reference.  By default this calls <a
     * href="#setPage">setPage</a>, and if an exception is thrown the
     * original previous document is restored and a beep sounded.  If
     * an attempt was made to follow a link, but it represented a
     * malformed url, this method will be called with a null argument.
     *
     * @param url The URL to follow.
     */
    protected void followLink(URL url)
    {
	String protocol = url.getProtocol ();
	assert protocol != null : "invalid URL in documentation";
	if (protocol == null)
	    /*
	     * Invalid URL in the documentation of this application.
	     * Silently ignore the URL and return without further
	     * action.
	     */
	    return;
	else if (!protocol.equals (PROTOCOL_JAR))
	    /*
	     * The URL represents an external link that references a
	     * page outside the documentation that is included in this
	     * application.  Do not follow external links; just return
	     * without further action.
	     *
	     * TODO: Actually, we should here start an external
	     * browser application like firefox or explorer and pass
	     * the url to this browser.
	     */
	    return;
	else
	    /*
	     * The URL represents an internal link that references a
	     * page within the documentation that is included in this
	     * application.
	     */
	    {
		Cursor cursor = editorPane.getCursor();
		Cursor waitCursor =
		    Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		editorPane.setCursor(waitCursor);
		SwingUtilities.invokeLater (new PageLoader (url, cursor));
	    }
    }

    /**
     * A temporary class that loads synchronously (although later than
     * the request so that a cursor change can be done).
     */
    private class PageLoader implements Runnable
    {
	URL url;
	Cursor cursor;

	PageLoader (URL url, Cursor cursor)
	{
	    this.url = url;
	    this.cursor = cursor;
	}

	public void run ()
	{
	    if (url == null)
		{
		    // restore the original cursor
		    editorPane.setCursor (cursor);
		}
	    else
		{
		    Document document = editorPane.getDocument ();
		    try
			{
			    editorPane.setPage (url);
			}
		    catch (IOException e)
			{
			    // restore previous contents
			    editorPane.setDocument (document);

			    // show error
			    JOptionPane.
				showMessageDialog (DocumentationDialog.this,
						   e.getLocalizedMessage (),
						   bundle.getString ("frame.title.errorIO"),
						   JOptionPane.ERROR_MESSAGE);
			}
		    finally
			{
			    // schedule the cursor to revert after
			    // the paint has happened.
			    url = null;
			    SwingUtilities.invokeLater (this);
			}
		}
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
