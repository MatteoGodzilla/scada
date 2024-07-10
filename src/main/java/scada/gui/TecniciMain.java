package scada.gui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLTecnici;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class TecniciMain extends StageController {
    private String username;

    public static TecniciMain newInstance(String username){
        return GuiConstructor.createInstance("/tecnici/tecnici-main.fxml", (TecniciMain instance, Stage stage)->{
            instance.stage = stage;
            instance.username = username;
            instance.InitialLoad();
        });
    }

    private void InitialLoad() {
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLTecnici.INTERVENTI_SCELTI)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                System.out.println(result.getInt(1));
                System.out.println(result.getString(2));
                System.out.println(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
