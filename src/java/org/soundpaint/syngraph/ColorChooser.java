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
 * $Id: ColorChooser.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
//import java.awt.Component;// TODO
//import java.awt.Window;// TODO
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ComponentAdapter;// TODO
//import java.awt.event.ComponentEvent;// TODO
//import java.awt.event.WindowAdapter;// TODO
//import java.awt.event.WindowEvent;// TODO
//import java.io.Serializable;// TODO
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A color chooser dialog like javax.swing.JColorChooser, but with a
 * customized object preview panel and internationalized buttons.
 *
 * @author Jürgen Reuter
 */
public class ColorChooser extends JDialog
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();

    private Main synGraph;
    private MODE mode;
    private Color initialColor;
    private JColorChooser chooserPane;
    private JLabel label;
    private JPanel previewPane;
    private boolean approved;

    private ColorChooser () {}

    public enum MODE { BG_COLOR, OBJ_COLOR };

    ColorChooser (Frame owner, Main synGraph, MODE mode)
    {
	super (owner);
	this.synGraph = synGraph;
	this.mode = mode;
	setModal (true);
	setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
	chooserPane = new JColorChooser (Color.white);
        chooserPane.setPreviewPanel (null);
	chooserPane.getSelectionModel ().
	    addChangeListener (new ColorChangeListener ());
	chooserPane.setPreviewPanel (new JPanel ()); // hide default preview

	getContentPane ().
	    setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
        getContentPane ().add (chooserPane);
	switch (mode)
	    {
	    case BG_COLOR:
		getContentPane ().add (new BackgroundPreviewPane ());
		break;
	    case OBJ_COLOR:
		getContentPane ().add (new ObjectPreviewPane ());
		break;
	    default:
	    }
	getContentPane ().add (new ButtonRow ());
    }

    private class BackgroundPreviewPane extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public BackgroundPreviewPane ()
	{
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.preview")));
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    previewPane = new JPanel ();
	    previewPane.setLayout (new BoxLayout (previewPane,
						  BoxLayout.X_AXIS));
	    previewPane.add (Box.createHorizontalGlue ());
	    JPanel previewSubPane = new JPanel ();
	    previewSubPane.setLayout (new BoxLayout (previewSubPane,
						     BoxLayout.Y_AXIS));
	    previewSubPane.add (Box.createVerticalGlue ());
	    previewPane.add (previewSubPane);
	    previewPane.add (Box.createHorizontalGlue ());
	    previewPane.setOpaque (true);
	    previewPane.setPreferredSize (new Dimension (100, 100));
	    add (previewPane);
	}
    }

    private class ObjectPreviewPane
	extends JPanel implements DocumentChangeListener
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public ObjectPreviewPane ()
	{
	    synGraph.addDocumentChangeListener (this);
	    setBorder (BorderFactory.
		       createTitledBorder (bundle.getString ("border.text.preview")));
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    previewPane = new JPanel ();
	    previewPane.
		setLayout (new BoxLayout (previewPane, BoxLayout.X_AXIS));
	    label = new JLabel ();
	    previewPane.add (Box.createHorizontalGlue ());
	    previewPane.add (label);
	    previewPane.add (Box.createHorizontalGlue ());
	    previewPane.setOpaque (true);
	    add (previewPane);
	}

	public void titleChanged (String title) {}
	public void authorChanged (String author) {}
	public void commentChanged (String comment) {}
	public void objectLabelChanged (String objectID) {}
	public void objectCommentChanged (String objectID) {}
	public void objectColorChanged (String objectID, Color color) {}

	public void bgColorChanged (Color color)
	{
	    previewPane.setBackground (color);
	}

	public void fontChanged (Font font) {}
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
	public void setAdded (String setID) {}
	public void setRemoved (String setID) {}
	public void setLabelChanged (String setID) {}
	public void setCommentChanged (String setID) {}
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
				getString ("button.tooltip.cancelColor"));
	    buttonCancel.addActionListener (new ButtonCancelListener ());
	    add (buttonCancel);
	    add (Box.createHorizontalGlue ());
	    JButton buttonOk =
		new JButton (bundle.getString ("button.text.approve"));
	    buttonOk.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.approve"));
	    buttonOk.
		setToolTipText (bundle.
				getString ("button.tooltip.approveColor"));
	    buttonOk.addActionListener (new ButtonOkListener ());
	    add (buttonOk);
	    add (Box.createHorizontalGlue ());
	    JButton buttonReset =
		new JButton (bundle.getString ("button.text.reset"));
	    buttonReset.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.reset"));
	    buttonReset.
		setToolTipText (bundle.
				getString ("button.tooltip.resetColor"));
	    buttonReset.addActionListener (new ButtonResetListener ());
	    add (buttonReset);
	}
    }

    // TODO:
    /*
	// The following few lines are used to register esc to close
	// the dialog
	Action cancelKeyAction = new AbstractAction ()
	    {
		public void actionPerformed (ActionEvent event)
		{
		    ((AbstractButton)event.getSource ()).
			fireActionPerformed (event);
		}
	    }; 
	KeyStroke cancelKeyStroke =
	    KeyStroke.getKeyStroke ((char)KeyEvent.VK_ESCAPE, false);
	InputMap inputMap =
	    cancelButton.getInputMap (JComponent.
				      WHEN_IN_FOCUSED_WINDOW);
	ActionMap actionMap = cancelButton.getActionMap ();
	if ((inputMap != null) && (actionMap != null))
	    {
		inputMap.put (cancelKeyStroke, "cancel");
		actionMap.put ("cancel", cancelKeyAction);
	    }
	// end esc handling
	*/

    private class ColorChangeListener implements ChangeListener
    {
	public void stateChanged (ChangeEvent event)
	{
	    switch (mode)
		{
		case BG_COLOR:
		    previewPane.setBackground (chooserPane.getColor ());
		    break;
		case OBJ_COLOR:
		    label.setForeground (chooserPane.getColor ());
		    break;
		default:
		}
	}
    }

    private class ButtonCancelListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    approved = false;
	    setVisible (false);
	}
    }

    private class ButtonOkListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    approved = true;
	    saveInputFields ();
	    setVisible (false);
	}
    }

    private class ButtonResetListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    chooserPane.setColor (initialColor);
	}
    }

    // TODO
    /*
    private static class Closer
	extends WindowAdapter implements Serializable
    {
        public void windowClosing (WindowEvent event)
	{
            cancelButton.doClick ();
            Window window = event.getWindow ();
            window.hide ();
        }
    }

    private static class DisposeOnClose
	extends ComponentAdapter implements Serializable
    {
        public void componentHidden(ComponentEvent event)
	{
            Window window = (Window)event.getComponent ();
            window.dispose ();
        }
    }
    */

    private void saveInputFields ()
    {
	// no input fields so far
    }

    private void loadInputFields ()
    {
	// no input fields so far
    }

    /**
     * Returns the chosen color, or <code>null</code>, if and only if
     * the color choice has been cancelled.
     */
    public Color showDialog (String title, String label, Font font,
			     Color initialColor)
    {
	setTitle (title);
        this.initialColor = initialColor;
	chooserPane.setColor (initialColor);
	previewPane.
	    setBackground (synGraph.getDocument ().getBGColor ());
	switch (mode)
	    {
	    case BG_COLOR:
		break;
	    case OBJ_COLOR:
		assert (label != null);
		this.label.setText (label);
		this.label.setFont (font != null ? font : Font.decode (null));
		break;
	    default:
	    }
	loadInputFields ();
	pack ();
	setVisible (true);
	return approved ? chooserPane.getColor () : null;
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
