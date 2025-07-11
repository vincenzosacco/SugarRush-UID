package model.game.utils;

import persistance.profile.Profile;
import persistance.profile.ProfileManager;

import javax.swing.*;
import java.util.List;

public class ShopModel {
    public static boolean buyCreature(int creatureIndex, int price) {
        Profile profile = ProfileManager.getLastProfile();
        int coins = profile.getCoins();
        List<Boolean> creatures = profile.getCharacters();

        if (coins >= price && !creatures.get(creatureIndex)) {
            profile.sumCoins(-price);
            profile.setCharacters(creatureIndex);
            ProfileManager.saveProfile(profile);
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
        Profile profile = ProfileManager.getLastProfile();
        profile.setCurrentCharacterIndex(creatureIndex);
        ProfileManager.saveProfile(profile);
//        View.getInstance().getHome().shopPanel.refreshCreatures();
    }

}
