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
 * $Id: FontDialog.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A dialog for choosing a font.
 *
 * @author Jürgen Reuter
 */
public class FontDialog extends JDialog
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    private final static Dimension PREFERRED_FONTLIST_SIZE =
	new Dimension (320, 240);
    private final static Dimension PREFERRED_SIZELIST_SIZE =
	new Dimension (20, 50);

    private Main synGraph;

    /*
    private String[] styles =
    {
	"regulär", "fett", "schräg gestellt"
    };
    */

    private ProtoFont[] protoFonts;
    private JList fontList;
    private JList sizeList;
    private PreviewPanel previewPanel;

    private final static int[] SIZES =
    {
	6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 42, 44, 48, 52
    };

    // point size (float/int)
    // logical name (String)
    // Font styles: PLAIN, BOLD, ITALIC
    // Baselines: ROMAN_BASELINE, CENTER_BASELINE, HANGING_BASELINE
    // Font types: TRUETYPE_FONT, TYPE1_FONT

    private FontDialog () {}

    FontDialog (Frame owner, Main synGraph)
    {
	super (owner);
	this.synGraph = synGraph;
	/*
	GraphicsEnvironment graphicsEnvironment =
	    GraphicsEnvironment.getLocalGraphicsEnvironment ();
	fontFamilyNames = graphicsEnvironment.getAvailableFontFamilyNames ();
	*/

	setModal (true);
	setTitle (bundle.getString ("frame.title.font"));
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
	JPanel fontProps = new JPanel ();
	fontProps.
	    setLayout (new BoxLayout (fontProps, BoxLayout.X_AXIS));
	fontProps.add (new FontPane ());
	fontProps.add (new SizePane ());
	getContentPane ().add (fontProps);
	previewPanel = new PreviewPanel ();
	getContentPane ().add (previewPanel);
	getContentPane ().add (new ButtonRow ());
	loadInputFields ();
	pack ();
    }

    private class ProtoFont
    {
	private Font font;

	ProtoFont (Font font)
	{
	    this.font = font;
	}

	Font getFont () { return font; }

	public String toString ()
	{
	    return font.getName ();
	}
    }

    private class PreviewPanel extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	private NoticeEditor.NoticePane preview;

	PreviewPanel ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.preview"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.preview"));
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    preview = new NoticeEditor.NoticePane (synGraph,
						   "AaBbCcDdEeFfGg01234");
	    preview.
		setToolTipText (bundle.
				getString ("textfield.tooltip.previewFont"));
	    label.setLabelFor (preview);
	    add (preview);
	}

	void fontChanged (Font font)
	{
	    preview.fontChanged (font);
	}
    }

    /**
     * The "Lohit Bengali" font is buggy and may cause
     * sun.font.TrueTypeFont.createScaler(Native Method) to hang.
     * Therefore, we deliberately remove this font from the list of
     * selectable fonts.  See
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6205321 for
     * details about the bug.
     */
    private static Font[] saneFonts (Font[] fonts)
    {
	Vector<Font> cleanFonts = new Vector<Font> ();
	for (int i = 0; i < fonts.length; i++)
	    {
		Font font = fonts[i];
		if (!font.getName ().equals ("Lohit Bengali"))
		    cleanFonts.add (font);
	    }
	return cleanFonts.toArray (new Font[0]);
    }

    private class FontPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	FontPane ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.fontName"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.fontName"));
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    GraphicsEnvironment graphicsEnvironment =
		GraphicsEnvironment.getLocalGraphicsEnvironment ();
	    Font[] allFonts = saneFonts (graphicsEnvironment.getAllFonts ());
	    protoFonts = new ProtoFont[allFonts.length];
	    for (int i = 0; i < allFonts.length; i++)
		protoFonts[i] = new ProtoFont (allFonts[i]);
	    fontList = new JList (protoFonts);
	    fontList.
		setToolTipText (bundle.
				getString ("list.tooltip.fontName"));
	    label.setLabelFor (fontList);
	    fontList.setSelectionMode (ListSelectionModel.
				       SINGLE_SELECTION);
	    fontList.setLayoutOrientation (JList.VERTICAL);
	    fontList.setVisibleRowCount (-1);
	    fontList.addListSelectionListener (new FontChangeListener ());
	    JScrollPane listScroller = new JScrollPane (fontList);
	    listScroller.setPreferredSize (PREFERRED_FONTLIST_SIZE);
	    add (listScroller);
	}
    }

    private class SizePane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	SizePane ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	    JPanel labelPane = new JPanel ();
	    labelPane.setLayout (new BoxLayout (labelPane, BoxLayout.X_AXIS));
	    JLabel label =
		new JLabel (bundle.getString ("label.text.fontSize"));
	    label.setDisplayedMnemonic (bundle.getKeyEvent ("label.mnemonic.fontSize"));
	    labelPane.add (label);
	    labelPane.add (Box.createHorizontalGlue ());
	    add (labelPane);
	    Integer[] sizes = new Integer[SIZES.length];
	    for (int i = 0; i < SIZES.length; i++)
		sizes[i] = new Integer (SIZES[i]);
	    sizeList = new JList (sizes);
	    sizeList.
		setToolTipText (bundle.
				getString ("list.tooltip.fontSize"));
	    label.setLabelFor (sizeList);
	    sizeList.setSelectionMode (ListSelectionModel.
				       SINGLE_SELECTION);
	    sizeList.setLayoutOrientation (JList.VERTICAL);
	    sizeList.setVisibleRowCount (-1);
	    sizeList.addListSelectionListener (new FontChangeListener ());
	    JScrollPane listScroller = new JScrollPane (sizeList);
	    listScroller.setPreferredSize (PREFERRED_SIZELIST_SIZE);
	    add (listScroller);
	}
    }

    private class FontChangeListener implements ListSelectionListener
    {
	public void valueChanged (ListSelectionEvent e)
	{
	    if (previewPanel == null) // not yet initialized
		return;
	    ProtoFont protoFont = (ProtoFont)(fontList.getSelectedValue ());
	    if (protoFont == null) // no font selected => no change
		return;
	    Integer size = (Integer)(sizeList.getSelectedValue ());
	    Font font =
		protoFont.getFont ().deriveFont ((float)size.intValue ());
	    previewPanel.fontChanged (font);
	}
    }

    private class ButtonRow extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public ButtonRow ()
	{
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JButton buttonCancel =
		new JButton (bundle.getString ("button.text.cancel"));
	    buttonCancel.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.cancel"));
	    buttonCancel.
		setToolTipText (bundle.
				getString ("button.tooltip.cancelFontDialog"));
	    buttonCancel.addActionListener (new ButtonCancelListener ());
	    add (buttonCancel);
	    add (Box.createHorizontalGlue ());
	    JButton buttonOk =
		new JButton (bundle.getString ("button.text.approve"));
	    buttonOk.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.approve"));
	    buttonOk.
		setToolTipText (bundle.
				getString ("button.tooltip.closeFontDialog"));
	    buttonOk.addActionListener (new ButtonOkListener ());
	    add (buttonOk);
	}
    }

    private class ButtonCancelListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    setVisible (false);
	}
    }

    private class ButtonOkListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    saveInputFields ();
	    setVisible (false);
	}
    }

    private void saveInputFields ()
    {
	ProtoFont protoFont = (ProtoFont)(fontList.getSelectedValue ());
	if (protoFont == null)
	    {
		JOptionPane.
		    showMessageDialog (FontDialog.this,
				       bundle.getString ("label.text.error.noFontSelected"),
				       bundle.getString ("frame.title.errorInvalidfont"),
				       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	Integer size = (Integer)(sizeList.getSelectedValue ());
	Font font =
	    protoFont.getFont ().deriveFont ((float)size.intValue ());
	synGraph.getDocument ().setFont (font);
    }

    private void loadInputFields ()
    {
	Font font = synGraph.getDocument ().getFont ();
	sizeList.setSelectedValue (new Integer (font.getSize ()), true);
	ProtoFont protoFont = null;
	for (int i = 0; i < protoFonts.length; i++)
	    {
		if (protoFonts[i].toString ().equals (font.getFontName ()))
		    {
			protoFont = protoFonts[i];
			break;
		    }
	    }
	if (protoFont == null)
	    protoFont = protoFonts[0];
	fontList.setSelectedValue (protoFont, true);
    }

    void showDialog ()
    {
	loadInputFields ();
	setVisible (true);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
