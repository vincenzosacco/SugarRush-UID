package view.base;

import javax.swing.*;

public abstract class BasePanel extends JPanel {
    public BasePanel() {
        super();
        this.setFocusable(true);
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentShown(ComponentEvent e) {
//                super.componentShown(e);
//                requestFocusInWindow(); // Request focus when the panel is shown
//            }
//        });
        SwingUtilities.invokeLater(this::bindControllers);

    }

    /**
     * @implNote In {@code BasePanel} call {@link #bindControllers() bindControllers} and {@link #requestFocusInWindow() requestFocusInWindow}
     */
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    /** Add listeners.
     * @apiNote This method is called in {@link #addNotify() addNotify}
     */
    protected abstract void bindControllers();

}
