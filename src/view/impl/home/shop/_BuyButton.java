package view.impl.home.shop;

import controller.shop.BuyButtonController;
import view.impl._common.buttons.CustomButton;

import java.awt.*;

class _BuyButton extends CustomButton {
    _BuyButton(int creatureNumber, int price){
        super("Buy", Color.BLACK, new Color(255, 204, 153)); // Light Orange

        setToolTipText("Buy this creature");
        addActionListener(new BuyButtonController(creatureNumber, price));
    }
}
