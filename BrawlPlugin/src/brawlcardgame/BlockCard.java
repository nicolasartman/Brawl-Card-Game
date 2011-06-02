package brawlcardgame;

/**
 *
 * @author Nicolas Artman
 */
public final class BlockCard extends BrawlCard {

   public BlockCard(Color color, BrawlCharacter character) {
      super(color, character);
   }

   @Override
   public boolean canPlayOn(BrawlCard card) {
      return card != null && ((card.getClass().toString().equals(HitCard.class.toString())
          || card.getCharacter().toString().equals(Hit2Card.class.toString())) &&
              card.getColor().equals(this.getColor()));
   }

   @Override
   public String display() {
      return "Block";
   }

   @Override
   public String getDescription() {
      return "Played on a hit of the same color, a block prevents further hits"
              + " from being played on the base. It can be removed by a press.";
   }
   
}
