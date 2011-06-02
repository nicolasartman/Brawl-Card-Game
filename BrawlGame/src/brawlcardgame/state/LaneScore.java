package brawlcardgame.state;

import java.util.EnumMap;

/**
 * A simple wrapper class for the end game score for a lane to allow
 * plugins to modify it
 * 
 * @author Nicolas Artman
 */
public class LaneScore {
   // The total number of points a stack is valued at in each direction
   // This determines who wins the lane
   private EnumMap<PlayerID, Integer> stackValues;
   // A lane score value represents the actual value of the lane
   // when tallying up who has the most. In standard brawl a lane is almost
   // always worth one, but in this enhanced version plugins can change that
   private EnumMap<PlayerID, Integer> laneScores;
   
   public LaneScore(int player1StackValue, int player2StackValue) {
       stackValues = new EnumMap<PlayerID, Integer>(PlayerID.class);
       laneScores = new EnumMap<PlayerID, Integer>(PlayerID.class);
       
       stackValues.put(PlayerID.one, player1StackValue);
       stackValues.put(PlayerID.two, player2StackValue);
       
       // Init lane scores to 0
       laneScores.put(PlayerID.one, 0);
       laneScores.put(PlayerID.two, 0); 
       
       // Calculate winner based on the stack values numbers
       if (stackValues.get(PlayerID.one) > stackValues.get(PlayerID.two)) {
          laneScores.put(PlayerID.one, 1);
       } else if (stackValues.get(PlayerID.one) < stackValues.get(PlayerID.two)) {
          laneScores.put(PlayerID.two, 1);
       }
   }
   
   /**
    * Gets the mutable stack values calculated for the lane.
    * Any changes to these values will be reflected in the final
    * game calculations.
    * 
    * IMPORTANT: if you wish to modify the laneScores based on a plugin's
    * mutation of these values, that must be done manually - no internal
    * recomputation is done on the stack values if you modify them externally.
    * 
    * @return The stack point values that were calculated for the lane
    */
   public EnumMap<PlayerID, Integer> getMutableStackValues() {
      return this.stackValues;
   }
   
   /**
    * Gets the mutable lane scores for this lane. These scores are added up
    * for each player at the end of the game to determine the winner.
    * 
    * @return The lane scores for each player for this lane (initially
    * 0 for one player and 1 for the other).
    */
   public EnumMap<PlayerID, Integer> getMutableLaneScores() {
      return this.laneScores;
   }

   public EnumMap<PlayerID, Integer> getLaneScores() {
      return this.laneScores.clone();
   }
}
