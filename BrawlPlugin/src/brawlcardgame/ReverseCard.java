package brawlcardgame;

import brawlcardgame.state.LaneScore;
import brawlcardgame.state.PlayerID;

/**
 *
 * @author Nicolas Artman
 */
public final class ReverseCard extends BaseModifierCard {

   public ReverseCard(BrawlCharacter character) {
      super(character);
   }

   @Override
   public String display() {
      return "Reverse";
   }

   @Override
   public void modifyScoreForLane(LaneScore scores) {
      Integer temp;
      
      // Swap player 1's score with player 2's score, thus reversing the base score
      temp = scores.getLaneScores().get(PlayerID.one);
      scores.getMutableLaneScores().put(PlayerID.one, scores.getLaneScores().get(PlayerID.two));
      scores.getMutableLaneScores().put(PlayerID.two, temp);
   }

   @Override
   public String getDescription() {
      return "The winner of a reversed base is the player with the *least* points"
              + " on their stack. This card stacks (e.g. two reverses cancel each"
              + " other out).";
   }
   
   
   
}
