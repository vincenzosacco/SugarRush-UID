package Controller;

import Model.IModel;

import java.util.EventListener;


/**
 * Represents a generic controller interface that defines the contract for communication
 * between the view and the model in an application. Implementations of this interface
 * are expected to handle user input, process it, and update the view and/or model accordingly.
 */
public interface IController extends EventListener {
    /**
     * @return the model associated with this controller.
     */
    IModel getModel();

    /**
     * Notifies the view that the model has changed.
     */
    void notifyView();

}
