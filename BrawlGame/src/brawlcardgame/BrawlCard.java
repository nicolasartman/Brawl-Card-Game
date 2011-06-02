package brawlcardgame;

import java.util.ArrayList;

/**
 * An abstract base class containing the required properties of all brawl cards.
 * 
 * @author Nicolas Artman
 */

public abstract class BrawlCard implements Cloneable {

   protected Color color;
   protected final BrawlCharacter character;
   
   public BrawlCard(Color color, BrawlCharacter character) {
      this.color = color;
      this.character = character;
   }

   public enum Color {
      // Nice looking rgb values for each color, because the defaults are awful
      red(222, 112, 112), green(105, 197, 108), blue(137, 187, 222), none(245,245,245);
      
      int r, g, b;
      
      Color(int r, int g, int b) {
         this.r = r;
         this.g = g;
         this.b = b;
      }
      
      public java.awt.Color getDisplayColor() {
         return new java.awt.Color(r, g, b);
      }
   }

   public final Color getColor() {
      return color;
   }
   
   public final void setColor(Color color) {
      this.color = color;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 97 * hash + (this.color != null ? this.color.hashCode() : 0);
      hash = 97 * hash + (this.character != null ? this.character.hashCode() : 0);

      return hash;
   }

   public final BrawlCharacter getCharacter() {
      return character;
   }


   /*
    * Determines whether this card is the same as another card (has the same character,
    * color, and card type).
    * @param other The other card
    */
   @Override
   public final boolean equals(Object other) {
      if (other instanceof BrawlCard) {
         BrawlCard otherBrawlCard = (BrawlCard)other;
         return (this.color == otherBrawlCard.color &&
                 this.character == otherBrawlCard.character &&
                 this.getClass().equals(otherBrawlCard.getClass()));
      } else {
         return false;
      }
   }

   @Override
   public final String toString() {
      return this.color.toString() + " " + this.getClass().toString()
              + " (" + this.character + ")";
   }
   
   @Override
   public Object clone() {
      try {
         return (BrawlCard) super.clone();
      } catch (CloneNotSupportedException ex) {
         throw new AssertionError();
      }
   }
   
   /*
    * Gets the point value for the card for tallying up points at the end of a game
    */
   public int getValue() {
      return 0;
   }

   /**
    * Determines if this card can be played on top of the given card. If you
    * override this you MUST do a null check on the card before any further
    * evaluation.
    */
   public abstract boolean canPlayOn(BrawlCard card);
   
   /**
    * A text description of this card suitable for display to the user
    */
   public abstract String display();
   
   /**
    * A brief description of how this card works, so players can learn
    * what it does before starting a game with this card in a deck.
    */
   public abstract String getDescription();
   
   /**
    * Plugin Method: allows the card to modify the stack it was just played on
    * 
    * @param stack The stack the card is being played on (including this card)
    */
   public void onPlayed(ArrayList<? extends BrawlCard> stack) {
      // By default leave the stack as-is
   }
   
   /**
    * Plugin Method: allows the card to modify the stack after another card
    * has been played on top of it.
    * 
    * @param stack The stack of cards this card is part of
    */
   public void onPlayedOn(ArrayList<? extends BrawlCard> stack) {
      // By default leave the stack as-is
   }
}
