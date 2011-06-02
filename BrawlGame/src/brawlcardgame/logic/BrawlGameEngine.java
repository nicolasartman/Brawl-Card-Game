package brawlcardgame.logic;

import brawlcardgame.state.ActionTarget;
import brawlcardgame.state.BrawlGameState;
import brawlcardgame.state.Direction;
import brawlcardgame.state.Lane;
import brawlcardgame.state.LaneScore;
import brawlcardgame.state.PlayerID;
import brawlcardgame.BaseCard;
import brawlcardgame.ClearCard;
import brawlcardgame.BrawlCard;
import brawlcardgame.BaseModifierCard;
import brawlcardgame.BrawlCardGameApp;
import brawlcardgame.BrawlCardGameApp.Setting;
import brawlcardgame.BrawlCardGameView;
import brawlcardgame.BrawlCharacter;
import java.util.ArrayList;

/**
 * The central controller class for managing the game as it progresses
 * 
 * @author Nicolas Artman
 */
public final class BrawlGameEngine
{

   private BrawlGameState state;
   private BrawlCardGameView view;
   private static final int kMaxLanes = 3;

   public BrawlGameEngine(BrawlGameState state, BrawlCardGameView view)
   {
      this.state = state;
      this.view = view;
   }

   public void doAction(PlayerAction action)
   {

      // Input check: if the action has a null target, it should not be
      // carried out
      if (action.getLocation() != null)
      {

         // Get the acting player
         PlayerID actingPlayer = action.getPlayerID();

         // If they wish to draw, do so if appropriate
         if (action.getLocation() == ActionTarget.deck)
         {
            state.drawFromPlayerDeck(actingPlayer);
         }
         else if (action.getLocation() == ActionTarget.discard)
         {
            // If their hand is empty, draw a card from their discard,
            if (state.getPlayerHand(actingPlayer) == null)
            {
               state.drawFromPlayerDiscard(actingPlayer);
            } // Otherwise, discard the card that's in their hand
            else
            {
               state.discardPlayerHand(actingPlayer);
            }
         } // They are attempting to play a card in the play area from their hand
         else if (state.getPlayerHand(actingPlayer) != null)
         {
            playCard(actingPlayer, action.getLocation());
         }
      }

      state.notifyObservers();
   }

   private void playCard(PlayerID player, ActionTarget target)
   {
      BrawlCard card = state.getPlayerHand(player);
      // if it's a base card, see if there's room for a base
      if (card.getClass().equals(BaseCard.class) && state.getNumberOfLanes() < kMaxLanes)
      {
         // if there is, add the base on the indicated side by translating
         // the target to left (lane 0) or right (lane 2)
         if (target.getLaneNumber() == 0 || target.getLaneNumber() == 2)
         {
            state.addLane(target.getLaneNumber(), (BaseCard) card);
            // Successful action so clear the card they just played from their hand
            state.clearPlayerHand(player);
         }
      } // The remaining cards have to be played on existing lanes, so 
      // If the target signals a valid unfrozen lane
      else if (state.getNumberOfLanes() > target.getLaneNumber()
          && !state.laneIsFrozen(target.getLaneNumber()))
      {
         // if it's a clear card
         if (card.getClass().equals(ClearCard.class))
         {
            // If they're not trying to clear the middle base or only base, both
            // of which are against the game rules
            if (!(state.getNumberOfLanes() == 1)
                && !(state.getNumberOfLanes() == kMaxLanes
                && target.getLaneNumber() == 1 /* middle lane */))
            {
               // Check base modifiers on the base to ensure none prevent clearing
               boolean baseCanBeCleared = true;
               for (BaseModifierCard modifier : state.getLane(target.getLaneNumber()).
                   getBaseModifiers())
               {
                  baseCanBeCleared &= !modifier.preventsClear();
               }

               if (baseCanBeCleared)
               {
                  // Clear the base and all cards from the desired lane
                  state.removeLane(target.getLaneNumber());
                  state.clearPlayerHand(player);
               }
            }
         } // if it's a base modifier
         else if (card instanceof BaseModifierCard)
         {
            // play the modifier on the base
            state.addBaseModifier((BaseModifierCard) card, target);

            try
            {
               // If there was a card that just got played on, let it react to being played on (Plugin API Point)
               int numberOfModifiers =
                   state.getLane(target.getLaneNumber()).getBaseModifiers().size();
               if (numberOfModifiers > 1)
               {
                  state.getLane(target.getLaneNumber()).getBaseModifiers().
                      get(numberOfModifiers - 2).onPlayedOn(state.getMutableBaseModifiers(target));
               }
               // Allow the card to affect its stack (Plugin API Point)
               card.onPlayed(state.getMutableBaseModifiers(target));
            }
            catch (Exception e)
            {
               System.out.println("Plugin error: " + e.getMessage());
               view.showMessage("Error", "A custom card has caused an error."
                   + " The game will continue, but rules may have been broken"
                   + " due to the malfunctioning card");
            }

            // Since they may have just frozen the last base, do an end game check
            if (allBasesAreFrozen())
            {
               endGame();
            }
            state.clearPlayerHand(player);
         } // All other cards are action cards
         else
         {
            // If possible, play their card on the desired stack
            if (card.canPlayOn(state.getLane(target.getLaneNumber()).getTopCardOfStack(target.
                getDirection())))
            {
               state.addCardToStack(card, target);

               try
               {
                  int stackSize = state.getLane(target.getLaneNumber()).getStack(
                      target.getDirection()).size();
                  // If a card was just played on, let it react (Plugin API Point)
                  if (stackSize > 1)
                  {
                     state.getLane(target.getLaneNumber()).getStack(target.getDirection()).
                         get(stackSize - 2).onPlayedOn(state.getMutableStack(target));
                  }
                  // Allow the card just played to affect the stack (Plugin API Point)
                  card.onPlayed(state.getMutableStack(target));
               }
               catch (Exception e)
               {
                  System.out.println("Plugin error: " + e.getMessage());
                  view.showMessage("Error", "A custom card has caused an error."
                      + " The game will continue, but rules may have been broken"
                      + " due to the malfunctioning card");
               }

               state.clearPlayerHand(player);
            }
         }
      }
   }

   private void endGame()
   {
      // Keeps track of the score for the lane currently being evaluated
      LaneScore currentLaneScore;

      // Total up each lane
      int player1Score = 0, player2Score = 0;

      for (Lane lane : state.getLanes())
      {
         // Create a lane score, which automatically calculates points
         // for the lane based on the stack scores
         currentLaneScore = new LaneScore(lane.getStackPointValue(Direction.up),
             lane.getStackPointValue(Direction.down));

         // Be prepared fo rissues issues from defective plugins
         try
         {
            // Allow base modifiers to modify the result
            for (BaseModifierCard card : lane.getBaseModifiers())
            {
               card.modifyScoreForLane(currentLaneScore);
            }

            // Increment the player scores accordingly
            player1Score += currentLaneScore.getLaneScores().get(PlayerID.one);
            player2Score += currentLaneScore.getLaneScores().get(PlayerID.two);
         }
         catch (Exception e)
         {
            System.out.println("Plugin error: " + e.getMessage());
            view.showMessage("Error", "A custom card has caused an error"
                + " when determining the final score. The final score may"
                + " be incorrect due to the malfunctioning card");
         }
      }

      if (player1Score > player2Score)
      {
         view.showMessage("Game Over", ((BrawlCharacter) BrawlCardGameApp.settings.get(
             Setting.P1Character)).name() + " Wins!");
      }
      else if (player1Score < player2Score)
      {
         view.showMessage("Game Over", ((BrawlCharacter) BrawlCardGameApp.settings.get(
             Setting.P2Character)).name() + " Wins!");
      }
      else
      {
         view.showMessage("Game Over", "The Game is a Tie");
      }
   }

   private boolean allBasesAreFrozen()
   {
      boolean unfrozenLanes = false;

      // If any lane on the playing area is unfrozen, there are unfrozen lanes
      for (Lane lane : state.getLanes())
      {
         unfrozenLanes |= !lane.isFrozen();
      }

      return !unfrozenLanes;
   }
}
