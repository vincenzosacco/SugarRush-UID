package view.impl.home.shop;

import model.game.utils.ShopModel;
import persistance.profile.ProfileManager;
import utils.Resources;
import view.base.AbsViewPanel;
import view.impl._common.buttons.CustomButton;
import view.impl._common.panels.CoinCountPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;;

public class ShopPanel extends AbsViewPanel {

    // an array that idicates which creatures are available in the shop
    private List<Boolean> creatures ;

    private final CoinCountPanel playerCoinsPanel = new CoinCountPanel();

    private final ImageIcon backgroundImage = new ImageIcon(Resources.getImage("/imgs/panels/levels/shopBG.png"));

    private ShopPanel() {
        refreshCreatures();
    }

    private static ShopPanel instance = null;
    public static ShopPanel getInstance(){
        if (instance == null) {
            instance = new ShopPanel();
        }
        return instance;
    }

    public void addCreatures(JPanel contentPanel) {
        addCreatureLine(contentPanel, new ImageIcon(Resources.getImage("/imgs/game/blocks/creature/creature-n.jpg")), 0, creatures.get(0), 0);
        addCreatureLine(contentPanel, new ImageIcon(Resources.getImage("/imgs/game/blocks/creature/creature2.png")), 100, creatures.get(1), 1);
        addCreatureLine(contentPanel, new ImageIcon(Resources.getImage("/imgs/game/blocks/creature/creature3.png")), 200, creatures.get(2), 2);
        addCreatureLine(contentPanel, new ImageIcon(Resources.getImage("/imgs/game/blocks/creature/creature4.png")), 400, creatures.get(3), 3);
        addCreatureLine(contentPanel, new ImageIcon(Resources.getImage("/imgs/game/blocks/creature/creature5.png")), 800, creatures.get(4), 4);
        addCreatureLine(contentPanel, new ImageIcon(Resources.getImage("/imgs/game/blocks/creature/creature6.png")), 1200, creatures.get(5), 5);
    }

    private void addCreatureLine(JPanel parent, ImageIcon creatureIcon, int price, boolean bought, int creatureId) {
        // resize the creature icon to fit better
        Image imageC = creatureIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        creatureIcon = new ImageIcon(imageC);

        JPanel linePanel = new _CreatureLine(creatureIcon, creatureId, price, bought);



        // buy if not bought and select otherwise
//        CustomButton actionButton;
//        if (bought){
//            if (cretureNumber == currentCharacterIndex) {
//                actionButton = new CustomButton("Selected", Color.BLACK, new Color(173, 216, 230)); // Light Blue
//            } else {
//                actionButton = new CustomButton("Select", Color.BLACK, new Color(144, 238, 144)); // Light Green
//                actionButton.setToolTipText("Select this creature");
//                actionButton.addActionListener(e -> {
//                    // Select the creature
//                    ShopModel.selectCreature(cretureNumber);
//                });
//            }
//            linePanel.add(Box.createHorizontalStrut(220)); // space between left and right
//        }
//        else {
//            actionButton = new _BuyButton(cretureNumber, price);
//
//
//        }

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

//    public void updateCoins(int c) {
//        coins = c;
//        playerCoinsPanel.setText(String.valueOf(coins));
//        playerCoinsPanel.revalidate();
//        playerCoinsPanel.repaint();
//        this.revalidate();
//        this.repaint();
//    }

    public void refreshCreatures(){
        this.removeAll();
        setLayout(new BorderLayout());

        creatures = ProfileManager.getLastProfile().getCharacters();

        // Top panel for coin counter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        playerCoinsPanel.setCoins(ProfileManager.getLastProfile().getCoins() );
        // Add vertical space above and under the coinCounterLabel
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(playerCoinsPanel);

        add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        addCreatures(contentPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(128);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

//--------------------------------------- CONTROLLER METHODS -----------------------------------------------------------------------------

    @Override
    protected void bindController() {

    }
}
