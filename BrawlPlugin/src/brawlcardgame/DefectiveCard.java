package brawlcardgame;

import java.util.ArrayList;

/**
 *
 * @author Quine
 */
public class DefectiveCard extends BrawlCard {

   public DefectiveCard(BrawlCharacter character) {
      super(Color.none, character);
   }
   
   @Override
   public boolean canPlayOn(BrawlCard card) {
      return true;
   }

   @Override
   public String display() {
      return "DEFECTIVE";
   }

   @Override
   public void onPlayed(ArrayList<? extends BrawlCard> stack) {
      ArrayList<Integer> list = new ArrayList<Integer>();
      list.get(-1); // Bad index so it throws an exception
   }

   @Override
   public String getDescription() {
      return "This is an example defective card that throws an exception when"
              + "used. Beyond that it does nothing besides sit there and be"
              + "useless.";
   }
   
   
   
}
