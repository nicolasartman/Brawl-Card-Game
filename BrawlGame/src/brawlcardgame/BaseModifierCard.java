package brawlcardgame;

import brawlcardgame.state.LaneScore;

/**
 *
 * @author Nicolas Artman
 */
public abstract class BaseModifierCard extends BrawlCard {
   public BaseModifierCard(BrawlCharacter character) {
      super(Color.none, character);
   }

   public boolean canPlayOn(BrawlCard card) {
      return card != null && card.getClass().equals(BaseCard.class);
   }
   
   /**
    * Allows modification of the final (post-calculation) score for the lane
    * @param scores Contains the stack and lane scores for this lane.
    */
   public void modifyScoreForLane(LaneScore scores) {
      // Default: do not modify scores
   }
   
   /**
    * Allows this card to prevent the base from being cleared
    */
   public boolean preventsClear() {
      return false;
   }
}
