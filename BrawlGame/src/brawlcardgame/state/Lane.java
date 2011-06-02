package brawlcardgame.state;

import brawlcardgame.BaseCard;
import brawlcardgame.BrawlCard;
import brawlcardgame.BaseModifierCard;
import brawlcardgame.FreezeCard;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Observable;

/**
 * A data structure to hold a base and two stacks of cards to play on
 * 
 * @author Nicolas Artman
 */
public class Lane extends Observable {

   private EnumMap<Direction, ArrayList<BrawlCard>> stacks;
   private BaseCard base;
   private ArrayList<BaseModifierCard> modifiers;
   private boolean isFrozen;

   public Lane(BaseCard base) {
      this.base = base;
      this.stacks = new EnumMap<Direction, ArrayList<BrawlCard>>(Direction.class);
      for (Direction dir : Direction.values()) {
         this.stacks.put(dir, new ArrayList<BrawlCard>());
      }
      this.modifiers = new ArrayList<BaseModifierCard>();
      this.isFrozen = false;
   }

   /*
    * Whether or not the lane has been frozen
    */
   public boolean isFrozen() {
      return this.isFrozen;
   }

   /*
    * Returns the top card of the up/down stack from this base, or null if there is none
    */
   public BrawlCard getTopCardOfStack(Direction dir) {
      // Check to make sure there is at least one card on the stack
      if (!this.stacks.get(dir).isEmpty()) {
         // Return a defensively-copied first card on the stack
         int topCardIndex = this.stacks.get(dir).size() - 1;
         return (BrawlCard) this.stacks.get(dir).get(topCardIndex).clone();
      } else {
         return (BrawlCard) this.base.clone();
      }
   }

   /*
    * Gets the base card in this lane, or null if there is none
    */
   public BaseCard getBase() {
      if (this.base != null) {
         return new BaseCard(this.base.getCharacter());
      } else {
         return null;
      }
   }

   /*
    * Adds the card to the top of the stack growing in the given direction
    */
   public void addCardToStack(BrawlCard card, Direction dir) {
      this.stacks.get(dir).add(card);
   }

   public int getStackPointValue(Direction dir) {
      int points = 0;

      for (BrawlCard card : this.stacks.get(dir)) {
         // Total up the points for the stack
         points += card.getValue();
      }

      return points;
   }

   public ArrayList<BrawlCard> getStack(Direction dir) {
      // Return a defensive copy of the stack
      ArrayList<BrawlCard> copy = new ArrayList<BrawlCard>();
      for (BrawlCard card : this.stacks.get(dir)) {
         copy.add((BrawlCard) card.clone());
      }

      return copy;
   }

   // Returns a reference to the original stack for mutation
   ArrayList<BrawlCard> getMutableStack(Direction direction) {
      return this.stacks.get(direction);
   }

   public void addBaseModifier(BaseModifierCard card) {
      this.modifiers.add(card);
      // If it's a freeze
      if (card.getClass() == FreezeCard.class) {
         this.isFrozen = true;
      }
   }
   
   public BaseModifierCard getTopBaseModifier() {
      return (BaseModifierCard)this.modifiers.get(modifiers.size() - 1).clone();
   }

   @Override
   public Object clone() {
      Lane copy = new Lane((BaseCard) this.base.clone());
      EnumMap<Direction, ArrayList<BrawlCard>> copyStacks =
              new EnumMap<Direction, ArrayList<BrawlCard>>(Direction.class);

      // Clone the stacks
      for (Direction dir : Direction.values()) {
         copyStacks.put(dir, new ArrayList<BrawlCard>());
         for (BrawlCard card : stacks.get(dir)) {
            copyStacks.get(dir).add((BrawlCard) card.clone());
         }
      }
      copy.stacks = copyStacks;

      // Clone the modifiers
      for (BaseModifierCard modifierCard : this.modifiers) {
         copy.addBaseModifier(modifierCard);
      }

      // Copy internal frozen state
      copy.isFrozen = this.isFrozen;

      return copy;
   }

   public ArrayList<BaseModifierCard> getBaseModifiers() {
      ArrayList<BaseModifierCard> copy = new ArrayList<BaseModifierCard>();
      for (BaseModifierCard modifierCard : this.modifiers) {
         copy.add((BaseModifierCard) modifierCard.clone());
      }
      return copy;
   }
   
   // Get the original mutable list of base modifiers
   ArrayList<BaseModifierCard> getMutableBaseModifiers() {
      return this.modifiers;
   }
}
