package brawlcardgame;

import brawlcardgame.BrawlCardGameApp;
import brawlcardgame.BrawlCharacter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import sun.security.jca.GetInstance;

/**
 * Stores the settings for the game
 * 
 * @author Nicolas Artman
 */
public class SettingsManager
{

   private static SettingsManager self = null;
   private final static String settingsFilePath = "settings.properties";
   private static EnumMap<Setting, Object> settings = new EnumMap<Setting, Object>(Setting.class);

   // Exists only to prevent instantiation
   private SettingsManager()
   {
      loadSettings();
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
         BrawlCardGameApp.getApplication().addStartupError("The settings file provided could not"
             + " be found at " + settingsFilePath + " using default settings.");
      }
      catch (Exception exception)
      {
         System.out.println("Failed to read settings, using defaults");
         BrawlCardGameApp.getApplication().addStartupError("The settings file provided could not"
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
                     BrawlCardGameApp.getApplication().addStartupError("<b>Warning</b>:" + value
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
            BrawlCardGameApp.getApplication().addStartupError("Custom setting" + setting.name()
                + " was specified incorrectly and has been ignored.");
         }
      }
   }

   /**
    * Convenience method for accessing a setting. Ensures this is instantiated before getting
    * the setting
    * @param setting The setting to retrieve the corresponding value for
    * @return The value for the requested setting
    */
   public static Object getSettingValue(Setting setting)
   {
      if (self == null)
      {
         self = new SettingsManager();
      }
      return settings.get(setting);
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
}