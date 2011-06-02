package brawlcardgame;

import java.util.ArrayList;

/**
 * A brawl "base" card, upon which all other cards are played during a match.
 *
 * @author Nicolas Artman
 */
public final class BaseCard extends BrawlCard {


   public BaseCard(BrawlCharacter character) {
      super(Color.none, character);
   }

   // A base card can only be put in an empty spot, there are no cards it can
   // be played on top of
   public boolean canPlayOn(BrawlCard card) {
      return false;
   }

   @Override
   public String display() {
      return "Base";
   }

   @Override
   public String getDescription() {
      return "Can be played on the left or right of the field as long as there"
              + " aren't already 3 bases. Creates a new lane with this base"
              + " and an empty stack in each player's direction to play on."
              + " At the end of the game, whoever has the most points* on this"
              + " base gets it, and the playe with the most bases* in their favor"
              + " wins the game. *The end game values may be altered by base"
              + " modifiers.";
   }
}
