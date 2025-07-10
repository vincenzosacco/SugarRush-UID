package view.menu;

import utils.Resources;
import view.editor.LevelEditorPanel;
import view.settings.BaseSettingsPanel;
import view.shop.ShopPanel;

import javax.swing.*;
import java.awt.*;

import static config.ViewConfig.BOARD_HEIGHT;
import static config.ViewConfig.BOARD_WIDTH;

public class CustomTabbedPane extends JPanel{
    // CardLayout to switch between views (like tabs)
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final StartMenuPanel startMenuPanel;
    private final LevelEditorPanel levelEditorPanel;
    private final ShopPanel shopPanel;
    private final BaseSettingsPanel baseSettingsPanel;

    // Arrays to store tab buttons and their underline indicators
    JButton[] buttons = new JButton[4];
    JPanel[] underlinePanels = new JPanel[4];

    public CustomTabbedPane(){
        this.startMenuPanel=new StartMenuPanel();
        this.levelEditorPanel= new LevelEditorPanel();
        this.baseSettingsPanel = new BaseSettingsPanel();
        this.shopPanel= new ShopPanel();

        this.cardLayout = new CardLayout(); // This is the CardLayout instance for CustomTabbedPane
        this.contentPanel = new JPanel(this.cardLayout); // This contentPanel uses the newly created cardLayout


        setLayout(new BorderLayout());

        // Tab bar at the bottom with custom icon buttons
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        tabPanel.setBackground(Color.WHITE); // White background for the tab bar

        // Create tab buttons with icons and tooltips

        // Map button with icon
        ImageIcon mapIcon = new ImageIcon(Resources.getImage("/imgs/icons/map.jpg"));
        JButton mapButton = new JButton(mapIcon);
        styleTabButton(mapButton, "Map");

        // Level Editor button
        ImageIcon levelEditorIcon = new ImageIcon(Resources.getImage("/imgs/icons/level_editor.jpg"));
        JButton levelEditorButton = new JButton(levelEditorIcon);
        styleTabButton(levelEditorButton, "Level Editor");

        // Shop button
        ImageIcon shopIcon = new ImageIcon(Resources.getImage("/imgs/icons/shop.jpg"));
        JButton shopButton = new JButton(shopIcon);
        styleTabButton(shopButton, "Shop");

        // Settings button
        ImageIcon settingsIcon = new ImageIcon(Resources.getImage("/imgs/icons/settings.jpg"));

        JButton settingsButton = new JButton(settingsIcon);
        styleTabButton(settingsButton, "Settings");

        // Add buttons to tabPanel with underline wrappers
        tabPanel.add(createTabWithUnderline(mapButton, 0));
        tabPanel.add(createTabWithUnderline(levelEditorButton, 1));
        tabPanel.add(createTabWithUnderline(shopButton, 2));
        tabPanel.add(createTabWithUnderline(settingsButton, 3));



        // Add panels to CardLayout container with unique keys
        contentPanel.add(startMenuPanel, "Start");
        contentPanel.add(levelEditorPanel, "Editor");
        contentPanel.add(shopPanel, "Shop");
        contentPanel.add(baseSettingsPanel, "Settings");

        // Show the initial tab (Map)
        updateTabSelection(0);

        cardLayout.show(contentPanel, "Start");
        contentPanel.revalidate();
        contentPanel.repaint();

        // Define actions for switching tabs
        mapButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "Start");
            updateTabSelection(0);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        levelEditorButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "Editor");
            updateTabSelection(1);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        shopButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "Shop");
            updateTabSelection(2);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        settingsButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "Settings");
            updateTabSelection(3);
            contentPanel.revalidate();
            contentPanel.repaint();
        });


        // Add tab bar and content area to the panel
        this.add(tabPanel, BorderLayout.PAGE_END);    // Tabs at the bottom
        this.add(contentPanel, BorderLayout.CENTER);  // Main content in the center

        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    // Helper method to apply consistent styling to tab buttons
    private void styleTabButton(JButton button, String tooltip) {
        button.setContentAreaFilled(false);   // No background fill
        button.setBorderPainted(false);       // No border
        button.setFocusPainted(false);        // No focus outline
        button.setOpaque(false);              // Transparent
        button.setToolTipText(tooltip);       // Tooltip on hover
    }

    // Creates a tab component with an underline indicator
    private JPanel createTabWithUnderline(JButton button, int index) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);  // Transparent panel

        // Create the underline indicator
        JPanel underline = new JPanel();
        underline.setPreferredSize(new Dimension(button.getPreferredSize().width, 3));
        underline.setBackground(Color.BLACK);   // Underline color
        underline.setVisible(false);            // Hidden by default

        // Store references
        underlinePanels[index] = underline;
        buttons[index] = button;

        // Add button and underline to the tab panel
        panel.add(button, BorderLayout.CENTER);
        panel.add(underline, BorderLayout.PAGE_END);

        return panel;
    }

    // Updates which tab is visually selected (underline visibility)
    private void updateTabSelection(int activeIndex) {
        for (int i = 0; i < underlinePanels.length; i++) {
            underlinePanels[i].setVisible(i == activeIndex);
        }
    }

    public StartMenuPanel getStartMenuPanel() {
        return startMenuPanel;
    }

    public ShopPanel getShopPanel(){return shopPanel;}
}