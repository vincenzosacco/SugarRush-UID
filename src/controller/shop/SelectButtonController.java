package controller.shop;

import model.game.utils.ShopModel;
import view.impl.home.shop.ShopPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectButtonController implements ActionListener {
    private final int creatureNumber;

    public SelectButtonController(int creatureNumber) {
        this.creatureNumber = creatureNumber;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ShopModel.selectCreature(creatureNumber);
        ShopPanel.getInstance().refreshCreatures();
    }
}