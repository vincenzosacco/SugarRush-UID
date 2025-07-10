package controller.shop;

import model.game.utils.ShopModel;
import view.impl.home.shop.ShopPanel;
import view.impl.home.shop._CreatureLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuyButtonController implements ActionListener {
    private final int creatureNumber;
    private final int price;

    public BuyButtonController(int creatureNumber, int price) {
        this.creatureNumber = creatureNumber;
        this.price = price;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ShopModel.buyCreature(creatureNumber, price);
        _CreatureLine parent = (_CreatureLine) SwingUtilities.getAncestorOfClass(_CreatureLine.class, (Component)e.getSource());
        parent.transformsToSelect();
        ShopPanel.getInstance().refreshCreatures();
    }
}
