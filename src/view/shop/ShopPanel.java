package view.shop;

import controller.ControllerObj;
import model.profile.ProfileManager;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel implements ViewComp {

    // an array that idicates which creatures are available in the shop
    private boolean [] creatures = {true, false, false, false, false, false};

    private int coins;
    private JLabel coinCounterLabel;

    private ImageIcon backgroundImage;

    public ShopPanel(){

        setLayout(new BorderLayout());

        backgroundImage = new ImageIcon(getClass().getResource("/imgs/panels/levels/shopBG.png"));
        //setBackground(Color.green);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        coins = ProfileManager.getLastProfile().getCoins();

        addCreatures(contentPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Make the scroll pane faster
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(128);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void bindController(ControllerObj controller) {

    }

    public void addCreatures(JPanel contentPanel) {

        // Add some space at the top of the panel
        contentPanel.add(Box.createVerticalStrut(50));

        ImageIcon coinIcon = new ImageIcon(getClass().getResource("/imgs/icons/coinsImmage.png"));
        // Resize the icon to fit better
        Image image = coinIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        coinIcon = new ImageIcon(image);
        coinCounterLabel = new JLabel(coinIcon, JLabel.LEFT);
        coinCounterLabel.setFont(new Font("Arial", Font.BOLD, 24));
        coinCounterLabel.setForeground(Color.WHITE); // Make text more visible
        coinCounterLabel.setText(String.valueOf(coins));
        coinCounterLabel.setBounds(20, 20, 120, 40);
        add(coinCounterLabel);

        addCreatureLine(contentPanel, new ImageIcon(getClass().getResource("/imgs/game/blocks/creature/creature-l.jpg")), 0, creatures[0]);
        addCreatureLine(contentPanel, new ImageIcon(getClass().getResource("/imgs/game/blocks/creature/creature2.png")), 100, creatures[1]);
        addCreatureLine(contentPanel, new ImageIcon(getClass().getResource("/imgs/game/blocks/creature/creature3.png")), 200, creatures[2]);
        addCreatureLine(contentPanel, new ImageIcon(getClass().getResource("/imgs/game/blocks/creature/creature4.png")), 400, creatures[3]);
        addCreatureLine(contentPanel, new ImageIcon(getClass().getResource("/imgs/game/blocks/creature/creature5.png")), 800, creatures[4]);
        addCreatureLine(contentPanel, new ImageIcon(getClass().getResource("/imgs/game/blocks/creature/creature6.png")), 1200, creatures[5]);
    }

    private void addCreatureLine(JPanel parent, ImageIcon creatureIcon, int price, boolean bought) {
        JPanel linePanel = new JPanel();
        linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));
        linePanel.setOpaque(false);

        // resize the creature icon to fit better
        Image imageC = creatureIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        creatureIcon = new ImageIcon(imageC);

        JLabel creatureLabel = new JLabel(creatureIcon);

        JLabel priceLabel = new JLabel(String.valueOf(price));
        ImageIcon coinIcon = new ImageIcon(getClass().getResource("/imgs/icons/coinsImmage.png"));
        // Resize the icon to fit better
        Image image = coinIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        coinIcon = new ImageIcon(image);


        linePanel.add(creatureLabel); // left
        linePanel.add(Box.createHorizontalStrut(20)); // space between left and center

        // buy if not bought and select otherwise
        JButton actionButton;
        if (bought){
            actionButton = new JButton("Select");
            actionButton.setToolTipText("Select this creature");
            linePanel.add(Box.createHorizontalStrut(250)); // space between left and right
        } else {
            actionButton = new JButton("Buy");
            actionButton.setToolTipText("Buy this creature");
            priceLabel.setIcon(coinIcon); // coin icon
            priceLabel.setFont(new Font("Arial", Font.BOLD, 50));
            priceLabel.setBounds(20, 20, 120, 40);
            linePanel.add(priceLabel); // center
            linePanel.add(Box.createHorizontalStrut(100)); // space between center and right
        }

        actionButton.setPreferredSize(new Dimension(120, 50)); // width, height
        actionButton.setMaximumSize(new Dimension(120, 50));
        actionButton.setBackground(new Color(144, 238, 144)); // A light green (Medium Aquamarine

        linePanel.add(actionButton); // right

        parent.add(linePanel);
        parent.add(Box.createVerticalStrut(10)); // space between lines
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void updateCoins() {
        this.coins = ProfileManager.getLastProfile().getCoins();
        coinCounterLabel.setText(String.valueOf(coins));
        coinCounterLabel.repaint();
    }
}
