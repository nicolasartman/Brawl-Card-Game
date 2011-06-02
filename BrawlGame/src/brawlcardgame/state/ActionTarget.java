package brawlcardgame.state;

import brawlcardgame.state.Direction;

/**
 * Represents a location in a player's hand/deck/discard or on the play field
 * that may be targeted for an action.
 * 
 * @author Nicolas Artman
 */
public enum ActionTarget {

   lane0Up(0, Direction.up),
   lane0Down(0, Direction.down),
   lane1Up(1, Direction.up),
   lane1Down(1, Direction.down),
   lane2Up(2, Direction.up),
   lane2Down(2, Direction.down),
   deck, discard;

   private int lane;
   private Direction direction;

   ActionTarget() {
      // Value for no lane
      this.lane = -1;
   }

   ActionTarget(int lane, Direction direction) {
      this.lane = lane;
      this.direction = direction;
   }

   public int getLaneNumber() {
      return lane;
   }
   
   public Direction getDirection() {
      return direction;
   }
}