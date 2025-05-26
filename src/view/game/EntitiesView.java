package view.game;

import model.game.Constants;

import javax.swing.*;
import java.awt.Image;
import java.util.Objects;

/**
 * Contains view information classes for game entities.
 * Static context, so no instances can be created.
 */
class EntitiesView {
    private static final ImageIcon[] ICONS = new ImageIcon[]{
            new ImageIcon(Objects.requireNonNull(EntitiesView.class.getResource("/wall.jpg"))),
            new ImageIcon(Objects.requireNonNull(EntitiesView.class.getResource("/creature.jpg"))),
            new ImageIcon(Objects.requireNonNull(EntitiesView.class.getResource("/sugar.jpg"))),
            new ImageIcon(Objects.requireNonNull(EntitiesView.class.getResource("/thorns.jpg"))),
            new ImageIcon(Objects.requireNonNull(EntitiesView.class.getResource("/enemy1.jpg")))
    };

    static Image getIcon(Constants.Block block) {
        return switch (block) {
            case WALL -> EntitiesView.ICONS[0].getImage();
            case CREATURE -> EntitiesView.ICONS[1].getImage();
            case SUGAR -> EntitiesView.ICONS[2].getImage();
            case THORNS -> EntitiesView.ICONS[3].getImage();
            case ENEMY1 -> EntitiesView.ICONS[4].getImage();
            case SPACE -> null; // No icon for SPACE block
        };
    }

}

