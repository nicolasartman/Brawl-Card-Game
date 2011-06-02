package brawlcardgame;
import brawlcardgame.state.LaneScore;
import brawlcardgame.state.PlayerID;

/**
 *
 * @author Nicolas Artman
 */
public final class NullCard extends BaseModifierCard {

   public NullCard(BrawlCharacter character) {
      super(character);
   }

   @Override
   public String display() {
      return "Null";
   }

   @Override
   public void modifyScoreForLane(LaneScore scores) {
      scores.getMutableLaneScores().put(PlayerID.one, 0);
      scores.getMutableLaneScores().put(PlayerID.two, 0);
   }

   @Override
   public String getDescription() {
      return "A base that is nulled is worth nothing at the end of the game.";
   }   
}
