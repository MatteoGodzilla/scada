import javafx.application.Application;
import javafx.stage.Stage;
import scada.gui.Test;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        var inst = Test.newInstance();
        stage.setScene(inst.getScene());
        stage.show();
    }

}