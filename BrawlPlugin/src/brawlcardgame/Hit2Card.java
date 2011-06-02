package brawlcardgame;

/**
 * Doubles the value of a base
 * 
 * @author Nicolas Artman
 */
public class Hit2Card extends BrawlCard
{

   public Hit2Card(Color color, BrawlCharacter character)
   {
      super(color, character);
   }

   @Override
   public boolean canPlayOn(BrawlCard card)
   {
      // A hit2 can only be played on another hit or hit2 of the same color, not a base
      return card != null && (card.getClass().toString().equals(HitCard.class.toString())
          || card.getClass().toString().equals(Hit2Card.class.toString()))
          && card.getColor().equals(this.getColor());
   }

   @Override
   public String display()
   {
      return "Hit-2";
   }

   @Override
   public String getDescription()
   {
      return "A hit that counts as two hits in one. Can't be played on an empty base.";
   }

   @Override
   public int getValue()
   {
      return 2;
   }
}
