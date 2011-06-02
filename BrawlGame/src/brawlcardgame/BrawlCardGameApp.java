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
   public static JSONObject deckCompositions;
   private ArrayList<String> startupErrors;

   @Override
   public void initialize(String[] args)
   {
      // Initialize the startup errors so they can be all shown at once in a nice scrollable window
      this.startupErrors = new ArrayList<String>();

      // Create a view
      view = new BrawlCardGameView(this);

      // Load in the settings and deck compositions
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
