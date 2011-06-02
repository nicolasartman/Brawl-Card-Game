package brawlcardgame.state;

/**
 *
 * @author Nicolas Artman
 */
public enum PlayerID {
   one, two;
   
   @Override
   public String toString () {
      return "Player " + this.name();
   }
}
