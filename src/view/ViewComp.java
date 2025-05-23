package view;

import controller.IControllerObj;

public interface ViewComp {
    /**
     * Registers a controller to the view component,
     * should be called to bind the controller to the view component.
     * @implNote this method should contain the logic to bind the controller to the view component specific to the ViewComponent
     * @param controller the controller to be bound to the view component
     */
    void bindController(IControllerObj controller);
}
