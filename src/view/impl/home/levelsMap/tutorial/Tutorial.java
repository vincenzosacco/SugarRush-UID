package view.impl.home.levelsMap.tutorial;

import view.impl._common.buttons.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tutorial extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final CustomButton backButton;
    private final CustomButton nextButton;
    private final CustomButton exitButton;

    private int currentPageIndex = 0;
    private final List<String> pageNames = new ArrayList<>();

    public Tutorial(JDialog parentDialog) {

        setLayout(new BorderLayout());
        setOpaque(false);

        // Use CardLayout to manage multiple tutorial pages
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Add tutorial pages with image and text
        addTutorialPage("/imgs/tutorial/step1.jpg", "Welcome to the tutorial!\n" +
                "In this screen you can use the keyboard arrows to move between pages, or exit by pressing ESC.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Level Selection\n" +
                "There are various levelsMap, sorted by difficulty. You start at level 1, the others are unlocked by completing the previous ones.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Toolbar\n" +
                "At the bottom you will find buttons to access the level editor, the shop and the settings.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Settings\n" +
                "From here you can adjust the volume of music and effects, and consult the game controls.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Pause Menu\n" +
                "During gameplay, press ESC or the pause buttons to open the level objectives menu.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Pause Menu – Functionality\n" +
                "From the menu you can open the settings, exit, restart the level or continue (also with the ENTER key).");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Movement\n" +
                "Use the arrows to move your character.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Warning!\n" +
                "Avoid enemies and traps, or you will lose! Collect coins by completing missions to buy upgrades in the shop.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "If you lose\n" +
                "You can exit with ESC or restart the level by pressing the ‘Restart’ buttons or the ENTER key.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Goal\n" +
                "Win by reaching the final finish line.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Next Levels\n" +
                "After winning, you can immediately move to the next level with the ‘Next’ buttons or by pressing ENTER.");
        addTutorialPage("/imgs/tutorial/step1.jpg", "Have fun!\n" +
                "Have fun and good luck in the game!");

        add(cardPanel, BorderLayout.CENTER);

        // Navigation panel with Back, Next, and Exit buttons at the bottom
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButton = new CustomButton("← Back", Color.BLACK, Color.WHITE);
        nextButton = new CustomButton("Next →", Color.BLACK, Color.WHITE);
        exitButton = new CustomButton("EXIT", Color.WHITE, new Color(220, 53, 69));

        navPanel.add(backButton);
        navPanel.add(nextButton);
        navPanel.add(exitButton);
        add(navPanel, BorderLayout.SOUTH);

        // Setup buttons actions for navigation and exit
        backButton.addActionListener(e -> showPage(currentPageIndex - 1));
        nextButton.addActionListener(e -> showPage(currentPageIndex + 1));
        exitButton.addActionListener(e -> parentDialog.dispose()); // Close the dialog on exit

        updateButtons();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setupKeyBindings();
    }

    /**
     * Adds a tutorial page with an image and text.
     * If image not found, shows placeholder text.
     */
    private void addTutorialPage(String imagePath, String text) {
        JPanel page = new JPanel(new BorderLayout());
        page.setOpaque(false);

        URL imgUrl = getClass().getResource(imagePath);
        JLabel imageLabel;
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            imageLabel = new ResizableImageTutorial(icon.getImage());
        } else {
            imageLabel = new JLabel("[Image not found]", SwingConstants.CENTER);
        }

        // Text area to display tutorial description (non-editable, wrapped)
        JTextArea textArea = new JTextArea(text);
        textArea.setFocusable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setMargin(new Insets(10, 20, 10, 20));

        page.add(imageLabel, BorderLayout.CENTER);
        page.add(textArea, BorderLayout.SOUTH);

        // Add page to CardLayout with unique name
        String name = "page" + pageNames.size();
        cardPanel.add(page, name);
        pageNames.add(name);

        // Trigger component resizing to adapt buttons etc.
        resizeComponents();
    }

    /**
     * Shows the tutorial page at the given index,
     * updates buttons accordingly.
     */
    private void showPage(int index) {
        if (index >= 0 && index < pageNames.size()) {
            currentPageIndex = index;
            cardLayout.show(cardPanel, pageNames.get(index));
            updateButtons();
        }
    }

    /**
     * Enables or disables Back and Next buttons
     * depending on current page position.
     */
    private void updateButtons() {
        backButton.setEnabled(currentPageIndex > 0);
        nextButton.setEnabled(currentPageIndex < pageNames.size() - 1);
    }

    /**
     * Sets up key bindings for keyboard navigation:
     * - Right arrow: next page
     * - Left arrow: previous page
     * - Escape: exit tutorial
     */
    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        // Bind RIGHT arrow key to next page
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "pressRight");
        actionMap.put("pressRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextButton.doClick();
            }
        });

        // Bind LEFT arrow key to previous page
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "pressLeft");
        actionMap.put("pressLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButton.doClick();
            }
        });

        // Bind ESCAPE key to exit tutorial
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "pressExit");
        actionMap.put("pressExit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitButton.doClick();
            }
        });
    }

    /**
     * Adds a component listener to resize buttons dynamically
     * based on the size of the tutorial panel.
     */
    private void resizeComponents(){
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int size = Math.min(getWidth(), getHeight()) / 10;
                size = Math.max(size, 30);

                // Resize exit, next, and back buttons proportionally
                exitButton.setPreferredSize(new Dimension(size*2, size));
                exitButton.revalidate();
                exitButton.repaint();

                nextButton.setPreferredSize(new Dimension(size * 2, size));
                nextButton.revalidate();
                nextButton.repaint();

                backButton.setPreferredSize(new Dimension(size * 2, size));
                backButton.revalidate();
                backButton.repaint();

                revalidate();
                repaint();
            }
        });
    }

    /**
     * Custom paintBorder to draw a rounded black border around the tutorial panel.
     */
    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 30;
        float borderWidth = 2.0f;

        RoundRectangle2D borderOutline = new RoundRectangle2D.Float(
                borderWidth / 2, borderWidth / 2,
                width - borderWidth, height - borderWidth,
                arc, arc
        );

        g2.setStroke(new BasicStroke(borderWidth));
        g2.setColor(Color.BLACK);
        g2.draw(borderOutline);

        g2.dispose();
    }
}
