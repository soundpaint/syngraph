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
 * $Id: I18NPropertyEditor.java 307 2008-01-03 07:28:53Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * An editor panel for editing object property strings with i18n
 * support.  This panel is used by property editor dialogs.
 *
 * @author Jürgen Reuter
 */
public class I18NPropertyEditor extends JPanel
{
    private final static long serialVersionUID = 0000000000000000000L;
    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    private final static Dimension PREFERRED_TABLE_SIZE =
	new Dimension (280, 120);

    private JTable propTable;

    private final static int PROP_VALUE_COLUMN_INDEX = 0;
    private final static int LOCALE_COLUMN_INDEX = 1;
    private final static String INITIAL_PROP_VALUE = "";

    private static class PropertyTableModel extends DefaultTableModel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public PropertyTableModel (Object data[][], Object columnNames[])
	{
	    super (data, columnNames);
	}

	public boolean isCellEditable (int row, int column)
	{
	    return column == PROP_VALUE_COLUMN_INDEX;
	}
    }

    I18NPropertyEditor (String labelText, int mnemonic, String tooltipText)
    {
	setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
	setBorder (new TitledBorder (new BevelBorder (BevelBorder.LOWERED),
				     labelText));
	String columnNames[] = new String[2];
	columnNames[PROP_VALUE_COLUMN_INDEX] =
	    bundle.getString ("label.text.value");
	columnNames[LOCALE_COLUMN_INDEX] =
	    bundle.getString ("label.text.locale");
	String data[][] = {};
	TableModel model = new PropertyTableModel (data, columnNames);
	propTable = new JTable (model);
	TableColumn localeColumn =
	    propTable.getColumnModel ().getColumn (LOCALE_COLUMN_INDEX);
	localeColumn.setPreferredWidth (0);
	propTable.setToolTipText (tooltipText);
	JScrollPane scrollPane = new JScrollPane (propTable);
	propTable.setPreferredScrollableViewportSize (PREFERRED_TABLE_SIZE);
	add (scrollPane);
	add (new ButtonRow (labelText));
    }

    private class ButtonRow extends JPanel
    {
	private final static long serialVersionUID = 0000000000000000000L;

	public ButtonRow (String labelText)
	{
	    setLayout (new BoxLayout (this, BoxLayout.X_AXIS));
	    JButton buttonAdd =
		new JButton (bundle.getString ("button.text.add"));
	    buttonAdd.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.add"));
	    buttonAdd.
		setToolTipText (bundle.
				getString ("button.tooltip.addProperty",
					   labelText));
	    buttonAdd.addActionListener (new ButtonAddListener ());
	    add (buttonAdd);
	    add (Box.createHorizontalGlue ());
	    JButton buttonDelete =
		new JButton (bundle.getString ("button.text.delete"));
	    buttonDelete.
		setMnemonic (bundle.getKeyEvent ("button.mnemonic.delete"));
	    buttonDelete.
		setToolTipText (bundle.
				getString ("button.tooltip.deleteProperty"));
	    buttonDelete.addActionListener (new ButtonDeleteListener ());
	    add (buttonDelete);
	}
    }

    private final static Locale[] LOCALES =
    {
	Locale.ENGLISH,
	Locale.FRENCH,
	Locale.GERMAN,
	Locale.ITALIAN,
	Locale.JAPANESE,
	Locale.KOREAN,
	Locale.CHINESE,
	Locale.SIMPLIFIED_CHINESE,
	Locale.TRADITIONAL_CHINESE,
	Locale.FRANCE,
	Locale.GERMANY,
	Locale.ITALY,
	Locale.JAPAN,
	Locale.KOREA,
	Locale.CHINA,
	Locale.PRC,
	Locale.TAIWAN,
	Locale.UK,
	Locale.US,
	Locale.CANADA,
	Locale.CANADA_FRENCH,
    };

    private static class LocaleComparator implements Comparator<Locale>
    {
	public int compare (Locale locale1, Locale locale2)
	{
	    if (locale1 == null)
		if (locale2 == null)
		    return 0;
		else
		    return 1;
	    else if (locale2 == null)
		return -1;
	    else
		return locale1.toString ().compareTo (locale2.toString ());
	}

	public boolean equals (Object other)
	{
	    return other instanceof LocaleComparator;
	}
    }

    private final static LocaleComparator LOCALE_COMPARATOR =
	new LocaleComparator ();

    private Locale[] getPropertyLocales (DefaultTableModel table)
    {
	Locale locales[] = new Locale[table.getRowCount ()];
	for (int row = 0; row < locales.length; row++)
	    {
		locales[row] =
		    (Locale)table.getValueAt (row, LOCALE_COLUMN_INDEX);
	    }
	return locales;
    }

    Locale[] getPropertyLocales ()
    {
	DefaultTableModel table = (DefaultTableModel)propTable.getModel ();
	return getPropertyLocales (table);
    }

    String getValue (Locale locale)
    {
	// TODO
	return null;
    }

    private int searchLocale (DefaultTableModel table, Locale locale)
    {
	Locale locales[] = getPropertyLocales (table);
	return
	    Arrays.binarySearch (locales, locale, LOCALE_COMPARATOR);
    }

    public void clear ()
    {
	DefaultTableModel table = (DefaultTableModel)propTable.getModel ();
	for (int row = table.getRowCount () - 1; row >= 0; row--)
	    {
		table.removeRow (row);
	    }
	repaint (); // TODO: need this line?
    }

    public void removeValue (Locale locale)
    {
	DefaultTableModel table = (DefaultTableModel)propTable.getModel ();
	int row = searchLocale (table, locale);
	if (row >= 0)
	    table.removeRow (row);
	else
	    throw new NoSuchElementException ();
	repaint (); // TODO: need this line?
    }

    public void addValue (String value, Locale locale)
    {
	DefaultTableModel table = (DefaultTableModel)propTable.getModel ();
	int insertionPoint = searchLocale (table, locale);
	if (insertionPoint >= 0)
	    // replace existing property value
	    {
		Vector<Object> row =
		    (Vector<Object>)(table.getDataVector ().elementAt (insertionPoint));
		row.setElementAt (value, PROP_VALUE_COLUMN_INDEX);
		repaint (); // TODO: need this line?
	    }
	else
	    {
		Vector<Object> row = new Vector<Object> ();
		row.add (value);
		row.add (locale);
		table.insertRow (-insertionPoint - 1, row);
		repaint (); // TODO: need this line?
	    }
    }

    private class ButtonAddListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    DefaultTableModel table = (DefaultTableModel)propTable.getModel ();
	    Locale locale = (Locale)
		JOptionPane.showInputDialog (null,
					     bundle.getString ("label.text.locale"),
					     bundle.getString ("frame.title.chooseLocale"),
					     JOptionPane.INFORMATION_MESSAGE, null,
					     LOCALES, LOCALES[0]);
	    if (locale == null)
		// operation cancelled
		return;
	    int insertionPoint = searchLocale (table, locale);
	    if (insertionPoint >= 0)
		{
		    JOptionPane.showMessageDialog(null,
						  bundle.getString ("label.text.errorLocaleAlreadyExists"),
						  bundle.getString ("frame.title.errorLocaleAlreadyExists"),
						  JOptionPane.ERROR_MESSAGE);
		}
	    else
		{
		    Vector<Object> row = new Vector<Object> ();
		    row.add (INITIAL_PROP_VALUE);
		    row.add (locale);
		    table.insertRow (-insertionPoint - 1, row);
		}
	}
    }

    private class ButtonDeleteListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    DefaultTableModel table = (DefaultTableModel)propTable.getModel ();
	    int[] rows = propTable.getSelectedRows ();
	    for (int i = rows.length - 1; i >= 0; i--)
		table.removeRow (rows[i]);
	}
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
