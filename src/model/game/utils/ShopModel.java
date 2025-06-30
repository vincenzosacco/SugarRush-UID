package model.game.utils;

import model.profile.Profile;
import model.profile.ProfileManager;
import view.View;

import javax.swing.*;
import java.util.List;

public class ShopModel {

    private static int coins;
    private static List<Boolean> creatures;

    public static boolean buyCreature(int creatureIndex, int price) {

        Profile profile = ProfileManager.loadLastProfile();
        coins = profile.getCoins();
        creatures = profile.getCharacters();

        if (coins >= price && !creatures.get(creatureIndex)) {
            profile.setCoins(profile.getCoins() - price);
            profile.setCharacters(creatureIndex);
            ProfileManager.saveProfile(profile);
            View.getInstance().getCustomTabbedPane().shopPanel.updateCoins(profile.getCoins());
            View.getInstance().getCustomTabbedPane().shopPanel.refreshCreatures();
            return true;
        }else if (coins < price) {
            JOptionPane.showMessageDialog(
                    null,
                    "You do not have enough coins to buy this character.",
                    "Insufficient Coins",
                    JOptionPane.WARNING_MESSAGE
            );
        }
        return false;
    }

    // Select a creature by index
    public static void selectCreature(int creatureIndex) {
        Profile profile = ProfileManager.loadLastProfile();
        profile.setCurrentCharacterIndex(creatureIndex);
        ProfileManager.saveProfile(profile);
        View.getInstance().getCustomTabbedPane().shopPanel.refreshCreatures();
    }

}
