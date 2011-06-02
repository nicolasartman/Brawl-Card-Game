/*
 * BrawlCardGameApp.java
 */
package brawlcardgame;

import brawlcardgame.state.BrawlGameState;
import brawlcardgame.logic.BrawlGameEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * The main class of the application.
 */
public class BrawlCardGameApp extends SingleFrameApplication
{

   private BrawlGameState state;
   private BrawlGameEngine controller;
   // The view was made public solely to allow the deck library to report errors when dynamically
   // creating classes in a nice way.
   public static BrawlCardGameView view;
   public static EnumMap<Setting, Object> settings;
   public static JSONObject deckCompositions;
   private final static String settingsFilePath = "settings.properties";
   private ArrayList<String> startupErrors;

   @Override
   public void initialize(String[] args)
   {
      // Initialize the startup errors so they can be all shown at once in a nice scrollable window
      this.startupErrors = new ArrayList<String>();

      // Create a view
      view = new BrawlCardGameView(this);

      // Load in the settings and deck compositions
      this.loadSettings();
      this.loadDeckCompositions();

      // Create a game state and initialize it
      this.state = new BrawlGameState();

      // Create and init a controller
      this.controller = new BrawlGameEngine(state, view);

      // Init View
      view.setController(controller);
      view.setState(state);
      this.state.addObserver(view);

      // Report any startup errors
      showStartupErrors();

      // Ready to go
      view.drawView();
   }

   private void loadDeckCompositions()
   {
      deckCompositions = null;
      
      try
      {
         // Convert it to a JSON object
         deckCompositions =
             (JSONObject) JSONValue.parseWithException(new FileReader(
             "decks.json"));
      }
      catch (FileNotFoundException ex)
      {
         addStartupError(
             "<font color='red'>CRITICAL: Could not locate deck file, the game will probably be no fun."
             + " You should make sure the decks.json file is in the same directory as this jar</font>");
      }
      catch (ParseException ex)
      {
         addStartupError("<font color='red'>CRITICAL: Your deckfile contains syntax errors:"
             + "\nPosition: " + ex.getPosition() + " - unexpected object </font>"
             + ex.getUnexpectedObject().toString());
      }
      catch (Exception ex)
      {
         addStartupError(
             "<font color='red'>CRITICAL: Could not read deck file, the game will probably be no fun."
             + " You should make sure the decks.json file is in the same directory as this jar</font>");
      }
   }

   public enum Setting
   {

      // UI Settings
      showCardsLeftInDeck(true, Boolean.class),
      showCharacterNames(true, Boolean.class),
      // Player 1 Controls
      P1Lane1Up('w', Character.class),
      P1Lane2Up('e', Character.class),
      P1Lane3Up('r', Character.class),
      P1Lane1Down('s', Character.class),
      P1Lane2Down('d', Character.class),
      P1Lane3Down('f', Character.class),
      P1Deck('a', Character.class),
      P1Discard('g', Character.class),
      // Player 2 Controls
      P2Lane1Up('u', Character.class),
      P2Lane2Up('i', Character.class),
      P2Lane3Up('o', Character.class),
      P2Lane1Down('j', Character.class),
      P2Lane2Down('k', Character.class),
      P2Lane3Down('l', Character.class),
      P2Deck('h', Character.class),
      P2Discard(';', Character.class),
      pluginDirectory("plugins/", String.class),
      P1Character(BrawlCharacter.Bennett, BrawlCharacter.class),
      P2Character(BrawlCharacter.Chris, BrawlCharacter.class);
      private Object value;
      private Class<? extends Object> type;

      Setting(Object value, Class<? extends Object> type)
      {
         this.value = type.cast(value);
         this.type = type;
      }

      public Object getDefaultValue()
      {
         return this.value;
      }

      public Class<? extends Object> getValueType()
      {
         return this.type;
      }
   }

   private void loadSettings()
   {
      // The properties file to read in with the settings in it
      Properties settingsFile = new Properties();
      // The value read in from the settings Properties object
      String value = null;
      // The datatype that the value *should* be, and will be cast to if all goes well
      Class<? extends Object> type = null;

      // load config file and process it
      settings = new EnumMap<Setting, Object>(Setting.class);
      // Init with default settings
      for (Setting setting : Setting.values())
      {
         settings.put(setting, setting.getDefaultValue());
      }

      // Read in all of the settings from the settings file
      try
      {
         FileInputStream sf = new FileInputStream(new File(settingsFilePath));
         settingsFile.load(sf);
      }
      catch (FileNotFoundException fnf)
      {
         addStartupError("The settings file provided could not"
             + " be found at " + settingsFilePath + " using default settings.");
      }
      catch (Exception exception)
      {
         System.out.println("Failed to read settings, using defaults");
         addStartupError("The settings file provided could not"
             + " be used. Default settings have been used instead.");
         return;
      }

      // Take each setting from the Properties object and convert it to the correct
      // datatype, then put it in 
      for (Setting setting : Setting.values())
      {
         try
         {
            value = settingsFile.getProperty(setting.name());
            type = setting.getValueType();

            // If the setting was found in the settings file
            if (value != null)
            {
               // Attempt to parse out the correct data from the value and alter the default setting
               // NOTE: I cannot figure out a more elegant way to do this even with heterogenous
               // container strategies more advanced than what I'm currently using
               if (type.equals(Character.class))
               {
                  settings.put(setting, Character.class.cast(value.charAt(0)));
               }
               else if (type.equals(Integer.class))
               {
                  settings.put(setting, Integer.valueOf(value));
               }
               else if (type.equals(Boolean.class))
               {
                  settings.put(setting, Boolean.valueOf(value));
               }
               else if (type.equals(BrawlCharacter.class))
               {
                  try
                  {
                     BrawlCharacter character = BrawlCharacter.valueOf(value);
                     settings.put(setting, character);
                  }
                  catch (Exception ex)
                  {
                     addStartupError("<b>Warning</b>:" + value
                         + " is not a valid character. You may have meant "
                         + getClosestCharacter(value.toString()).name()
                         + " so that character will be used. Please make sure there is a valid"
                         + " character for each player in the settings file.");
                     // Use the closest character
                     settings.put(setting, getClosestCharacter(value.toString()));
                  }

               }
               // Other values can just be left as strings
               else
               {
                  settings.put(setting, value);
               }
            }
         }
         catch (Exception e)
         {
            System.out.println("Error reading setting " + setting.name());
            addStartupError("Custom setting" + setting.name()
                + " was specified incorrectly and has been ignored.");
         }
      }
   }

   private BrawlCharacter getClosestCharacter(String toName)
   {
      // it will ALWAYS find a distance less than Integer.MAX_VALUE
      BrawlCharacter closestCharacter = null;
      int currBestLevDist = Integer.MAX_VALUE;
      // Scan each character and report the closest
      for (BrawlCharacter currCharacter : BrawlCharacter.values())
      {
         // If this character's name is closer to what they entered than previous best, record it
         if (StringUtils.getLevenshteinDistance(toName, currCharacter.name()) < currBestLevDist)
         {
            closestCharacter = currCharacter;
            currBestLevDist = StringUtils.getLevenshteinDistance(toName, currCharacter.name());
         }
      }

      return closestCharacter;
   }

   public Object getSettingValue(Setting setting)
   {
      return settings.get(setting);
   }

   /**
    * At startup create and show the main frame of the application.
    */
   @Override
   public void startup()
   {
      show(view);
   }

   /**
    * This method is to initialize the specified window by injecting resources.
    * Windows shown in our application come fully initialized from the GUI
    * builder, so this additional configuration is not needed.
    */
   @Override
   public void configureWindow(java.awt.Window root)
   {
   }

   /**
    * A convenient static getter for the application instance.
    * @return the instance of BrawlCardGameApp
    */
   public static BrawlCardGameApp getApplication()
   {
      return Application.getInstance(BrawlCardGameApp.class);
   }

   /**
    * Main method launching the application.
    */
   public static void main(String[] args)
   {
      for (String string : args)
      {
         System.out.println(string);
      }
      launch(BrawlCardGameApp.class, args);
   }

   public void addStartupError(String message)
   {
      startupErrors.add(message);
   }

   private void showStartupErrors()
   {
      if (this.startupErrors.size() > 0)
      {
         view.showStartupErrors(startupErrors);
      }
   }
}
