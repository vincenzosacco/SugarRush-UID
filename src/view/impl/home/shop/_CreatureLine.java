package view.impl.home.shop;

import utils.Resources;
import view.View;
import view.impl._common.buttons.CustomButton;
import view.impl._common.panels.CoinCountPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class _CreatureLine extends JPanel {
    /** Button to buy/select the creature */
    private CustomButton button;
    private final int creautureId;

    _CreatureLine(ImageIcon creatureIcon, int creatureId, int price, boolean bought) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // CREATURE ICON //
        JLabel creatureLabel = new JLabel(creatureIcon);

        // PRICE //
        CoinCountPanel priceLabel = new CoinCountPanel(price);
        // Resize the icon to fit better
//        Image image = coinIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

//        priceLabel.setIcon(coinIcon); // coin icon
        priceLabel.setFont(new Font("Arial", Font.BOLD, 40));
        priceLabel.setBounds(20, 20, 120, 40);
        int spaceCenterRight =  price < 1000 ? 100 : 78;

        // BUY BUTTON //
        this.creautureId = creatureId;
        if (bought) {
            button = new _SelectButton(creatureId);
        }
        else {
            button = new _BuyButton(creatureId, price);
            // another action listener to change the button
            button.addActionListener(this::onBuy);
        }
        button.setPreferredSize(new Dimension(120, 50)); // width, height
        button.setMaximumSize(new Dimension(120, 50));

        // ADD COMPONENTS //
        add(creatureLabel); // left
        add(Box.createHorizontalStrut(30)); // space between left and center
        add(priceLabel); // center
        add(Box.createHorizontalStrut(spaceCenterRight)); // space between center and right
        add(button); // right
    }

    /** Transform the buy button to a select*/
    private void onBuy(ActionEvent e){
        button = new _SelectButton(creautureId);
        button.revalidate();
        button.repaint();
    }
}
