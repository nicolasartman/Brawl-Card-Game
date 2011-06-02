package brawlcardgame;

import brawlcardgame.state.LaneScore;
import brawlcardgame.state.PlayerID;
import java.util.ArrayList;

/**
 * Doubles the value of a base
 * 
 * @author Nicolas Artman
 */
public class DoubleCard extends BaseModifierCard
{

   public DoubleCard(BrawlCharacter character)
   {
      super(character);
   }

   @Override
   public boolean canPlayOn(BrawlCard card)
   {
      return card != null && card.getClass() == BaseCard.class;
   }

   @Override
   public String display()
   {
      return "Double";
   }

   @Override
   public String getDescription()
   {
      return "Doubles the value of the base it is played on";
   }

   @Override
   public int getValue()
   {
      return super.getValue();
   }

   // Other functions you can override. See the docs for more information.
   @Override
   public void onPlayed(ArrayList<? extends BrawlCard> stack)
   {
      super.onPlayed(stack);
   }

   @Override
   public void onPlayedOn(ArrayList<? extends BrawlCard> stack)
   {
      super.onPlayedOn(stack);
   }

   // Double the value of the base
   @Override
   public void modifyScoreForLane(LaneScore scores)
   {
      scores.getMutableLaneScores().put(PlayerID.one, scores.getMutableLaneScores().get(PlayerID.one)
          * 2);
      scores.getMutableLaneScores().put(PlayerID.two, scores.getMutableLaneScores().get(PlayerID.two)
          * 2);
   }
}
