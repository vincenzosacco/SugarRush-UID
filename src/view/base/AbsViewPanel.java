
package view.base;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Represent a view component in the MVC architecture.
 * This abstract class is intended to be extended by main Panels of the view layer,
 * such as it provides a common structure.
 * @apiNote is not necessary to extend this class for every JPanel in the view package.
 */
public abstract class AbsViewPanel extends BasePanel {
    /**
     * Default constructor for the view panel.
     * Calls {@link #bindController()} to set up the controller for this view.
     */
    public AbsViewPanel(){
        super();
        SwingUtilities.invokeLater(this::bindController);
    }

    /**
     * Binds the controller to the view component.
     * This method should be implemented by subclasses to provide specific binding logic.
     *<p>By default, it sets the panel to be focusable and requests focus when the panel is shown. </p>
     */
    protected abstract void bindController();

}
