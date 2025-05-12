package Controller._abstract;

import Controller.IController;
import Model.IModel;
import View.AbsView;

public abstract class AbsController implements IController{
    protected IModel model;
    protected AbsView view;

    protected AbsController(IModel model, AbsView view){
        this.model = model;
        this.view = view;
    }

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public void notifyView(){
        view.repaint();
    }

    

}
