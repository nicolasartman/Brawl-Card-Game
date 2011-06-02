package brawlcardgame;

/**
 * Represents a brawl "hit" card, which is placed on a base/hit and determines
 * who wins it (hits on your side vs hits on the opponent's side).
 *
 * @author Nicolas Artman
 */
public final class HitCard extends BrawlCard
{
   
   public HitCard(Color color, BrawlCharacter character)
   {
      super(color, character);
   }
   
   public boolean canPlayOn(BrawlCard card)
   {
      return card != null && (card.getClass().equals(BaseCard.class)
          || ((card.getClass().toString().equals(HitCard.class.toString()) || card.getClass().
          toString().equals(Hit2Card.class.toString())) && this.getColor().equals(card.getColor())));
   }
   
   @Override
   public String display()
   {
      return "Hit";
   }
   
   @Override
   public int getValue()
   {
      return 1;
   }
   
   @Override
   public String getDescription()
   {
      return "Played on an empty base or a hit of the same color, this card"
          + "counts as 1 point for whichever stack its played on. Whichever"
          + "stack has more points at the end of the game determines the winner"
          + "of a base (though this may be altered by base modifiers).";
   }
}
