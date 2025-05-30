import controller.Controller;
import view.View;

public class Main {
    public static void main(String[] args) {
        Controller.bind();
        //View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName()); --> TO BE IMPLEMENTED
        View.getInstance().showPanel(View.PanelName.START_MENU.getName());
        View.getInstance().launchView();
    }
}