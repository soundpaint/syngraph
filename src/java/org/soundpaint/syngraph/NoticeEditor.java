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
 * $Date: 2008-01-04 04:19:55 +0100 (Fri, 04 Jan 2008) $
 * $Id: NoticeEditor.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

class NoticeEditor extends JDialog
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private final static Dimension PREFERRED_PANE_SIZE =
	new Dimension (480, 320);

    private Main synGraph;
    private NoticePane noticePane;

    private NoticeEditor () {}

    NoticeEditor (Frame owner, Main synGraph)
    {
	super (owner);
	this.synGraph = synGraph;
	setModal (false);
	setTitle (bundle.getString ("frame.title.noticeEditor"));
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	noticePane = new NoticePane (synGraph);
	JScrollPane scroller = new JScrollPane ();
	scroller.setPreferredSize (PREFERRED_PANE_SIZE);
	JViewport viewport = scroller.getViewport ();
	viewport.add (noticePane);
	viewport.setScrollMode (JViewport.BACKINGSTORE_SCROLL_MODE);
	getContentPane ().add (scroller);
	getContentPane ().add (new OptionsPanel ());
	getContentPane ().add (new ButtonRow ());
	pack ();
    }

    static class NoticePane extends JTextPane
	implements DocumentListener, DocumentChangeListener
    {
	private final static long serialVersionUID = 0000000000000000000L;
	private final static String NOTICE_PAD_SYMBOL_SET_ID =
	    "notice-pad-symbol";

	private Main synGraph;
	private DefaultStyledDocument content;
	private boolean isCryptic;

	public void titleChanged (String title) {}
	public void authorChanged (String author) {}
	public void commentChanged (String comment) {}

	public void objectLabelChanged (String objectID)
	{
	    SwingUtilities.
		invokeLater (new UpdateText (0, content.getLength ()));
	}

	public void objectCommentChanged (String objectID) {}

	public void objectColorChanged (String objectID, Color color)
	{
	    SwingUtilities.
		invokeLater (new UpdateText (0, content.getLength ()));
	}

	public void bgColorChanged (Color color)
	{
	    setBackground (color);
	}

	public void fontChanged (Font font)
	{
	    SwingUtilities.invokeLater (new UpdateFont (font));
	}

	public void tabAdded (String tabID) {}
	public void tabRemoved (String tabID) {}
	public void tabLabelChanged (String tabID) {}
	public void tabCommentChanged (String tabID) {}
	public void tabObjectAlignChanged (String tabID, String objectID,
					   float x, float y) {}
	public void selectedTabChanged (String tabID) {}
	public void functionAdded (String functionID) {}
	public void functionRemoved (String functionID) {}
	public void functionLabelChanged (String functionID) {}
	public void functionCommentChanged (String functionID) {}
	public void setAdded (String setID)
	{
	    SwingUtilities.
		invokeLater (new UpdateText (0, content.getLength ()));
	}

	public void setRemoved (String setID)
	{
	    SwingUtilities.
		invokeLater (new UpdateText (0, content.getLength ()));
	}

	public void setLabelChanged (String setID) {}
	public void setCommentChanged (String setID) {}

	public void insertUpdate (DocumentEvent e)
	{
	    SwingUtilities.
		invokeLater (new UpdateText (e.getOffset (), e.getLength ()));
	}

	public void removeUpdate (DocumentEvent e) {}

	public void changedUpdate (DocumentEvent e) {}

	private Font font;

	private class UpdateFont implements Runnable
	{

	    UpdateFont (Font font)
	    {
		NoticePane.this.font = font;
	    }

	    public void run ()
	    {
		String text;
		try
		    {
			text = content.getText (0, content.getLength ());
		    }
		catch (BadLocationException e)
		    {
			assert (false);
			return; // abort update
		    }
		String fontFamily = font.getName ();
		int fontSize = font.getSize ();
		for (int pos = 0; pos < text.length (); pos++)
		    {
			SimpleAttributeSet attributes =
			    new SimpleAttributeSet ();
			StyleConstants.setFontFamily (attributes, fontFamily);
			StyleConstants.setFontSize (attributes, fontSize);
			content.setCharacterAttributes (pos, 1,
							attributes, false);
		    }
	    }
	}

	private class UpdateText implements Runnable
	{
	    private int offset, length;

	    UpdateText (int offset, int length)
	    {
		this.offset = offset;
		this.length = length;
	    }

	    public void run ()
	    {
		String text;
		try
		    {
			text = content.getText (offset, length);
		    }
		catch (BadLocationException e)
		    {
			assert false : "bad location: " + e;
			return; // abort update
		    }
		String fontFamily = font.getName ();
		int fontSize = font.getSize ();
		Document document = synGraph.getDocument ();
		Color bgColor = (document != null) ?
		    document.getBGColor () : Document.DEFAULT_BG_COLOR;
		for (int pos = offset; pos < offset + length; pos++)
		    {
			Character ch =
			    Character.valueOf (text.charAt (pos - offset));
			Color fgColor = Document.DEFAULT_FG_COLOR;
			if (document != null)
			    {
				String objectID =
				    document.
				    getSetObjectIDByLabel (NOTICE_PAD_SYMBOL_SET_ID,
							   "" + ch);
				if (objectID != null)
				    fgColor =
					document.getObjectColor (objectID);
			    }
			SimpleAttributeSet attributes =
			    new SimpleAttributeSet ();
			StyleConstants.setForeground (attributes, fgColor);
			StyleConstants.setBackground (attributes,
						      isCryptic () ?
						      fgColor : bgColor);
			StyleConstants.setFontFamily (attributes, fontFamily);
			StyleConstants.setFontSize (attributes, fontSize);
			content.setCharacterAttributes (pos, 1,
							attributes, false);
		    }
	    }
	}

	private NoticePane () {}

	NoticePane (Main synGraph)
	{
	    this (synGraph,
		  bundle.getString ("textarea.text.noticeEditorInstruction"));
	}

	NoticePane (Main synGraph, String initialText)
	{
	    this.synGraph = synGraph;
	    isCryptic = false;
	    synGraph.addDocumentChangeListener (this);
	    content = new DefaultStyledDocument ();
	    try
		{
		    content.insertString (0,
					  initialText,
					  new SimpleAttributeSet ());
		}
	    catch (BadLocationException e)
		{
		    assert (false);
		}
	    content.addDocumentListener (this);
	    setDocument (content);
	    setBackground (synGraph.getDocument ().getBGColor ());
	    SwingUtilities.
		invokeLater (new UpdateFont (synGraph.getDocument ().
					     getFont ()));
	    SwingUtilities.
		invokeLater (new UpdateText (0, content.getLength ()));
	}

	void setCryptic (boolean isCryptic)
	{
	    this.isCryptic = isCryptic;
	    SwingUtilities.
		invokeLater (new UpdateText (0, content.getLength ()));
	}

	boolean isCryptic ()
	{
	    return isCryptic;
	}
    }

    JCheckBox crypticCheck;

    private class OptionsPanel extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public OptionsPanel ()
	{
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.cryptify")));
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.isCrypticCheck"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.isCrypticCheck"));
	    crypticCheck = new JCheckBox ();
	    label.setLabelFor (crypticCheck);
	    crypticCheck.setToolTipText (bundle.
					 getString ("checkbox.tooltip.isCrypticCheck"));
	    crypticCheck.addActionListener (new CrypticCheckListener ());
	    add (crypticCheck);
	    add (label);
	    add (Box.createHorizontalGlue ());
	}
    }

    private class CrypticCheckListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    noticePane.setCryptic (crypticCheck.isSelected ());
	}
    }

    private class ButtonRow extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public ButtonRow ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JButton printButton =
		new JButton (bundle.getString ("button.text.print"));
	    printButton.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.print"));
	    printButton.
		setToolTipText (bundle.
				getString ("button.tooltip.printNotice"));
	    printButton.addActionListener (new PrintButtonListener ());
	    add (printButton);
	    add (Box.createHorizontalGlue ());
	    JButton exportButton =
		new JButton (bundle.getString ("button.text.export"));
	    exportButton.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.export"));
	    exportButton.
		setToolTipText (bundle.
				getString ("button.tooltip.exportNotice"));
	    exportButton.addActionListener (new ExportButtonListener ());
	    add (exportButton);
	    add (Box.createHorizontalGlue ());
	    JButton okButton =
		new JButton (bundle.getString ("button.text.close"));
	    okButton.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.close"));
	    okButton.
		setToolTipText (bundle.
				getString ("button.tooltip.closeNotice"));
	    okButton.addActionListener (new ButtonOkListener ());
	    add (okButton);
	}
    }

    private class PrintButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    GUI.getInstance ().getNoticePrintDialog ().
		showDialog (noticePane);
	}
    }

    private class ExportButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    try
		{
		    GUI.getInstance ().getNoticeExportDialog ().
			showDialog (noticePane);
		}
	    catch (UnsupportedOperationException e)
		{
		    JOptionPane.
			showMessageDialog (NoticeEditor.this,
					   bundle.getString ("label.text.errorImgNoDriver"),
					   bundle.getString ("frame.title.errorImgWrite"),
					   JOptionPane.ERROR_MESSAGE);
		}
	}
    }

    private class ButtonOkListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    setVisible (false);
	}
    }

    void showDialog ()
    {
	setVisible (true);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
