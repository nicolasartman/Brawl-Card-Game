package brawlcardgame;

import java.util.ArrayList;

/**
 *
 * @author Nicolas Artman
 */
public final class PressCard extends BrawlCard {

   public PressCard(BrawlCharacter character) {
      super(Color.none, character);
   }

   @Override
   public boolean canPlayOn(BrawlCard card) {
      return card != null && card.getClass().toString().equals(BlockCard.class.toString());
   }

   @Override
   public String display() {
      return "Press";
   }

   @Override
   public void onPlayed(ArrayList<? extends BrawlCard> stack) {
      // Remove the block card under this card
      stack.remove(stack.size() - 2);
      // Remove this card
      stack.remove(this);
   }

   @Override
   public String getDescription() {
      return "Can only be played on a block. Removes the block (and is then"
              + " removed itself).";
   }
   
   
   
}
