package brawlcardgame.logic;

import brawlcardgame.BrawlCardGameApp;
import brawlcardgame.FreezeCard;
import brawlcardgame.BrawlCard.Color;
import brawlcardgame.SettingsManager.Setting;
import brawlcardgame.BrawlCharacter;
import brawlcardgame.BrawlCard;
import brawlcardgame.SettingsManager;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A static utility class that creates new decks from the deck compositions provided
 * 
 * @author Nicolas Artman
 */
public class BrawlDeckLibrary
{

   private static String kPackagePrefix = "brawlcardgame.";

   @SuppressWarnings("unchecked")
   public static ArrayList<BrawlCard> getDeckForCharacter(BrawlCharacter character)
   {

      ArrayList<BrawlCard> cards = new ArrayList<BrawlCard>();
      // Generic counter variable used in for loops
      int i = 0;
      // The card currently being created and added to the deck
      BrawlCard currCard;
      // The raw JSON data for that card
      JSONObject currCardJSONData;
      // The name of the current card in text form, in case the plugin is malformed, so we can
      // report the most detailed errors possible
      String currCardName;

      // If the decks were read in successfully
      if (BrawlCardGameApp.deckCompositions != null)
      {
         // Get the character's deck composition
         JSONArray deckComposition =
             (JSONArray) BrawlCardGameApp.deckCompositions.get(character.name());

         if (deckComposition == null)
         {
            BrawlCardGameApp.getApplication().addStartupError("Couldn't find deck in deck file for "
                + character.name());
            System.exit(-1);
         }

         // Add each cards to the deck
         for (int currCardIndex = 0; currCardIndex < deckComposition.size(); currCardIndex++)
         {
            // Get the card data
            currCardJSONData = (JSONObject) deckComposition.get(currCardIndex);

            // Card name in case of failure
            currCardName = currCardJSONData.get("card") == null ? "[No card name provided]"
                : (String) currCardJSONData.get("card") + "Card";

            // Create the card itself if possible, or report the reason an issue arises
            try
            {
               Number copiesOfCard = (Number) currCardJSONData.get("count");

               if (copiesOfCard == null)
               {
                  throw new Exception("No 'count' provided for card " + currCardName);
               }

               for (int cardCount = 0; cardCount < copiesOfCard.intValue(); cardCount++)
               {
                  // Create the card from the JSON data
                  currCard =
                      createCard(currCardName, (String) currCardJSONData.get("color"), character);

                  // Success! The new card has been dynamically loaded and can be added to the deck
                  cards.add(currCard);
               }
            }
            catch (ClassNotFoundException ex)
            {
               BrawlCardGameApp.getApplication().addStartupError("class not found on classpath for card "
                   + currCardName
                   + ". Card was omitted from the deck");
            }
            catch (InstantiationException ex)
            {
               BrawlCardGameApp.getApplication().addStartupError(
                   "couldn't instantiate card " + currCardName + ". Card was omitted from the deck");
            }
            catch (IllegalAccessException ex)
            {
               BrawlCardGameApp.getApplication().addStartupError(
                   "unable to access card file for card " + currCardName
                   + ". Card was omitted from the deck");
            }
            catch (IllegalArgumentException ex)
            {
               BrawlCardGameApp.getApplication().addStartupError(
                   "Error: bad value in card " + currCardName + ": " + ex.getMessage()
                   + ". Card was omitted from the deck");
            }
            catch (Exception ex)
            {
               BrawlCardGameApp.getApplication().addStartupError(
                   "Error: " + ex.getMessage() + ". Card was omitted from the deck");
            }
            catch (Throwable thrown)
            {
               BrawlCardGameApp.getApplication().addStartupError(
                   "Error: " + currCardName
                   + "was defined incorrectly. Make sure to put it in package"
                   + " brawlcardgame and make sure it's located in [pluginsDirectory]/brawlcardgame/. "
                   + "The card has been omitted from the deck");
            }
         }

         // Shuffle
         Collections.shuffle(cards);
      }

      // Add the freezes to the end
      for (int freezeCounter = 0; freezeCounter < 3; freezeCounter++)
      {
         cards.add(new FreezeCard(character));
      }

      return cards;
   }

   @SuppressWarnings("unchecked")
   private static BrawlCard createCard(String cardName, String cardColor, BrawlCharacter character)
       throws
       InstantiationException, IllegalAccessException, IllegalArgumentException,
       InvocationTargetException, ClassNotFoundException, Exception
   {
      BrawlCard card;
      Constructor<? extends BrawlCard>[] constructors;

      // Construct the card using the current classpath if possible, then fall 
      // back to the plugin dir if it's not
      try
      {
         constructors = (Constructor<? extends BrawlCard>[]) Class.forName(kPackagePrefix
             + cardName).
             getConstructors();
      }
      catch (Exception ex)
      {
         URLClassLoader loader = new URLClassLoader(new URL[]
             {
                new File((String) SettingsManager.getSettingValue(Setting.pluginDirectory)).toURI().
                toURL()
             });

         System.out.println("Required class for card " + cardName
             + " was not found on the default classpath, falling back to classLoader for"
             + " custom plugins dir " + SettingsManager.getSettingValue(
             Setting.pluginDirectory));
         constructors = (Constructor<? extends BrawlCard>[]) loader.loadClass(kPackagePrefix
             + cardName).getConstructors();

      }

      // A brawl card must only have one constructor, which takes 1 or 2 args
      if (constructors.length == 1 && constructors[0].getParameterTypes().length == 2)
      {
         // if the color is null but required, don't bother trying to instantiate it
         if (cardColor == null)
         {
            throw new Exception("Card " + cardName + " requires a color but none was provided.");
         }

         card = constructors[0].newInstance(Color.valueOf(cardColor.toLowerCase()), character);
      }
      else if (constructors.length == 1 && constructors[0].getParameterTypes().length == 1)
      {
         card = constructors[0].newInstance(character);
      }
      else
      {
         // If the card wasn't created successfully then don't include it in the deck and let the
         // user know
         throw new InstantiationException();
      }

      return card;
   }
}
