package scada.gui.tecnici;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scada.dao.DAO;
import scada.dao.SQLTecnici;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class TecniciMain extends StageController {
    public ListView<TecniciMainRowData> assignedList;
    public ListView<TecniciMainRowData> availableList;
    private String username;

    public static TecniciMain newInstance(String username){
        return GuiConstructor.createInstance("/tecnici/tecnici-main.fxml", (TecniciMain instance, Stage stage)->{
            instance.stage = stage;
            instance.username = username;
            instance.assignedList.setCellFactory(listview -> new ListCell<>(){
                {
                    prefWidthProperty().bind(instance.assignedList.widthProperty().subtract(2));
                    setMaxWidth(USE_PREF_SIZE);
                    setPadding(Insets.EMPTY);
                }
                @Override
                protected void updateItem(TecniciMainRowData item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        AnchorPane row = TecniciMainRow.newInstance(item).root;
                        row.prefWidthProperty().bind(this.prefWidthProperty());
                        setGraphic(row);
                    }
                }
            });
            instance.InitialLoad();
        });
    }

    private void InitialLoad() {
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLTecnici.INTERVENTI_SCELTI)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                assignedList.getItems().add(new TecniciMainRowData(result.getInt(1), result.getString(2), result.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
