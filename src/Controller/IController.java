package Controller;

import Model.IModel;


/**
 * Represents a generic controller interface that defines the contract for communication
 * between the view and the model in an application. Implementations of this interface
 * are expected to handle user input, process it, and update the view and/or model accordingly.
 */
public interface IController {

    IModel getModel();
}
