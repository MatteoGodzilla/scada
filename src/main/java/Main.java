import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.gui.Login;
import scada.gui.addetti.Addetto;
import scada.gui.tecnici.TecniciMain;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            DAO.connectToDB();
            //DO NOT COMMIT THIS
            // Addetto addetto = Addetto.newInstance("carlo");
            // addetto.getStage().show();
            TecniciMain tecnici = TecniciMain.newInstance("luigi.verdi");
            tecnici.getStage().show();
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