import controller.Controller;
import view.View;

public class Main {
    public static void main(String[] args) {
        Controller.bind();
        View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
        View.getInstance().launchView();
    }
}