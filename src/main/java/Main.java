import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
			Parent p = FXMLLoader.load(getClass().getResource("test.fxml"));
            Scene s = new Scene(p);
            stage.setScene(s);
            stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch();
    }

}