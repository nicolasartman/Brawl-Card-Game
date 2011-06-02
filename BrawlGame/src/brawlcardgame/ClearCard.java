package brawlcardgame;

/**
 *
 * @author Nicolas Artman
 */
public class ClearCard extends BrawlCard {

   public ClearCard(BrawlCharacter character) {
      super(Color.none, character);
   }

   @Override
   public boolean canPlayOn(BrawlCard card) {
      return card != null;
   }

   @Override
   public String display() {
      return "Clear";
   }

   @Override
   public String getDescription() {
      return "Removes a base and all cards on it from the playfield. May not"
              + "remove the middle base or the only base.";
   }
   
}
