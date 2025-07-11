package view.impl.home.shop;

import persistance.profile.ProfileManager;
import utils.Resources;
import view.base.BasePanel;
import view.impl._common.panels.CoinCountPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

;

public class ShopPanel extends BasePanel {

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

    public void refreshCreatures(){
        this.removeAll();
        setLayout(new BorderLayout());

        creatures = ProfileManager.getLastProfile().getCharacters();

        // Top panel for coin counter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        playerCoinsPanel.updateCoinCount(ProfileManager.getLastProfile().getCoins() );
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

        add(Box.createHorizontalStrut((int) (getWidth()*0.1)), BorderLayout.WEST);
        add(Box.createHorizontalStrut((int) (getWidth()*0.1)), BorderLayout.EAST);
        
        revalidate();
        repaint();
    }
//--------------------------------------- OVERRIDE SWING ----------------------------------------------------------------------------------
    @Override
    public void addNotify() {
        // ADD space between BoxLayout.CENTER and LEFT - RIGHT ("compress CENTER panel")
        add(Box.createHorizontalStrut((int) (getWidth()*0.1)), BorderLayout.WEST);
        add(Box.createHorizontalStrut((int) (getWidth()*0.1)), BorderLayout.EAST);
        super.addNotify(); // <-- this request focus
    }

//--------------------------------------- ABSTRACT OVERRIDEs ----------------------------------------------------------------------------------
    @Override
    protected void bindControllers(){

    }


}
