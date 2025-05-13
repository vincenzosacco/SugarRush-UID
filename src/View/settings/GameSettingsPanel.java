package View.settings;

import View.AbsView;

import java.awt.*;

/**
 * Panel for game settings
 */
public class GameSettingsPanel extends AbsView {
    private boolean isOpen = false;

    public GameSettingsPanel(){
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.WHITE);

        addComponents();

        this.setFocusable(true);
        this.setVisible(false);
        this.requestFocusInWindow();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    /** add all components to this panel */
    private void addComponents(){
        Label languageLabel = new Label("Language");
        this.add(languageLabel);
    }

    @Override
    protected void draw(Graphics g) {

    }
}
