package brawlcardgame.state;

import brawlcardgame.BrawlCharacter;
import brawlcardgame.logic.BrawlDeckLibrary;
import brawlcardgame.BrawlCard;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This class covers the concepts I focused on from chapters 3 and 4.
 * 
 * @author Nicolas Artman
 */
public final class BrawlDeck implements Cloneable {

   private ArrayList<BrawlCard> cards;
   private BrawlCharacter character;

   public BrawlDeck(BrawlCharacter character) {
      this.character = character;
      // Get the cards from the deck library
      this.cards = BrawlDeckLibrary.getDeckForCharacter(character);

   }

   public BrawlDeck(ArrayList<BrawlCard> cards, BrawlCharacter character) {
      this.cards = cards;
      this.character = character;
   }
   
   /*
    * Removes the top card of the deck and returns it, or returns null if the deck is empty
    */
   public BrawlCard drawCard() {
      if (!this.cards.isEmpty()) {
         return this.cards.remove(0);
      } else {
         return null;
      }
   }
   
   public int getSize() {
      return this.cards.size();
   }

   public BrawlCharacter getCharacter() {
      return character;
   }
   
   // Item 5.15: mutability is minimized by only exposing this in ways necessary for testing
   public void setCharacter(BrawlCharacter newCharacter) {
      this.character = newCharacter;
   }

   @Override
   @SuppressWarnings("unchecked")
   // Item 3.11: Clone is only overridden in order to support the defensive-coding of another class
   public Object clone() {
      BrawlDeck theClone;
      
      try {
         theClone = (BrawlDeck) super.clone();
         theClone.cards = new ArrayList<BrawlCard>();
         // Item 8.46: for each loop used
         for (BrawlCard card : this.cards) {
            // Item 5.27: generic method is used with generic type
            theClone.cards.add((BrawlCard)card.clone());
         }
      } catch (CloneNotSupportedException ex) {
         throw new AssertionError();
      }
      
      return theClone;
   }

   public ArrayList<BrawlCard> getCards() {
      ArrayList<BrawlCard> copy = new ArrayList<BrawlCard>();
      for (BrawlCard brawlCard : cards) {
         copy.add((BrawlCard) brawlCard.clone());
      }
      
      return copy;
   }
}
