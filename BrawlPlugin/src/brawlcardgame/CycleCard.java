package brawlcardgame;

import java.util.ArrayList;

/**
 *
 * @author Nicolas Artman
 */
public class CycleCard extends BrawlCard {

   public CycleCard(BrawlCharacter character) {
      super(Color.none, character);
   }

   @Override
   public boolean canPlayOn(BrawlCard card) {
      return true;
   }

   @Override
   public String display() {
      return "Cycle";
   }

   @Override
   public void onPlayed(ArrayList<? extends BrawlCard> stack) {
      for (BrawlCard card : stack) {
         if (card.getColor() == Color.blue) {
            card.setColor(Color.red);
         } else if (card.getColor() == Color.green) {
            card.setColor(Color.blue);
         } else if (card.getColor() == Color.red) {
            card.setColor(Color.green);
         }
      }
      
      stack.remove(this);
   }

   @Override
   public String getDescription() {
      return "Changes all cards on a stack to the next color in the cycle"
              + " red->green->blue (then back to red). Removed after use.";
   }
   
   
   
}
