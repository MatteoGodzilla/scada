import javafx.application.Application;
import javafx.stage.Stage;
import scada.gui.Login;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Login l = Login.newInstance();
        l.getStage().show();
    }

}