package lu.uni.adtool.tools;

import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.ui.TreeDockable;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

/**
 * Class to store program options.
 *
 * @author Piot Kordy
 * @version
 */

// ".config/"+ getClass().getPackage().getImplementationTitle() in linux
// ~/Library/Application Support
// CSIDL_LOCAL_APPDATA
public final class Options {
  public static String       language                  = new String("en");
  public static String       country                   = new String("US");
  /**
   * Debugging messages
   */
  public static boolean      debug_enable              = true;
  /**
   * Indentation level when printing Terms.
   */
  public static Integer      indentLevel               = 2;

  /**
   * logging options
   */
  public static int          log_noLinesSaved          = 1000;
  /**
   * Print options
   */
  public static int          print_noPages             = 1;
  public static boolean      print_perserveAspectRatio = true;
  public static boolean      printview_showPageNumbers = true;
  public static Color        printview_background      = new Color(144, 153, 174);
  public static Color        printview_border          = Color.DARK_GRAY;
  public static Color        printview_shadow          = Color.BLACK;
  public static Color        printview_paper           = Color.WHITE;

  /**
   * The minimal gap between nodes.
   */
  public static int          canv_gapBetweenNodes      = 20;
  /**
   * The height of the level when drawing a tree.
   */
  public static int          canv_gapBetweenLevels     = 50;

  /**
   * Gui opitons
   */
  public static Color        canv_BackgroundColor      = Color.white;
  public static Color        canv_EdgesColor           = Color.black;
  public static Color        canv_ArcColor             = Color.black;
  public static Color        canv_TextColorAtt         = Color.black;
  public static Color        canv_TextColorDef         = Color.black;
  public static Color        canv_FillColorAtt         = Color.white;
  public static Color        canv_FillColorDef         = Color.white;
  public static Color        canv_BorderColorAtt       = Color.red;
  public static Color        canv_BorderColorDef       = Color.green;
  public static Color        canv_EditableColor        = new Color(255, 255, 155);
  public static ShapeType    canv_ShapeAtt             = ShapeType.OVAL;
  public static ShapeType    canv_ShapeDef             = ShapeType.RECTANGLE;
  public static Font         canv_Font                 = new Font("SanSerif", Font.PLAIN, 12);
  public static Color        canv_LabelMarkColor       = new Color(184, 207, 229);
  public static Color        canv_rankRootMark         = new Color(150, 150, 0);
  public static Color        canv_rankNodeMark         = new Color(50, 170, 50);
  public static Color        canv_rankLeafMark         = new Color(150, 255, 150);
  public static ADTNode.Role canv_Defender             = ADTNode.Role.OPPONENT;
  /**
   * Arc size of round rectangle for node.
   */
  public static int          canv_ArcSize              = 10;

  /**
   * Arc padding for drawing arc to mark conjunctive nodes.
   */
  public static int          canv_ArcPadding           = 20;
  public static int          canv_LineWidth            = 2;
  public static boolean      canv_DoAntialiasing       = true;

  public static final double canv_scaleFactor          = 1.1;
  /**
   * options for save/load dialogs
   */
  public static int          save_version              = 3;
  public static boolean      main_saveLayout           = true;
  public static boolean      main_saveDomains          = true;
  public static boolean      main_saveDerivedValues    = false;

  /**
   * Constructs a new instance.
   */
  public static enum ShapeType {
    RECTANGLE, OVAL
  }

  /**
   * Saved options
   */
  // diretrory where preferences are stored
  public static String pref_path;
  // name of the file where layout is saved
  public static String pref_layoutfile;
  public static String fc_save_file;

  public static String getMsg(String msg) {
    return messages.getString(msg);
  }

  public static String getMsg(String msg, String param) {
    Object[] messageArguments = {param};
    formatter.applyPattern(messages.getString(msg));
    return formatter.format(messageArguments);
  }

  public static String getMsg(String msg, String param1, String param2) {
    Object[] messageArguments = {param1, param2};
    formatter.applyPattern(messages.getString(msg));
    return formatter.format(messageArguments);
  }

  public static void saveOptions() {
    // System.out.println("AppFolder: " +
    // com.github.axet.desktop.Desktop.getAppDataFolder());
    Preferences prefs = Preferences.userRoot().node(PREF_PATH);
    prefs.put("pref_path", pref_path);
    prefs.put("layoutfile", pref_layoutfile);
    // prefs.put("maude_command", maude_command);
    // prefs.put("maude_options", maude_options);
    // prefs.putInt("indent_level", indent_level);
    // prefs.putInt("log_no_lines", log_no_lines);
    // prefs.putBoolean("multiline", multiline);
  }

  public static void loadOptions() {
    formatter.setLocale(new Locale(language, country));
    Preferences prefs = Preferences.userRoot().node(PREF_PATH);
    pref_layoutfile = prefs.get("layoutfile", def_layoutfile);
    File app_path = new File(getAppFolder(), "adtool");
    pref_path = prefs.get("pref_path", app_path.getPath());

  }

  public static boolean tryLoadLayout(CControl control) {
    File dir = new File(pref_path);
    if (!dir.exists()) {
      return false;
    }
    File layout = new File(dir, pref_layoutfile + ".xml");
    // File layout = new File(dir, pref_layoutfile );
    if (!layout.exists()) {
      return false;
    }
    try {
      control.readXML(layout);
      // control.read(layout);
      DefaultSingleCDockable dock = (DefaultSingleCDockable) control
          .getSingleDockable(TreeDockable.getUniqueId(1) + TreeDockable.TREEVIEW_ID);
      if (dock != null) {
        dock.toFront();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean saveLayout(CControl control) {
    File dir = new File(pref_path);
    if (!dir.exists()) {
      try {
        dir.mkdir();
      }
      catch (SecurityException se) {
        se.printStackTrace();
        return false;
      }
    }
    try {
      control.write(new File(dir, pref_layoutfile));
      control.writeXML(new File(dir, pref_layoutfile + ".xml"));
    }
    catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static File getAppFolder() {
    if (Platform.isMac()) {
      File home = new File(System.getProperty("user.home"));
      return new File(new File(home, "Library"), "Application Support");
    }
    else if (Platform.isWindows()) {

      String path = System.getenv("LOCALAPPDATA");
      if (path == null) {
        path = System.getenv("APPDATA");
      }
      if (path != null) {
        return new File(path);
      }
      File home = new File(System.getProperty("user.home"));
      return new File(new File(home, "Local Settings"), "Application Data");
    }
    else {
      File home = new File(System.getProperty("user.home"));
      return new File(home, ".config");
    }
  }

  private static ResourceBundle messages       =
      ResourceBundle.getBundle("i18n.messages", new Locale(language, country));
  private static MessageFormat  formatter      = new MessageFormat("");

  private static final String   PREF_PATH      = "adtool/ui/prefs";

  private static String         def_layoutfile = "adtool.layout";
}