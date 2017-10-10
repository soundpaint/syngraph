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
 * $Id: Document.java 313 2008-01-04 03:19:55Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class encapsulates all data that is stored when saving all
 * application data.
 *
 * @author Jürgen Reuter
 */
class Document
{
    /*
     * TODO: Instead of throwing IOException on file corruption, maybe
     * generate a warning, but still load document based on a best
     * effort strategy?
     *
     * TODO: instead of using Properties#storeToXML and
     * Properties#loadFromXML, maybe use Java XML Binding (JAXB) for
     * storing/loading document instances.
     */

    private final static ResourceBundle bundle = ResourceBundle.getBundle ();
    public final static Color DEFAULT_FG_COLOR = Color.BLACK;
    public final static Color DEFAULT_BG_COLOR = Color.WHITE;
    public final static Font DEFAULT_FONT = 
	(new javax.swing.JLabel ()).getFont ().
	deriveFont (Font.BOLD, 32.0f);
    private final static String MAP_NAME = "MAP";

    /*
     * The following reasonable limits are intended to avert denial of
     * service attacks by malicious (i.e. overly large or degenerated)
     * .syg files.
     *
     * TODO: When adding template editing capabilities to this
     * application, make sure, that these limits not only apply when
     * loading an .syg file, but also when editing/saving an .syg
     * file.  Otherwise, the user may accidentally create an .syg file
     * that can not be loaded back into this application.
     */
    private final static int MAX_FILE_LENGTH = 65536;
    private final static int MAX_OBJECTS = 1024;
    private final static int MAX_TABS = 32;
    private final static int MAX_SETS = 16;
    private final static int MAX_FUNCTIONS = 8;

    private transient DocumentInfo documentInfo;
    private String title;
    private String author;
    private String comment;
    private Font font;
    private Color bgColor;
    private String selectedTabID;
    private Map<String, ObjectInfo> objectInfos;
    private Map<String, ObjectTab> objectTabs;
    private Map<String, ObjectFunction> objectFunctions;
    private Map<String, ObjectSet> objectSets;

    private abstract static class NamedEntity
    {
	private Map<Locale, String> labels;
	private Map<Locale, String> comments;

	private NamedEntity ()
	{
	    labels = new HashMap<Locale, String> ();
	    comments = new HashMap<Locale, String> ();
	}

	private static int commonStartLength (String s1, String s2)
	{
	    int i;
	    for (i = 0; (i < s1.length ()) && (i < s2.length ()); i++)
		{
		    if (s1.charAt (i) != s2.charAt (i))
			return i;
		}
	    return i;
	}

	private static boolean matchesBetterThan (Locale candidate,
						  Locale current,
						  Locale target)
	{
	    String candidateStr = candidate.toString ();
	    String currentStr = current.toString ();
	    String targetStr = target.toString ();
	    return
		commonStartLength (candidateStr, targetStr) >
		commonStartLength (currentStr, targetStr);
	}

	private static String getBestLocaleMatch (Map<Locale, String> map)
	{
	    /*
	     * TODO: This algorithm does not at all scale well for a
	     * large number of locales.  Maybe replace Map by a
	     * SortedMap, as soon as the number of supported locales
	     * grows.
	     */
	    Locale optimalLocale = Locale.getDefault ();
	    Locale matchedLocale = null;
	    Iterator<Locale> iterator = map.keySet ().iterator ();
	    while (iterator.hasNext ())
		{
		    Locale locale = iterator.next ();
		    if (matchedLocale == null)
			matchedLocale = locale;
		    else if (matchesBetterThan (locale, matchedLocale,
						optimalLocale))
			matchedLocale = locale;
		}
	    if (matchedLocale == null)
		return null;
	    else
		return map.get (matchedLocale);
	}

	Set<Locale> getLabelLocales ()
	{
	    return labels.keySet ();
	}

	String getLabel (Locale locale)
	{
	    return labels.get (locale);
	}

	String getLabel ()
	{
	    return getBestLocaleMatch (labels);
	}

	void setLabel (Locale locale, String label)
	{
	    labels.put (locale, label);
	}

	void removeLabel (Locale locale)
	{
	    labels.remove (locale);
	}

	void removeAllLabels ()
	{
	    labels.clear ();
	}

	Set<Locale> getCommentLocales ()
	{
	    return comments.keySet ();
	}

	String getComment (Locale locale)
	{
	    return comments.get (locale);
	}

	String getComment ()
	{
	    return getBestLocaleMatch (comments);
	}

	void setComment (Locale locale, String comment)
	{
	    comments.put (locale, comment);
	}

	void removeComment (Locale locale)
	{
	    comments.remove (locale);
	}

	void removeAllComments ()
	{
	    comments.clear ();
	}
    }

    // TODO: maybe rename ObjectInfo -> GraphObject?
    private static class ObjectInfo extends NamedEntity
    {
	private String objectID;
	private Color color;

	private ObjectInfo () {}

	ObjectInfo (String objectID)
	{
	    assert (objectID != null);
	    this.objectID = objectID;
	}

	Color getColor ()
	{
	    return color;
	}

	void setColor (Color color)
	{
	    this.color = color;
	}

	String getObjectID ()
	{
	    return objectID;
	}
    }

    private static class Point
    {
	float x, y;

	Point ()
	{
	    this (0.0f, 0.0f);
	}

	Point (float x, float y)
	{
	    this.x = x;
	    this.y = y;
	}
    }

    private static class ObjectTab extends NamedEntity
    {
	private String tabID;
	private Map<String, Point> objectAlignments;

	private ObjectTab () {}

	ObjectTab (String tabID)
	{
	    assert (tabID != null);
	    this.tabID = tabID;
	    objectAlignments = new HashMap<String, Point> ();
	}

	Set<String> getObjectIDs ()
	{
	    return objectAlignments.keySet ();
	}

	Point getObjectAlignment (String objectID)
	{
	    return objectAlignments.get (objectID);
	}

	void setObjectAlignment (String objectID, Point alignment)
	{
	    objectAlignments.put (objectID, alignment);
	}
    }

    /**
     * An ObjectFunction instance maps syngraph object symbols to
     * syngraph object symbols.  A typical example is an
     * ObjectFunction called "to-lower-case", that maps upper case
     * letter symbols to lower case symbols.  ObjectFunction
     * definitions are useful, e.g. when copying colors from upper
     * case symbols to lower case symbols in one go.
     */
    private static class ObjectFunction extends NamedEntity
    {
	private String functionID;
	private Map<String, String> objectMapping;

	private ObjectFunction () {}

	ObjectFunction (String functionID)
	{
	    assert (functionID != null);
	    this.functionID = functionID;
	    objectMapping = new HashMap<String, String> ();
	}

	Set<String> getKeyIDs ()
	{
	    return objectMapping.keySet ();
	}

	String getValueID (String keyID)
	{
	    return objectMapping.get (keyID);
	}

	void setValueID (String keyID, String valueID)
	{
	    objectMapping.put (keyID, valueID);
	}
    }

    /**
     * An ObjectSet instance is a named set of syngraph object symbols
     * with unique labels.  Symbols can be uniquely looked up by their
     * label.  A typical example is an ObjectSet called
     * "notice-pad-symbol", that contains the set of all symbols with
     * unique labels that are automatically recognized when typing
     * onto a notice pad.
     */
    private static class ObjectSet extends NamedEntity
    {
	private String setID;
	private transient Set<String> objectIDs;
	private Map<String, String> objects;

	private ObjectSet () {}

	ObjectSet (String setID)
	{
	    assert (setID != null);
	    this.setID = setID;
	    objects = null;
	    objectIDs = new HashSet<String> ();
	}

	Set<String> getLabels ()
	{
	    assert objects != null : "not yet completed";
	    return objects.keySet ();
	}

	String getObjectID (String label)
	{
	    assert objects != null : "not yet completed";
	    return objects.get (label);
	}

	void addObject (String label, String objectID)
	{
	    assert objects != null : "not yet completed";
	    objects.put (label, objectID);
	}

	void addObjectID (String objectID)
	{
	    assert objects == null : "already completed";
	    objectIDs.add (objectID);
	}

	void complete (Map<String, ObjectInfo> objectInfos)
	    throws IOException
	{
	    assert objects == null : "already completed";
	    objects = new HashMap<String, String> ();
	    for (String objectID : objectIDs)
		{
		    ObjectInfo object = objectInfos.get (objectID);
		    // FIXME: We only handle the default locale.  This
		    // approach only works if the locale is not
		    // switched while the program is running.
		    // Otherwise, for each locale, we need a separate
		    // instance of "Map<String, String> objects".
		    String label = object.getLabel ();
		    String lastObjectID = objects.put (label, objectID);
		    if (lastObjectID != null)
			throwCorruptedException (); // set has ambiguous labels
		}
	}
    }

    private Document () {}

    Document (String author, DocumentInfo documentInfo)
    {
	this.author = author;
	this.documentInfo = documentInfo;
	documentInfo.setUnsavedChanges (false);
	title = null;
	comment = null;
	font = DEFAULT_FONT;
	bgColor = Color.WHITE;
	selectedTabID = null;
	objectInfos = new HashMap<String, ObjectInfo> ();
	objectTabs = new HashMap<String, ObjectTab> ();
	objectFunctions = new HashMap<String, ObjectFunction> ();
	objectSets = new HashMap<String, ObjectSet> ();
    }

    private void copyFrom (Document other)
    {
	this.title = other.title;
	this.author = other.author;
	this.comment = other.comment;
	this.font = other.font;
	this.bgColor = other.bgColor;
	this.selectedTabID = other.selectedTabID;
	this.objectInfos = other.objectInfos;
	this.objectTabs = other.objectTabs;
	this.objectFunctions = other.objectFunctions;
	this.objectSets = other.objectSets;
    }

    private static void throwCorruptedException () throws IOException
    {
	throw new IOException (bundle.getString ("message.fileCorrupted"));
    }

    private static void throwCorruptedException (String i18nMessage,
						 String ... params)
	throws IOException
    {
	String localizedDetailMessage = bundle.getString (i18nMessage, params);
	String localizedFullMessage =
	    bundle.getString ("message.fileCorruptedDetail",
			      localizedDetailMessage);
	throw new IOException (localizedFullMessage);
    }

    void load (String path) throws IOException
    {
	long length = new File (path).length ();
	if (length > MAX_FILE_LENGTH)
	    throwCorruptedException (bundle.getString ("message.file.tooLarge"));
	FileInputStream documentStream = new FileInputStream (path);
	load (documentStream, path);
    }

    /*
     * Load a special file (e.g. the factory settings file)
     * that has no path that is accessible for the user.
     */
    void load (InputStream documentStream) throws IOException
    {
	load (documentStream, null);
    }

    private void load (InputStream documentStream, String path)
	throws IOException
    {
	boolean loadedMap = false;
	ZipInputStream zipStream = new ZipInputStream (documentStream);
	ZipEntry entry = zipStream.getNextEntry ();
	while (entry != null)
	    {
		String name = entry.getName ();
		if (name == null)
		    throwCorruptedException ();
		if (name.equals (MAP_NAME))
		    {
			if (loadedMap) // duplicate zip entry?
			    throwCorruptedException ();
			loadMap (zipStream, path);
			loadedMap = true;
		    }
		else
		    {
			// ignore unknown zip entry
			/*
			 * TODO: Maybe, print a warning?
			 */
		    }
		entry = zipStream.getNextEntry ();
	    }
	zipStream.close ();
	if (!loadedMap)
	    throwCorruptedException ();
    }

    private static class UnclosableInputStream extends InputStream
    {
	InputStream in;
	private UnclosableInputStream () {}
	public UnclosableInputStream (InputStream in)
	{
	    assert in != null;
	    this.in = in;
	}
	public int available () throws IOException { return in.available (); }
	public void close () throws IOException {} // don't close
	public void mark (int readlimit) { in.mark (readlimit); }
	public boolean markSupported () { return in.markSupported (); }
	public int read () throws IOException { return in.read (); }
	public int read (byte[] b) throws IOException { return in.read (b); }
	public int read (byte[] b, int off, int len) throws IOException { return in.read (b, off, len); }
	public void reset () throws IOException { in.reset (); }
	public long skip (long n) throws IOException { return in.skip (n); }
    }

    private void loadMap (InputStream mapStream, String documentPath)
	throws IOException
    {
	Properties props = new Properties ();
	props.loadFromXML (new UnclosableInputStream (mapStream));
	String fileFormatVersion = props.getProperty ("format.version");
	if (!Main.getFileFormatVersion ().equals (fileFormatVersion))
	    {
		String warning =
		    bundle.getString ("label.text.warningBadFileFormatVersion",
				      fileFormatVersion,
				      Main.getFileFormatVersion ());
		System.err.printf (warning);
		System.err.println ();
	    }
	String fontName = null;
	int fontStyle = 0;
	int fontSize = 0;
	String formatVersion = null;
	for (Object keyObj : props.keySet ())
	    {
		String key = (String)keyObj;
		String value = props.getProperty (key);
		if (key.startsWith ("tab."))
		    parseTabProperty (key.substring (4), value, this);
		else if (key.startsWith ("function."))
		    parseFunctionProperty (key.substring (9), value, this);
		else if (key.startsWith ("set."))
		    parseSetProperty (key.substring (4), value, this);
		else if (key.startsWith ("object."))
		    parseObjectProperty (key.substring (7), value, this);
		else if (key.equals ("document.title"))
		    title = value;
		else if (key.equals ("document.author"))
		    author = value;
		else if (key.equals ("document.comment"))
		    comment = value;
		else if (key.equals ("document.background.color"))
		    bgColor = ParseUtils.parseColor (value);
		else if (key.startsWith ("document.selectedTab."))
		    selectedTabID = key.substring (21);
		else if (key.equals ("document.font.name"))
		    fontName = value;
		else if (key.equals ("document.font.style"))
		    fontStyle = parseInt (value);
		else if (key.equals ("document.font.size"))
		    fontSize = parseInt (value);
		else if (key.equals ("format.version"))
		    formatVersion = value;
		else
		    {
			String warning =
			    bundle.getString ("label.text.warningUnknownProperty",
					      key, value);
			System.err.printf (warning);
			System.err.println ();
		    }
	    }
	if ((fontName != null) && (fontSize > 0))
	    font = new Font (fontName, fontStyle, fontSize);
	else
	    font = DEFAULT_FONT;
	checkFunctions ();
	completeSets ();
	documentInfo.setUnsavedChanges (false);
    }

    private void checkFunctions () throws IOException
    {
	for (String functionID : objectFunctions.keySet ())
	    {
		ObjectFunction function = objectFunctions.get (functionID);
		for (String keyID : function.getKeyIDs ())
		    {
			ObjectInfo object;
			object = objectInfos.get (keyID);
			if (object == null)
			    throwCorruptedException ("message.danglingObjectRef",
						     functionID, keyID);
			String valueID = function.getValueID (keyID);
			object = objectInfos.get (valueID);
			if (object == null)
			    throwCorruptedException ("message.danglingObjectRef",
						     functionID, valueID);
		    }
	    }
    }

    private void completeSets () throws IOException
    {
	for (String setID : objectSets.keySet ())
	    {
		ObjectSet set = objectSets.get (setID);
		set.complete (objectInfos);
	    }
    }

    private static int parseInt (String valueStr) throws IOException
    {
	int value = 0;
	try
	    {
		value = Integer.parseInt (valueStr);
	    }
	catch (NumberFormatException e)
	    {
		throwCorruptedException ();
	    }
	return value;
    }

    private static void parseObjectProperty (String key, String value,
					     Document document)
	throws IOException
    {
	int dotPos = key.indexOf ('.');
	if (dotPos < 0)
	    throwCorruptedException ();
	String objectID = key.substring (0, dotPos);
	key = key.substring (dotPos + 1);
	if (key.equals ("color"))
	    {
		Color color = ParseUtils.parseColor (value);
		document.setObjectColor (objectID, color);
	    }
	else if (key.startsWith ("label."))
	    {
		Locale locale = parseLocale (key.substring (6));
		document.setObjectLabel (objectID, locale, value);
	    }
	else if (key.startsWith ("comment."))
	    {
		Locale locale = parseLocale (key.substring (8));
		document.setObjectComment (objectID, locale, value);
	    }
	else
	    throwCorruptedException ();
	if (document.objectInfos.size () > MAX_OBJECTS)
	    throwCorruptedException (bundle.getString ("message.file.tooManyObjects"));
    }

    private static void parseTabProperty (String key, String value,
					  Document document)
	throws IOException
    {
	int dotPos = key.indexOf ('.');
	if (dotPos < 0)
	    throwCorruptedException ();
	String tabID = key.substring (0, dotPos);
	ObjectTab tab = document.objectTabs.get (tabID);
	if (tab == null)
	    {
		tab = new ObjectTab (tabID);
		document.objectTabs.put (tabID, tab);
	    }
	if (document.objectTabs.size () > MAX_TABS)
	    throwCorruptedException (bundle.getString ("message.file.tooManyTabs"));
	key = key.substring (dotPos + 1);
	if (key.startsWith ("label."))
	    {
		Locale locale = parseLocale (key.substring (6));
		tab.setLabel (locale, value);
	    }
	else if (key.startsWith ("comment."))
	    {
		Locale locale = parseLocale (key.substring (8));
		tab.setComment (locale, value);
	    }
	else if (key.startsWith ("object."))
	    parseTabObjectProperty (key.substring (7), value, tab);
	else
	    throwCorruptedException ();
    }

    private static void parseFunctionProperty (String key, String value,
					       Document document)
	throws IOException
    {
	int dotPos = key.indexOf ('.');
	if (dotPos < 0)
	    throwCorruptedException ();
	String functionID = key.substring (0, dotPos);
	ObjectFunction function = document.objectFunctions.get (functionID);
	if (function == null)
	    {
		function = new ObjectFunction (functionID);
		document.objectFunctions.put (functionID, function);
	    }
	if (document.objectFunctions.size () > MAX_FUNCTIONS)
	    throwCorruptedException (bundle.getString ("message.file.tooManyFunctions"));
	key = key.substring (dotPos + 1);
	if (key.startsWith ("label."))
	    {
		Locale locale = parseLocale (key.substring (6));
		function.setLabel (locale, value);
	    }
	else if (key.startsWith ("comment."))
	    {
		Locale locale = parseLocale (key.substring (8));
		function.setComment (locale, value);
	    }
	else if (key.startsWith ("map."))
	    parseFunctionMapping (key.substring (4), function);
	else
	    throwCorruptedException ();
    }

    private static void parseSetProperty (String key, String value,
					  Document document)
	throws IOException
    {
	int dotPos = key.indexOf ('.');
	if (dotPos < 0)
	    throwCorruptedException ();
	String setID = key.substring (0, dotPos);
	ObjectSet set = document.objectSets.get (setID);
	if (set == null)
	    {
		set = new ObjectSet (setID);
		document.objectSets.put (setID, set);
	    }
	if (document.objectSets.size () > MAX_SETS)
	    throwCorruptedException (bundle.getString ("message.file.tooManySets"));
	key = key.substring (dotPos + 1);
	String objectID = key;
	set.addObjectID (objectID);
    }

    private static Locale parseLocale (String key) //throws IOException
    {
	String language, country, variant;
	int underscorePos1 = key.indexOf ('_');
	if (underscorePos1 >= 0)
	    {
		language = key.substring (0, underscorePos1);
		key = key.substring (underscorePos1 + 1);
		int underscorePos2 = key.indexOf ('_');
		if (underscorePos2 >= 0)
		    {
			country = key.substring (0, underscorePos2);
			variant = key.substring (underscorePos2 + 1);
		    }
		else
		    {
			country = key;
			variant = null;
		    }
	    }
	else
	    {
		language = key;
		country = null;
		variant = null;
	    }
	/*
	 * TODO: if (language, country or variant contains non-letter
	 * character) then throwCorruptedException ();
	 */
	Locale locale;
	if (variant != null)
	    locale = new Locale (language, country, variant);
	else if (country != null)
	    locale = new Locale (language, country);
	else
	    locale = new Locale (language);
	return locale;
    }

    private static void parseTabObjectProperty (String key, String value,
						ObjectTab tab)
	throws IOException
    {
	int dotPos = key.indexOf ('.');
	if (dotPos < 0)
	    throwCorruptedException ();
	String objectID = key.substring (0, dotPos);
	key = key.substring (dotPos + 1);
	if (key.startsWith ("align."))
	    parseTabObjectAlignment (key.substring (6), value, tab, objectID);
	else
	    throwCorruptedException ();
    }

    private static void parseTabObjectAlignment (String key, String value,
						 ObjectTab tab,
						 String objectID)
	throws IOException
    {
	if (!key.equals ("x") && !key.equals ("y"))
	    throwCorruptedException ();
	Point point = tab.getObjectAlignment (objectID);
	if (point == null)
	    {
		point = new Point ();
	    }
	float floatValue = 0.0f;
	try
	    {
		floatValue = Float.parseFloat (value);
	    }
	catch (NumberFormatException e)
	    {
		throwCorruptedException ();
	    }
	if (key.equals ("x"))
	    point.x = floatValue;
	else
	    point.y = floatValue;
	tab.setObjectAlignment (objectID, point);
    }

    private static void parseFunctionMapping (String key,
					      ObjectFunction function)
	throws IOException
    {
	int dotPos = key.indexOf ('.');
	if (dotPos < 0)
	    throwCorruptedException ();
	String keyID = key.substring (0, dotPos);
	key = key.substring (dotPos + 1);
	String valueID = key;
	function.setValueID (keyID, valueID);
    }

    void save (String path) throws IOException
    {
	ZipOutputStream zipStream =
	    new ZipOutputStream (new FileOutputStream (path));
	ZipEntry entry = new ZipEntry (MAP_NAME);
	zipStream.putNextEntry (entry);
	saveMap (zipStream);
	zipStream.close ();
    }

    private static class UnclosableOutputStream extends OutputStream
    {
	OutputStream out;
	private UnclosableOutputStream () {}
	public UnclosableOutputStream (OutputStream out)
	{
	    assert out != null;
	    this.out = out;
	}
	public void close () throws IOException {} // don't close
	public void flush () throws IOException
	{
	    out.flush ();
	}
	public void write (byte[] b) throws IOException
	{
	    out.write (b);
	}
	public void write (byte[] b, int off, int len) throws IOException
	{
	    out.write (b, off, len);
	}
	public void write (int b) throws IOException
	{
	    out.write (b);
	}
    }

    private void saveMap (OutputStream outputStream) throws IOException
    {
	Properties props = new Properties ();
	props.setProperty ("format.version", Main.getFileFormatVersion ());
	if (title != null)
	    props.setProperty ("document.title", title);
	if (author != null)
	    props.setProperty ("document.author", author);
	if (comment != null)
	    props.setProperty ("document.comment", comment);
	ParseUtils.dumpFont (props, "document.font", font);
	props.setProperty ("document.background.color",
			   ParseUtils.color2String (bgColor));
	props.setProperty ("document.selectedTab." + selectedTabID, "");
	for (String objectID : objectInfos.keySet ())
	    {
		ObjectInfo object = objectInfos.get (objectID);
		Color color = object.getColor ();
		props.setProperty ("object." + objectID + ".color",
				   ParseUtils.color2String (color));
		for (Locale locale : object.getLabelLocales ())
		    {
			String localeID = locale2ID (locale);
			String objectLabel = object.getLabel (locale);
			props.setProperty ("object." + objectID +
					   ".label." + localeID,
					   objectLabel);
		    }
	    }
	saveObjectTabs (props);
	saveObjectFunctions (props);
	saveObjectSets (props);
	props.storeToXML (new UnclosableOutputStream (outputStream),
			  "syngraph");
	documentInfo.setUnsavedChanges (false);
    }

    private static String locale2ID (Locale locale)
    {
	String language = locale.getLanguage ();
	String country = locale.getCountry ();
	String variant = locale.getVariant ();
	String localeID =
	    language +
	    ((country.length () > 0) ?
	     "_" + country +
	     ((variant.length () > 0) ?
	      "_" + variant : "") : "");
	return localeID;
    }

    private void saveObjectTabs (Properties props)
	throws IOException
    {
	for (String tabID : objectTabs.keySet ())
	    {
		ObjectTab tab = objectTabs.get (tabID);
		for (Locale locale : tab.getLabelLocales ())
		    {
			String localeID = locale2ID (locale);
			String tabLabel = tab.getLabel (locale);
			props.setProperty ("tab." + tabID +
					   ".label." + localeID,
					   tabLabel);
		    }
		for (Locale locale : tab.getCommentLocales ())
		    {
			String localeID = locale2ID (locale);
			String tabComment = tab.getComment (locale);
			props.setProperty ("tab." + tabID +
					   ".comment." + locale,
					   tabComment);
		    }
		for (String objectID : tab.getObjectIDs ())
		    {
			Point alignment = tab.getObjectAlignment (objectID);
			props.setProperty ("tab." + tabID + ".object." +
					   objectID + ".align.x",
					   "" + alignment.x);
			props.setProperty ("tab." + tabID + ".object." +
					   objectID + ".align.y",
					   "" + alignment.y);
		    }
	    }
    }

    private void saveObjectFunctions (Properties props)
	throws IOException
    {
	for (String functionID : objectFunctions.keySet ())
	    {
		ObjectFunction function = objectFunctions.get (functionID);
		for (Locale locale : function.getLabelLocales ())
		    {
			String localeID = locale2ID (locale);
			String functionLabel = function.getLabel (locale);
			props.setProperty ("function." + functionID +
					   ".label." + localeID,
					   functionLabel);
		    }
		for (Locale locale : function.getCommentLocales ())
		    {
			String localeID = locale2ID (locale);
			String functionComment = function.getComment (locale);
			props.setProperty ("function." + functionID +
					   ".comment." + locale,
					   functionComment);
		    }
		for (String keyID : function.getKeyIDs ())
		    {
			String valueID = function.getValueID (keyID);
			props.setProperty ("function." + functionID + ".map." +
					   keyID + "." + valueID, "");
		    }
	    }
    }

    private void saveObjectSets (Properties props)
	throws IOException
    {
	for (String setID : objectSets.keySet ())
	    {
		ObjectSet set = objectSets.get (setID);
		for (Locale locale : set.getLabelLocales ())
		    {
			String localeID = locale2ID (locale);
			String setLabel = set.getLabel (locale);
			props.setProperty ("set." + setID +
					   ".label." + localeID,
					   setLabel);
		    }
		for (Locale locale : set.getCommentLocales ())
		    {
			String localeID = locale2ID (locale);
			String setComment = set.getComment (locale);
			props.setProperty ("set." + setID +
					   ".comment." + locale,
					   setComment);
		    }
		for (String label : set.getLabels ())
		    {
			String objectID = set.getObjectID (label);
			props.setProperty ("set." + setID + "." + objectID, "");
		    }
	    }
    }

    void detachFromGUI ()
    {
	/*
	 * Objects only live in ObjectTab instances.  Therefore, it is
	 * sufficient to detach all tabs.
	 */
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		for (String tabID : objectTabs.keySet ())
		    {
			ObjectTab tab = objectTabs.get (tabID);
			listener.tabRemoved (tabID);
		    }
	    }

	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		for (String functionID : objectFunctions.keySet ())
		    {
			listener.functionRemoved (functionID);
		    }
	    }

	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		for (String setID : objectSets.keySet ())
		    {
			listener.setRemoved (setID);
		    }
	    }

	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.titleChanged ("");
		listener.authorChanged ("");
		listener.commentChanged ("");
		listener.fontChanged (DEFAULT_FONT);
		listener.bgColorChanged (DEFAULT_BG_COLOR);
	    }
    }

    void attachToGUI ()
    {
	/*
	 * Adding new tabs will reset selectedTabID.  Therefore, save
	 * it now and restore it later on.
	 */
	String selectedTabID = this.selectedTabID;

	/*
	 * Calling listener.tabAdded () may result in having another
	 * listener.  Therefore, as a first step, we call this method
	 * in order to be sure to catch all listeners, and then, in a
	 * second step, we call any other listener method.
	 */

	/*
	 * Don't iterate with implicit loop, since this may result in
	 * a java.util.ConcurrentModificationException due to any
	 * listener being added to the set.
	 */
	Enumeration<DocumentChangeListener> enumeration =
	    documentInfo.getListeners ().elements ();
	while (enumeration.hasMoreElements ())
	    {
		DocumentChangeListener listener = enumeration.nextElement ();
		for (String tabID : objectTabs.keySet ())
		    {
			ObjectTab tab = objectTabs.get (tabID);
			listener.tabAdded (tabID);
		    }
	    }

	/*
	 * Now restore the saved selectedTabID.
	 */
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.selectedTabChanged (selectedTabID);
	    }

	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		for (String functionID : objectFunctions.keySet ())
		    {
			listener.functionAdded (functionID);
		    }
	    }

	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		for (String setID : objectSets.keySet ())
		    {
			listener.setAdded (setID);
		    }
	    }

	/*
	 * Next step: object alignments (and all other tab specific
	 * stuff).  These must come before color/font settings, such
	 * that they are updated to any color/font.
	 */
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		for (String tabID : objectTabs.keySet ())
		    {
			ObjectTab tab = objectTabs.get (tabID);
			for (Locale locale : tab.getLabelLocales ())
			    {
				listener.tabLabelChanged (tabID);
			    }
			for (Locale locale : tab.getCommentLocales ())
			    {
				listener.tabCommentChanged (tabID);
			    }
			for (String objectID : tab.getObjectIDs ())
			    {
				Point alignment =
				    tab.getObjectAlignment (objectID);
				listener.
				    tabObjectAlignChanged (tabID, objectID,
							   alignment.x,
							   alignment.y);
			    }
		    }
	    }

	/*
	 * Finally, we can globally change colors, fonts, etc.
	 */
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.titleChanged (title);
		listener.authorChanged (author);
		listener.commentChanged (comment);
		listener.fontChanged (font);
		listener.bgColorChanged (bgColor);
		for (String objectID : objectInfos.keySet ())
		    {
			Color color = getObjectColor (objectID);
			listener.objectColorChanged (objectID, color);
			ObjectInfo object = objectInfos.get (objectID);
			listener.objectLabelChanged (objectID);
			listener.objectCommentChanged (objectID);
		    }
	    }
    }

    String getTitle ()
    {
	return title;
    }

    void setTitle (String title)
    {
	this.title = title;
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.titleChanged (title);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getAuthor ()
    {
	return author;
    }

    void setAuthor (String author)
    {
	this.author = author;
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.authorChanged (author);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getComment ()
    {
	return comment;
    }

    void setComment (String comment)
    {
	this.comment = comment;
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.commentChanged (comment);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    Font getFont ()
    {
	return font;
    }

    void setFont (Font font)
    {
	this.font = font;
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.fontChanged (font);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    Color getBGColor ()
    {
	return bgColor;
    }

    void setBGColor (Color bgColor)
    {
	this.bgColor = bgColor;
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.bgColorChanged (bgColor);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getTabLabel (String tabID, Locale locale)
    {
	ObjectTab tab = objectTabs.get (tabID);
	return tab.getLabel (locale);
    }

    String getTabLabel (String tabID)
    {
	ObjectTab tab = objectTabs.get (tabID);
	return tab.getLabel ();
    }

    void setTabLabel (String tabID, Locale locale, String label)
    {
	ObjectTab tab = objectTabs.get (tabID);
	tab.setLabel (locale, label);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.tabLabelChanged (tabID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getTabComment (String tabID, Locale locale)
    {
	ObjectTab tab = objectTabs.get (tabID);
	return tab.getComment (locale);
    }

    String getTabComment (String tabID)
    {
	ObjectTab tab = objectTabs.get (tabID);
	return tab.getComment ();
    }

    void setTabComment (String tabID, Locale locale, String comment)
    {
	ObjectTab tab = objectTabs.get (tabID);
	tab.setComment (locale, comment);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.tabCommentChanged (tabID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getSelectedTabID ()
    {
	return selectedTabID;
    }

    void setSelectedTabID (String selectedTabID)
    {
	this.selectedTabID = selectedTabID;
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.selectedTabChanged (selectedTabID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getFunctionLabel (String functionID, Locale locale)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	return function.getLabel (locale);
    }

    String getFunctionLabel (String functionID)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	return function.getLabel ();
    }

    void setFunctionLabel (String functionID, Locale locale, String label)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	function.setLabel (locale, label);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.functionLabelChanged (functionID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getFunctionComment (String functionID, Locale locale)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	return function.getComment (locale);
    }

    String getFunctionComment (String functionID)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	return function.getComment ();
    }

    void setFunctionComment (String functionID, Locale locale, String comment)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	function.setComment (locale, comment);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.functionCommentChanged (functionID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getSetLabel (String setID, Locale locale)
    {
	ObjectSet set = objectSets.get (setID);
	return set.getLabel (locale);
    }

    String getSetLabel (String setID)
    {
	ObjectSet set = objectSets.get (setID);
	return set.getLabel ();
    }

    void setSetLabel (String setID, Locale locale, String label)
    {
	ObjectSet set = objectSets.get (setID);
	set.setLabel (locale, label);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.setLabelChanged (setID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getSetComment (String setID, Locale locale)
    {
	ObjectSet set = objectSets.get (setID);
	return set.getComment (locale);
    }

    String getSetComment (String setID)
    {
	ObjectSet set = objectSets.get (setID);
	return set.getComment ();
    }

    void setSetComment (String setID, Locale locale, String comment)
    {
	ObjectSet set = objectSets.get (setID);
	set.setComment (locale, comment);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.setCommentChanged (setID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    String getSetObjectIDByLabel (String setID, String label)
    {
	ObjectSet set = objectSets.get (setID);
	return (set != null) ? set.getObjectID (label) : null;
    }

    Set<Locale> getObjectLabelLocales (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	assert object != null;
	return object.getLabelLocales ();
    }

    String getObjectLabel (String objectID, Locale locale)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    return null;
	else
	    return object.getLabel (locale);
    }

    String getObjectLabel (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    return null;
	else
	    return object.getLabel ();
    }

    void setObjectLabel (String objectID, Locale locale, String label)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    {
		object = new ObjectInfo (objectID);
		objectInfos.put (objectID, object);
	    }
	object.setLabel (locale, label);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    listener.objectLabelChanged (objectID);
	documentInfo.setUnsavedChanges (true);
    }

    void removeObjectLabels (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	assert object != null;
	object.removeAllLabels ();
    }

    void removeObjectLabel (String objectID, Locale locale)
    {
	ObjectInfo object = objectInfos.get (objectID);
	assert object != null;
	object.removeLabel (locale);
	documentInfo.setUnsavedChanges (true);
    }

    Set<Locale> getObjectCommentLocales (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	assert object != null;
	return object.getCommentLocales ();
    }

    String getObjectComment (String objectID, Locale locale)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    return null;
	else
	    return object.getComment (locale);
    }

    String getObjectComment (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    return null;
	else
	    return object.getComment ();
    }

    void setObjectComment (String objectID, Locale locale,
			   String comment)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    {
		object = new ObjectInfo (objectID);
		objectInfos.put (objectID, object);
	    }
	object.setComment (locale, comment);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.objectCommentChanged (objectID);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    void removeObjectComments (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	assert object != null;
	object.removeAllComments ();
    }

    void removeObjectComment (String objectID, Locale locale)
    {
	ObjectInfo object = objectInfos.get (objectID);
	assert object != null;
	object.removeComment (locale);
	documentInfo.setUnsavedChanges (true);
    }

    Color getObjectColor (String objectID)
    {
	ObjectInfo object = objectInfos.get (objectID);
	return object.getColor ();
    }

    void setObjectColor (String objectID, Color color)
    {
	ObjectInfo object = objectInfos.get (objectID);
	if (object == null)
	    {
		object = new ObjectInfo (objectID);
		objectInfos.put (objectID, object);
	    }
	object.setColor (color);
	for (DocumentChangeListener listener : documentInfo.getListeners ())
	    {
		listener.objectColorChanged (objectID, color);
	    }
	documentInfo.setUnsavedChanges (true);
    }

    void resetAllObjectColors ()
    {
	for (String objectID : objectInfos.keySet ())
	    setObjectColor (objectID, DEFAULT_FG_COLOR);
    }

    void copyObjectColors (String functionID)
    {
	ObjectFunction function = objectFunctions.get (functionID);
	for (String keyID : function.getKeyIDs ())
	    {
		ObjectInfo source = objectInfos.get (keyID);
		assert source != null : "corrupted function " + functionID;
		String valueID = function.getValueID (keyID);
		ObjectInfo target = objectInfos.get (valueID);
		assert target != null : "corrupted function " + functionID;
		setObjectColor (target.objectID, source.color);
	    }
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
