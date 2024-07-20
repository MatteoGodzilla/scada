import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.gui.Login;
import scada.gui.responsabili.ResponsabiliMain;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            DAO.connectToDB();
            // Login login = Login.newInstance();
            // login.getStage().show();
            ResponsabiliMain responsabiliMain = ResponsabiliMain.newInstance("mario.rossi");
            responsabiliMain.getStage().show();
        } catch (SQLException e) {
            System.err.println("Could not connect to the database");
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find connector for mysql");
            e.printStackTrace();
        }
    }
}