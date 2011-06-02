package brawlcardgame.logic;
import brawlcardgame.BrawlCardGameApp;
import brawlcardgame.SettingsManager;
import brawlcardgame.SettingsManager.Setting;
import brawlcardgame.state.PlayerID;
import brawlcardgame.state.ActionTarget;

/**
 * Represents an action a player is taking based on the key they pressed
 * 
 * @author Nicolas Artman
 */
public class PlayerAction {

   private ActionTarget target;
   private PlayerID playerID;

   public PlayerAction(Character keyPressed) {
      BrawlCardGameApp app = BrawlCardGameApp.getApplication();
      // Determine the player who pressed the key [1]
      char key = keyPressed.charValue();
      // If the key is one of player 1's keys
      if (key == SettingsManager.getSettingValue(Setting.P1Lane1Up) 
              || key == SettingsManager.getSettingValue(Setting.P1Lane2Up)
              || key == SettingsManager.getSettingValue(Setting.P1Lane3Up)
              || key == SettingsManager.getSettingValue(Setting.P1Lane1Down)
              || key == SettingsManager.getSettingValue(Setting.P1Lane2Down)
              || key == SettingsManager.getSettingValue(Setting.P1Lane3Down)
              || key == SettingsManager.getSettingValue(Setting.P1Deck)
              || key == SettingsManager.getSettingValue(Setting.P1Discard)) {
         this.playerID = PlayerID.one;
      } else {
         this.playerID = PlayerID.two;
      }
      // Determine the target referenced
      this.target = getLocationForKey(key);
   }

   public PlayerAction(PlayerID pid, ActionTarget loc) {
      this.playerID = pid;
      this.target = loc;
   }

   private ActionTarget getLocationForKey(char key) {
      ActionTarget loc = null;
      BrawlCardGameApp app = BrawlCardGameApp.getApplication();
      
      if (key == SettingsManager.getSettingValue(Setting.P1Lane1Down) 
              || key == SettingsManager.getSettingValue(Setting.P2Lane1Down)) {
         loc = ActionTarget.lane0Down;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Lane2Down) 
              || key == SettingsManager.getSettingValue(Setting.P2Lane2Down)) {
         loc = ActionTarget.lane1Down;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Lane3Down) 
              || key == SettingsManager.getSettingValue(Setting.P2Lane3Down)) {
         loc = ActionTarget.lane2Down;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Lane1Up) 
              || key == SettingsManager.getSettingValue(Setting.P2Lane1Up)) {
         loc = ActionTarget.lane0Up;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Lane2Up) 
              || key == SettingsManager.getSettingValue(Setting.P2Lane2Up)) {
         loc = ActionTarget.lane1Up;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Lane3Up) 
              || key == SettingsManager.getSettingValue(Setting.P2Lane3Up)) {
         loc = ActionTarget.lane2Up;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Deck) 
              || key == SettingsManager.getSettingValue(Setting.P2Deck)) {
         loc = ActionTarget.deck;
      } else if (key == SettingsManager.getSettingValue(Setting.P1Discard) 
              || key == SettingsManager.getSettingValue(Setting.P2Discard)) {
         loc = ActionTarget.discard;
      }

      return loc;
   }

   public ActionTarget getLocation() {
      return target;
   }

   public PlayerID getPlayerID() {
      return playerID;
   }
}
