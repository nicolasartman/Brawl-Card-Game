package brawlcardgame;

/**
 * A "Freeze" card, which locks a base so no further cards can be played
 * 
 * @author Nicolas Artman
 */
public final class FreezeCard extends BaseModifierCard {

   public FreezeCard(BrawlCharacter character) {
      super(character);
   }

   @Override
   public String display() {
      return "Freeze!";
   }

   @Override
   public boolean preventsClear() {
      return true;
   }

   @Override
   public String getDescription() {
      return "Freezes a base so no more cards can be played on it or either"
              + "of its stacks. When all bases in play are frozen, the game ends.";
   }
}
