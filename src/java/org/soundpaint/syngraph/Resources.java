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
 * $Date: 2008-01-04 00:01:35 +0100 (Fri, 04 Jan 2008) $
 * $Id: Resources.java 309 2008-01-03 23:01:35Z reuter $
 */
package org.soundpaint.syngraph;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ListResourceBundle;

/**
 * A default resources implementation that is used if no proper
 * resources can be found in the bundle.
 *
 * @author Jürgen Reuter
 */
public class Resources extends ListResourceBundle
{
    // TODO: rename this class into DefaultResources or FallbackResources?

    private final static Locale LOCALE = Locale.ENGLISH;
    private final static Object[][] CONTENTS =
    {
	{ "border.text.defaultDocumentAuthor",
	  "Dokumenten-Voreinstellung für \"Autor\"" },
	{ "border.text.lookAndFeel",
	  "Look & Feel" },
	{ "border.text.toolBar",
	  "Werkzeugleiste" },
	{ "border.text.toolTips",
	  "Hinweise" },
	{ "border.text.preview",
	  "Vorschau" },
	{ "border.text.cryptify",
	  "Geheimschrift" },
	{ "label.text.fileTypeSyg",
	  "SynGraph-Dateien" },
	{ "label.text.invalidLaf",
	  "Look & Feel \"{0}\" nicht gefunden" },
	{ "label.text.saveFormat",
	  "Speichern in Dateiformat:" },
	{ "label.text.noTabInfo",
	  "Für dieses Diagramm ist keine weitere#n#" +
	  "Information verfügbar." },
	{ "label.text.defaultDocumentAuthor",
	  "Autor" },
	{ "label.mnemonic.defaultDocumentAuthor",
	  "" + KeyEvent.VK_A },
	{ "label.text.lookAndFeel",
	  "Look & Feel" },
	{ "label.mnemonic.lookAndFeel",
	  "" + KeyEvent.VK_L },
	{ "label.text.toolBarCheck",
	  "Werkzeugleiste einblenden" },
	{ "label.mnemonic.toolBarCheck",
	  "" + KeyEvent.VK_W },
	{ "label.text.toolTipsCheck",
	  "Hinweise aktivieren" },
	{ "label.mnemonic.toolTipsCheck",
	  "" + KeyEvent.VK_H },
	{ "label.text.isCrypticCheck",
	  "Kryptifizieren" },
	{ "label.mnemonic.isCrypticCheck",
	  "" + KeyEvent.VK_K },
	{ "label.text.propertyTitle",
	  "Titel" },
	{ "label.mnemonic.propertyTitle",
	  "" + KeyEvent.VK_T },
	{ "label.text.propertyAuthor",
	  "Autor" },
	{ "label.mnemonic.propertyAuthor",
	  "" + KeyEvent.VK_U },
	{ "label.text.propertyComment",
	  "Kommentar" },
	{ "label.mnemonic.propertyComment",
	  "" + KeyEvent.VK_K },
	{ "label.text.preview",
	  "Vorschau" },
	{ "label.mnemonic.preview",
	  "" + KeyEvent.VK_V },
	{ "label.text.fontName",
	  "Name:" },
	{ "label.mnemonic.fontName",
	  "" + KeyEvent.VK_N },
	{ "label.text.fontSize",
	  "Größe [pt]:" },
	{ "label.mnemonic.fontSize",
	  "" + KeyEvent.VK_G },
	{ "label.text.menuTitleFile",
	  "Datei" },
	{ "label.mnemonic.menuTitleFile",
	  "" + KeyEvent.VK_D },
	{ "label.text.menuItemOpen",
	  "Öffnen..." },
	{ "label.mnemonic.menuItemOpen",
	  "" + KeyEvent.VK_F },
	{ "label.text.menuItemSave",
	  "Speichern" },
	{ "label.mnemonic.menuItemSave",
	  "" + KeyEvent.VK_S },
	{ "label.text.menuItemSaveAs",
	  "Speichern unter..." },
	{ "label.mnemonic.menuItemSaveAs",
	  "" + KeyEvent.VK_U },
	{ "label.text.menuItemClose",
	  "Schließen" },
	{ "label.mnemonic.menuItemClose",
	  "" + KeyEvent.VK_L },
	{ "label.text.menuItemPreferences",
	  "Einstellungen..." },
	{ "label.mnemonic.menuItemPreferences",
	  "" + KeyEvent.VK_I },
	{ "label.text.menuItemProperties",
	  "Eigenschaften..." },
	{ "label.mnemonic.menuItemProperties",
	  "" + KeyEvent.VK_E },
	{ "label.text.menuItemNotice",
	  "Notizzettel..." },
	{ "label.mnemonic.menuItemNotice",
	  "" + KeyEvent.VK_N },
	{ "label.text.menuItemExit",
	  "Beenden" },
	{ "label.mnemonic.menuItemExit",
	  "" + KeyEvent.VK_B },
	{ "label.text.menuTitleEdit",
	  "Optionen" },
	{ "label.mnemonic.menuTitleEdit",
	  "" + KeyEvent.VK_E },
	{ "label.text.menuItemBlacken",
	  "Alles auf Schwarz..." },
	{ "label.mnemonic.menuItemBlacken",
	  "" + KeyEvent.VK_S },
	{ "label.text.menuItemFont",
	  "Zeichensatz..." },
	{ "label.mnemonic.menuItemFont",
	  "" + KeyEvent.VK_Z },
	{ "label.text.menuItemBgColor",
	  "Hintergrundfarbe..." },
	{ "label.mnemonic.menuItemBgColor",
	  "" + KeyEvent.VK_H },
	{ "label.text.menuItemCopy",
	  "Kopiere {0}..." },
	{ "label.text.menuTitleHelp",
	  "Hilfe" },
	{ "label.mnemonic.menuTitleHelp",
	  "" + KeyEvent.VK_H },
	{ "label.text.menuItemDocumentation",
	  "Dokumentation..." },
	{ "label.mnemonic.menuItemDocumentation",
	  "" + KeyEvent.VK_K },
	{ "label.text.menuItemAbout",
	  "Über..." },
	{ "label.mnemonic.menuItemAbout",
	  "" + KeyEvent.VK_B },
	{ "label.text.menuItemLicence",
	  "Lizenz..." },
	{ "label.mnemonic.menuItemLicence",
	  "" + KeyEvent.VK_L },
	{ "label.text.changeColor",
	  "Farbe ändern..." },
	{ "label.mnemonic.menuItemChangeColor",
	  "" + KeyEvent.VK_F },
	{ "label.text.changeComment",
	  "Kommentar ändern..." },
	{ "label.mnemonic.menuItemChangeComment",
	  "" + KeyEvent.VK_K },
	{ "label.text.bgColor",
	  "Hintergrundfarbe" },
	{ "label.text.queryCopy",
	  "Sollen die Farben aller {0}#n#unwiderruflich kopiert werden?" },
	{ "label.text.appFrameUsage",
	  "Zum Ändern der Farbe auf jeweiliges Symbol klicken!" },
	{ "label.text.queryDiscardChanges",
	  "Änderungen an aktuellem Dokument verwerfen?" },
	{ "label.text.errorNoProperExportDriver",
	  "Kein geeigneter Bildtreiber für Export gefunden" },
	{ "label.text.errorNoExportDriver",
	  "Kein Bildtreiber für Export gefunden" },
	{ "label.text.errorLoad",
	  "Fehler beim Öffnen: {0}" },
	{ "label.text.errorSave",
	  "Fehler beim Speichern: {0}" },
	{ "label.text.warningUnknownProperty",
	  "Warnung: unbekannte Eigenschaft:#n#{0}={1}" },
	{ "label.text.warningBadFileFormatVersion",
	  "Warnung: falsche Version beim Dateiformat: {0}#n#" +
	  "(Version {1} erwartet)" },
	{ "label.text.errorNoFontSelected",
	  "Kein Zeichensatz ausgewählt" },
	{ "label.text.errorImgNoDriver",
	  "Kein gültiges Schreibformat wird unterstützt" },
	{ "label.text.queryOverwrite",
	  "Datei existiert bereits.#n#" +
	  "Bestehende Datei überschreiben?" },
	{ "label.text.errorBadParams",
	  "Fehler in den Parametern beim Programmaufruf" },
	{ "label.text.querySaveChanges",
	  "Sollen alle Änderungen gespeichert werden?" },
	{ "label.text.errorMissingResource",
	  "Ressource \"{0}\" nicht gefunden" },
	{ "label.text.errorLocaleAlreadyExists",
	  "Die angegebene Kombination aus Land und Sprache existiert bereits." },
	{ "label.text.queryBlacken",
	  "Sollen alle Symbole in allen Diagrammen#n#" +
	  "unwiderruflich auf schwarz zurückgesetzt werden?" },
	{ "label.text.programTitle",
	  "#PROGRAM_NAME# v#PROGRAM_VERSION#" },
	{ "label.text.about",
	  "#PROGRAM_NAME# v#PROGRAM_VERSION##n#" +
	  "Ein kleines Werkzeug, um Diagramme zur#n#" +
	  "graphemischen Synästhesie zu erzeugen.#n#" +
	  "\u00a9 2006, 2007 by Jürgen Reuter#n#" +
	  "http://www.juergen-reuter.de/software/syngraph.php" },
	{ "label.text.modifiedTrue",
	  "Datei enthält ungesicherte Änderungen" },
	{ "label.text.modifiedFalse",
	  "Datei enthält keine ungesicherten Änderungen" },
	{ "label.text.value",
	  "Wert" },
	{ "label.text.locale",
	  "Land/Sprache" },
	{ "button.text.approve",
	  "Übernehmen" },
	{ "button.mnemonic.approve",
	  "" + KeyEvent.VK_B },
	{ "button.text.cancel",
	  "Abbrechen" },
	{ "button.mnemonic.cancel",
	  "" + KeyEvent.VK_A },
	{ "button.text.reset",
	  "Zurücksetzen" },
	{ "button.mnemonic.reset",
	  "" + KeyEvent.VK_Z },
	{ "button.text.apply",
	  "Anwenden" },
	{ "button.mnemonic.apply",
	  "" + KeyEvent.VK_W },
	{ "button.text.open",
	  "Öffnen" },
	{ "button.mnemonic.open",
	  "" + KeyEvent.VK_F },
	{ "button.text.save",
	  "Speichern" },
	{ "button.mnemonic.save",
	  "" + KeyEvent.VK_S },
	{ "button.text.exit",
	  "Beenden" },
	{ "button.mnemonic.exit",
	  "" + KeyEvent.VK_B },
	{ "button.text.print",
	  "Drucken..." },
	{ "button.mnemonic.print",
	  "" + KeyEvent.VK_D },
	{ "button.text.doPrint",
	  "Drucken" },
	{ "button.mnemonic.doPrint",
	  "" + KeyEvent.VK_D },
	{ "button.text.export",
	  "Exportieren..." },
	{ "button.mnemonic.export",
	  "" + KeyEvent.VK_E },
	{ "button.text.doExport",
	  "Exportieren" },
	{ "button.mnemonic.doExport",
	  "" + KeyEvent.VK_E },
	{ "button.text.info",
	  "Info..." },
	{ "button.mnemonic.info",
	  "" + KeyEvent.VK_I },
	{ "button.text.close",
	  "Schließen" },
	{ "button.mnemonic.close",
	  "" + KeyEvent.VK_S },
	{ "button.text.add",
	  "Hinzufügen" },
	{ "button.mnemonic.add",
	  "" + KeyEvent.VK_H },
	{ "button.text.delete",
	  "Löschen" },
	{ "button.mnemonic.delete",
	  "" + KeyEvent.VK_L },
	{ "button.tooltip.open",
	  "Datei öffnen" },
	{ "button.tooltip.export",
	  "Grafik Exportieren" },
	{ "button.tooltip.print",
	  "Grafik Drucken" },
	{ "button.tooltip.save",
	  "Datei speichern" },
	{ "button.tooltip.exit",
	  "Programm beenden" },
	{ "button.tooltip.printTab",
	  "Diagram drucken" },
	{ "button.tooltip.exportTab",
	  "Diagram als Bild speichern" },
	{ "button.tooltip.tabInfo",
	  "Informationen zum Aufbau dieses Diagramms" },
	{ "button.tooltip.printNotice",
	  "Notiz drucken" },
	{ "button.tooltip.exportNotice",
	  "Notiz als Bild speichern" },
	{ "button.tooltip.applyFont",
	  "Zeichensatz anwenden" },
	{ "button.tooltip.cancelFontDialog",
	  "Dialog Zeichensatz abbrechen" },
	{ "button.tooltip.closeFontDialog",
	  "Dialog Zeichensatz schließen" },
	{ "button.tooltip.closeNotice",
	  "Notizzettel schließen" },
	{ "button.tooltip.approvePreferences",
	  "Änderungen übernehmen" },
	{ "button.tooltip.cancelPreferences",
	  "Änderungen verwerfen" },
	{ "button.tooltip.approveProperties",
	  "Änderungen übernehmen" },
	{ "button.tooltip.cancelProperties",
	  "Änderungen verwerfen" },
	{ "button.tooltip.approveColor",
	  "Farbe übernehmen" },
	{ "button.tooltip.cancelColor",
	  "Farbe verwerfen" },
	{ "button.tooltip.resetColor",
	  "Farbe zurücksetzen" },
	{ "button.tooltip.closeDoc",
	  "Dokumentation schließen" },
	{ "button.tooltip.closeLicence",
	  "Fenster schließen" },
	{ "button.tooltip.addProperty",
	  "{0} hinzufügen" },
	{ "button.tooltip.deleteProperty",
	  "ausgewählte Zeilen löschen" },
	{ "checkbox.tooltip.toolBarCheck",
	  "Werkzeugleiste einblenden / ausblenden" },
	{ "checkbox.tooltip.toolTipsCheck",
	  "Hinweise aktivieren / deaktivieren" },
	{ "checkbox.tooltip.isCrypticCheck",
	  "Kryptifizierte Darstellung ein-/ausschalten" },
	{ "frame.title.errorLaf",
	  "Fehler beim Setzen des Look & Feel" },
	{ "frame.title.colorChooser",
	  "Farbe für Symbol \"{0}\" wählen" },
	{ "frame.title.print",
	  "Drucken" },
	{ "frame.title.exportAs",
	  "Exportieren unter" },
	{ "frame.title.noticeEditor",
	  "Notizzettel" },
	{ "frame.title.preferences",
	  "Einstellungen" },
	{ "frame.title.properties",
	  "Eigenschaften" },
	{ "frame.title.objectProperties",
	  "Eigenschaften für Symbol \"{0}\"" },
	{ "frame.title.font",
	  "Zeichensatz" },
	{ "frame.title.errorInvalidfont",
	  "Ungültiger Zeichensatz" },
	{ "frame.title.errorExport",
	  "Fehler beim Exportieren" },
	{ "frame.title.errorSave",
	  "Fehler beim Speichern" },
	{ "frame.title.errorPrint",
	  "Fehler beim Drucken" },
	{ "frame.title.licence",
	  "Lizenzvereinbarung" },
	{ "frame.title.errorImgWrite",
	  "Fehler beim Speichern eines Bildes" },
	{ "frame.title.errorBadParams",
	  "Ungültige Parameter" },
	{ "frame.title.toolbar",
	  "Werkzeugleiste" },
	{ "frame.title.about",
	  "Über #PROGRAM_NAME#" },
	{ "frame.title.confirm.overwrite",
	  "Datei überschreiben?" },
	{ "frame.title.confirm",
	  "Aktion bestätigen" },
	{ "frame.title.error",
	  "Fehler" },
	{ "frame.title.selectBgColor",
	  "Hintergrundfarbe wählen" },
	{ "frame.title.documentation",
	  "Dokumentation" },
	{ "frame.title.errorLoad",
	  "Fehler beim Öffnen" },
	{ "frame.title.errorIO",
	  "Ein-/Ausgabe-Fehler" },
	{ "frame.title.errorLocaleAlreadyExists",
	  "Land/Sprache existiert bereits" },
	{ "frame.title.saveAs",
	  "Speichern unter" },
	{ "frame.title.open",
	  "Öffnen" },
	{ "frame.title.info",
	  "Information" },
	{ "frame.title.chooseLocale",
	  "Land/Sprache wählen" },
	{ "list.tooltip.fontName",
	  "Name des Zeichensatzes" },
	{ "list.tooltip.fontSize",
	  "Größe des Zeichensatzes in [pt]" },
	{ "list.tooltip.lookAndFeel",
	  "gewünschtes Look & Feel wählen" },
	{ "textfield.tooltip.defaultDocumentAuthor",
	  "Voreinstellung für den Autor des Dokuments" },
	{ "textfield.tooltip.documentTitle",
	  "Titel des Dokuments" },
	{ "textfield.tooltip.documentAuthor",
	  "Autor des Dokuments" },
	{ "textfield.tooltip.previewFont",
	  "Vorschau auf Zeichensatz" },
	{ "textarea.text.noticeEditorInstruction",
	  "Hier beliebigen Text eingeben! (Hinweis: der Inhalt dieses" +
	  "Notizzettels wird beim Speichern nicht gesichert!)" },
	{ "textarea.tooltip.documentComment",
	  "Kommentar zum Dokument" },
	{ "text.messageError",
	  "FEHLER" },
	{ "text.messageWarning",
	  "WARNUNG" },
	{ "text.messageQuestion",
	  "FRAGE" },
	{ "text.messageInfo",
	  "INFORMATION" },
	{ "text.messagePlain",
	  "" },
	{ "message.fileCorrupted",
	  "Fehlerhafte SynGraph-Datei" },
	{ "message.fileCorruptedDetail",
	  "Fehlerhafte SynGraph-Datei: {0}" },
	{ "message.file.tooLarge",
	  "Datei zu groß" },
	{ "message.file.tooManyObjects",
	  "Datei enthält zu viele Objekte" },
	{ "message.file.tooManyTabs",
	  "Datei enthält zu viele Diagramme" },
	{ "message.file.tooManySets",
	  "Datei enthält zu viele Mengendefinitionen" },
	{ "message.file.tooManyFunctions",
	  "Datei enthält zu viele Funktionsdefinitionen" },
	{ "message.invalidFont",
	  "Zeichensatz {0} [{1}, {2}] nicht gefunden" },
	{ "message.danglingObjectRef",
	  "Funktion \"{0}\" referenziert unbekanntes Objekt \"{1}\"" },
	{ "filefilter.text.imgDescription",
	  "Windows Pixelgrafikdateien" },
	{ "resource.documentation",
	  "/helpdoc/introduction_de.html" },
	{ "resource.licence",
	  "/COPYING_de" },
    };

    public Object[][] getContents ()
    {
	return CONTENTS;
    }

    /**
     * Creates a Resources.properties file, using all properties
     * defined in this ListResourceBundle.
     *
     * @param path The file path of the file to create.  If
     *     <code>null</code>, the path takes the name
     *     "Resources_X.properties", where "X" is the String
     *     representation of the locale of this ListResourceBundle.
     */
    public static void createPropertiesFile (String path)
	throws IOException
    {
	if (path == null)
	    path = "Resources_" + LOCALE + ".properties";
	PrintWriter writer = new PrintWriter (path);
	writer.println ("# SynGraph resources for the " +
			LOCALE.getDisplayName () + " locale");
	for (int i = 0; i < CONTENTS.length; i++)
	    writer.println (CONTENTS[i][0] + "=" + CONTENTS[i][1]);
	writer.println ("#");
	writer.println ("# Local Variables:");
	writer.println ("#   coding:utf-8");
	writer.println ("# End:");
	writer.println ("#");
	writer.close ();
    }

    private static void printUsage (PrintStream out)
    {
	out.println ("Usage: org.soundpaint.syngraph.Resources [ <outfile> ]");
	out.println ();
	out.println ("Creates a Resources.properties file, using all");
	out.println ("properties defined in org.soundpaint.syngraph.Resources.");
    }

    public static void main (String[] args) throws IOException
    {
	if (args.length == 0)
	    createPropertiesFile (null);
	else if (args.length == 1)
	    createPropertiesFile (args[0]);
	else
	    printUsage (System.err);
    }
}

/*
 * Local Variables:
 *   coding:utf-8
 * End:
 */
