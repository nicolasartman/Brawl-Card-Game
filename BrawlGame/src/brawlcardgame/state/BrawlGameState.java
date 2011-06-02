package brawlcardgame.state;

import brawlcardgame.BrawlCharacter;
import brawlcardgame.BaseCard;
import brawlcardgame.BrawlCard;
import brawlcardgame.BaseModifierCard;
import brawlcardgame.BrawlCardGameApp;
import brawlcardgame.BrawlCardGameApp.Setting;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Observable;

/**
 *
 * @author Nicolas Artman
 */
public final class BrawlGameState extends Observable {

   private EnumMap<PlayerID, Player> players;
   private ArrayList<Lane> lanes;

   public BrawlGameState() {
      this.startNewGame();
   }

   public void startNewGame() {
      // Initialize the players
      this.players = new EnumMap<PlayerID, Player>(PlayerID.class);
      this.players.put(PlayerID.one, new Player(new BrawlDeck((BrawlCharacter)BrawlCardGameApp.settings.get(Setting.P1Character))));
      this.players.put(PlayerID.two, new Player(new BrawlDeck((BrawlCharacter)BrawlCardGameApp.settings.get(Setting.P2Character))));

      // Initialize the game board - there are 3 lanes in any game of brawl
      // the board starts with two bases
      this.lanes = new ArrayList<Lane>();
      lanes.add(new Lane(new BaseCard(BrawlCharacter.Darwin))); // char is irrelevant for starting bases
      lanes.add(new Lane(new BaseCard(BrawlCharacter.Darwin)));
   }

   /*
    * For getting the number of lanes currently in the play area
    */
   public int getNumberOfLanes() {
      return lanes.size();
   }

   /*
    * Get a copy of the specified lane
    */
   public Lane getLane(int laneNumber) {
      if (lanes.size() > laneNumber) {
         return lanes.get(laneNumber);
      } else {
         return null;
      }
   }

   // Since this will only be used when playing a new base, an int is used
   // to denote lane 0 (left) or 2 (right) for brevity
   public void addLane(int newLaneLocation, BaseCard base) {
      final int left = 0, right = 2;

      if (newLaneLocation == left) {
         this.lanes.add(0, new Lane(base));
      } else if (newLaneLocation == right) {
         this.lanes.add(new Lane(base));
      } else {
         // Debug
         System.out.println("Warning: attempt to add base to bad lane number");
      }

      setChanged();
   }

   public void removeLane(int lane) {
      this.lanes.remove(lane);

      setChanged();
   }

   public ArrayList<Lane> getLanes() {
      ArrayList<Lane> copy = new ArrayList<Lane>();
      for (Lane lane : lanes) {
         copy.add((Lane) lane.clone());
      }

      return copy;
   }

   public void addCardToStack(BrawlCard card, ActionTarget loc) {
      this.lanes.get(loc.getLaneNumber()).addCardToStack(card, loc.getDirection());
      setChanged();
   }

   public ArrayList<BrawlCard> getMutableStack(ActionTarget target) {
      return this.lanes.get(target.getLaneNumber()).getMutableStack(target.getDirection());
   }

   public ArrayList<BaseModifierCard> getMutableBaseModifiers(ActionTarget target) {
      return this.lanes.get(target.getLaneNumber()).getMutableBaseModifiers();
   }

   public void addBaseModifier(BaseModifierCard card, ActionTarget loc) {
      lanes.get(loc.getLaneNumber()).addBaseModifier(card);
      setChanged();
   }

   public EnumMap<PlayerID, Player> getPlayers() {
      EnumMap<PlayerID, Player> copy = new EnumMap<PlayerID, Player>(PlayerID.class);

      for (PlayerID pid : PlayerID.values()) {
         copy.put(pid, (Player) players.get(pid).clone());
      }

      return copy;
   }

   public void drawFromPlayerDeck(PlayerID pid) {
      if (players.get(pid).getHand() == null
              && players.get(pid).getDeckSize() > 0) {
         players.get(pid).drawCardFromDeck();
         setChanged();
      }
   }

   public void drawFromPlayerDiscard(PlayerID pid) {
      // If their hand is empty and their discard is not
      if (players.get(pid).getHand() == null
              && players.get(pid).getTopOfDiscard() != null) {
         players.get(pid).drawCardFromDiscard();

         setChanged();
      }
   }

   public void discardPlayerHand(PlayerID pid) {
      if (players.get(pid).getHand() != null) {
         players.get(pid).discardHand();
         setChanged();
      }
   }

   public BrawlCard getPlayerHand(PlayerID pid) {
      return players.get(pid).getHand() == null
              ? null : (BrawlCard) players.get(pid).getHand().clone();
   }

   public void clearPlayerHand(PlayerID pid) {
      if (players.get(pid).getHand() != null) {
         players.get(pid).clearHand();
         setChanged();
      }
   }

   public boolean laneIsFrozen(int laneNumber) {
      return laneNumber < lanes.size() && lanes.get(laneNumber).isFrozen();
   }
}
