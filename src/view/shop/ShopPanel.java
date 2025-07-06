package view.shop;

import controller.ControllerObj;
import model.game.utils.ShopModel;
import model.profile.ProfileManager;
import view.ViewComp;
import view.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static config.ModelConfig.MAX_COINS;

public class ShopPanel extends JPanel implements ViewComp {

    // an array that idicates which creatures are available in the shop
    private List<Boolean> creatures ;
    private int currentCharacterIndex;

    private int coins;
    private JLabel coinCounterLabel;

    private final ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/panels/levels/shopBG.png")));;

    public ShopPanel() {refreshCreatures();}

    @Override
    public void bindController(ControllerObj controller) {

    }

    public void addCreatures(JPanel contentPanel) {

        addCreatureLine(contentPanel, new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/game/blocks/creature/creature-l.jpg"))), 0, creatures.get(0), 0);
        addCreatureLine(contentPanel, new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/game/blocks/creature/creature2.png"))), 100, creatures.get(1), 1);
        addCreatureLine(contentPanel, new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/game/blocks/creature/creature3.png"))), 200, creatures.get(2), 2);
        addCreatureLine(contentPanel, new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/game/blocks/creature/creature4.png"))), 400, creatures.get(3), 3);
        addCreatureLine(contentPanel, new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/game/blocks/creature/creature5.png"))), 800, creatures.get(4), 4);
        addCreatureLine(contentPanel, new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/game/blocks/creature/creature6.png"))), 1200, creatures.get(5), 5);
    }

    private void addCreatureLine(JPanel parent, ImageIcon creatureIcon, int price, boolean bought, int cretureNumber) {
        JPanel linePanel = new JPanel();
        linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));
        linePanel.setOpaque(false);

        // resize the creature icon to fit better
        Image imageC = creatureIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        creatureIcon = new ImageIcon(imageC);

        JLabel creatureLabel = new JLabel(creatureIcon);

        JLabel priceLabel = new JLabel(String.valueOf(price));
        ImageIcon coinIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/coinsImmage.png")));
        // Resize the icon to fit better
        Image image = coinIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        coinIcon = new ImageIcon(image);


        linePanel.add(creatureLabel); // left
        linePanel.add(Box.createHorizontalStrut(30)); // space between left and center

        // buy if not bought and select otherwise
        CustomButton actionButton;
        if (bought){
            if (cretureNumber == currentCharacterIndex) {
                actionButton = new CustomButton("Selected", Color.BLACK, new Color(173, 216, 230)); // Light Blue
            } else {
                actionButton = new CustomButton("Select", Color.BLACK, new Color(144, 238, 144)); // Light Green
                actionButton.setToolTipText("Select this creature");
                actionButton.addActionListener(e -> {
                    // Select the creature
                    ShopModel.selectCreature(cretureNumber);
                });
            }
            linePanel.add(Box.createHorizontalStrut(220)); // space between left and right
        }
        else {
            actionButton = new CustomButton("Buy", Color.BLACK, new Color(255, 204, 153)); // Light Orange
            actionButton.setToolTipText("Buy this creature");
            actionButton.addActionListener(e -> {
                // Buy the creature
                ShopModel.buyCreature(cretureNumber, price); //FIXME questa view è controllata da un Model e non da un Controller. SBAGLIATO
            });
            priceLabel.setIcon(coinIcon); // coin icon
            priceLabel.setFont(new Font("Arial", Font.BOLD, 40));
            priceLabel.setBounds(20, 20, 120, 40);
            linePanel.add(priceLabel); // center
            if (price<1000) {
                linePanel.add(Box.createHorizontalStrut(100)); // space between center and right
            }
            else{
                linePanel.add(Box.createHorizontalStrut(78)); // space between center and right
            }
        }

        actionButton.setPreferredSize(new Dimension(120, 50)); // width, height
        actionButton.setMaximumSize(new Dimension(120, 50));

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

    public void updateCoins(int c) {
        coins = c;
        coinCounterLabel.setText(String.valueOf(coins));
        coinCounterLabel.revalidate();
        coinCounterLabel.repaint();
        this.revalidate();
        this.repaint();
    }

    public void refreshCreatures(){
        this.removeAll();
        setLayout(new BorderLayout());

        creatures = ProfileManager.getLastProfile().getCharacters();
        currentCharacterIndex = ProfileManager.getLastProfile().getCurrentCharacterIndex();
        coins = ProfileManager.getLastProfile().getCoins();

        // Top panel for coin counter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        ImageIcon coinIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/coinsImmage.png")));
        Image image = coinIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        coinIcon = new ImageIcon(image);
        coinCounterLabel = new JLabel(coinIcon, JLabel.LEFT);
        coinCounterLabel.setFont(new Font("Arial", Font.BOLD, 24));
        coinCounterLabel.setForeground(Color.WHITE);
        coinCounterLabel.setText(String.valueOf(coins));
        // Add vertical space above and under the coinCounterLabel
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(coinCounterLabel);

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
}
