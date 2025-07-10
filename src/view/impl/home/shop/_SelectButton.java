package view.impl.home.shop;

import controller.shop.SelectButtonController;
import persistance.profile.ProfileManager;
import view.impl._common.buttons.CustomButton;

import java.awt.*;

class _SelectButton extends CustomButton {
    _SelectButton(int creatureNumber) {
        super("", Color.BLACK, null);

        int selectedCharacterId = ProfileManager.getLastProfile().getCurrentCharacterIndex();

        // IF SELECTED
        if (creatureNumber == selectedCharacterId) {
            colorBackground = new  Color(173, 216, 230); // Light Blue
            text = "Selected";
            setToolTipText("Selected creature");
            setEnabled(false);
       }
        // ELSE
        else {
            colorBackground = new Color(144, 238, 144); // Light Green
            text = "Select";
            setToolTipText("Select this creature");
        }
        addActionListener(new SelectButtonController(creatureNumber));
    }
}
