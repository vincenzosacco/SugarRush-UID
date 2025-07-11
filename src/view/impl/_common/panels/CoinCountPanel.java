package view.impl._common.panels;

import utils.Resources;

import javax.swing.*;
import java.awt.*;

/**
 * General panel to display the number of coins collected in the game next to the coin icon.
 */
public class CoinCountPanel extends JPanel {
    private final JLabel imageLabel;
    private final JLabel countLabel;
    private final ImageIcon coinIcon = new ImageIcon(Resources.getImage("/imgs/icons/coinsImmage.png").getScaledInstance(40, 40, Image.SCALE_SMOOTH));

    public CoinCountPanel() {
        super();
        setLayout(new GridLayout(1, 2)); // 1 row , 2 column
        setOpaque(false); // Make the panel transparent

        // IMAGE LABEL //
        imageLabel = new JLabel(coinIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // TEXT COUNT LABEL //
        countLabel = new JLabel();
        countLabel.setFont(new Font("Arial", Font.BOLD, 24));
        countLabel.setForeground(Color.WHITE); // Make text more visible
        countLabel.setText("0"); // Default text for coins
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // ADD and PUT CONSTRAINS //
        add(imageLabel);
        add(countLabel);
    }

    public CoinCountPanel(int coins){
        this();
        updateCoinCount(coins);
    }



    public void updateCoinCount(int coins) {
        countLabel.setText(String.valueOf(coins)); // Update the label with the number of coins
    }

    public void setTextColor(Color color) {
        countLabel.setForeground(color); // Set the text color of the label
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        // Coin label
//        FontMetrics fm= countLabel.getFontMetrics(countLabel.getFont());
//
//        //        int labelWidth = (int)(w * 0.12);  // 12% of panel width
////        int labelHeight = (int)(h * 0.07); // 7% of panel height
//        int labelWidth= fm.stringWidth(String.valueOf(MAX_COINS)) +coinIcon.getImage().getWidth(this)+10;
//        int labelHeight= fm.getHeight();
//
//        int labelX= (int) (500 * 0.02);
//        int labelY= (int) (500 * 0.05);
//        countLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
//        super.paintComponent(g);
//    }



}
