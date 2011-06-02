package brawlcardgame.state;

import brawlcardgame.BrawlCard;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Nicolas Artman
 */
public class Player implements Cloneable {

   private BrawlDeck deck;
   private ArrayList<BrawlCard> discard;
   private BrawlCard hand;

   public int getDeckSize() {
      return deck.getSize();
   }

   public Player(BrawlDeck deck) {
      this.hand = null;
      this.deck = deck;
      this.discard = new ArrayList<BrawlCard>();
   }

   /*
    * Draw the top card of the deck and put it into the player's hand, if possible
    */
   public void drawCardFromDeck() {
      this.hand = this.deck.drawCard();
   }

   public BrawlCard getHand() {
      return hand;
   }

   public void clearHand() {
      this.hand = null;
   }

   public BrawlCard getTopOfDiscard() {
      if (!this.discard.isEmpty()) {
         return (BrawlCard) this.discard.get(0).clone();
      } else {
         return null;
      }
   }

   /*
    * Draw the top card of the discard pile and put it into the player's hand, if possible
    */
   public void drawCardFromDiscard() {
      // Remove the top card from the discard and put it into the player's hand
      this.hand = this.discard.remove(0);
   }

   public void discardHand() {
      // Put the card from the player's hand onto the top of their discard
      this.discard.add(0, hand);
      this.hand = null;
   }

   @Override
   public Object clone() {
      Player copy = new Player((BrawlDeck) this.deck.clone());
      for (BrawlCard card : discard) {
         copy.discard.add((BrawlCard) card.clone());
      }

      copy.hand = this.hand == null ? null : (BrawlCard) this.hand.clone();

      return copy;
   }

   public BrawlDeck getDeck() {
      return (BrawlDeck) deck.clone();
   }
   
   
}
