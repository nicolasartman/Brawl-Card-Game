package brawlcardgame;

import java.util.ArrayList;

/**
 *
 * @author Nicolas Artman
 */
public final class HoldCard extends BaseModifierCard {

   public HoldCard(BrawlCharacter character) {
      super(character);
   }
   
   @Override
   public String display() {
      return "Hold";
   }

   @Override
   public boolean preventsClear() {
      return true;
   }

   @Override
   public void onPlayedOn(ArrayList<? extends BrawlCard> stack) {
      // Remove self from the stack
      stack.remove(this);
   }

   @Override
   public String getDescription() {
      return "Prevents the base it is played on from being cleared. This card"
              + " gets removed if another base modifier is played the base.";
   }
   
   
   
}
