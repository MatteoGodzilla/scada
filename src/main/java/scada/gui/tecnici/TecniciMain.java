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
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLTecnici.INTERVENTI)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                TecniciMainRowData data = null;
                int id_int = result.getInt(1);
                int type_int = result.getInt(2);
                String desc_int = result.getString(3);
                switch(type_int){
                    case 1:{
                        try (var macc_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACCHINARIO)) {
                            //Controllo preventivo
                            macc_stmt.setInt(1, id_int);
                            ResultSet macc = macc_stmt.executeQuery();
                            macc.next();
                            int inst_code = macc.getInt(1);
                            int inst_type = macc.getInt(2);
                            PreparedStatement imp_stmt = null;
                            switch(inst_type){
                                default:
                                case 1:
                                    imp_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACC_FOTO);
                                    break;
                                case 2:
                                    imp_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACC_EOLICO);
                                    break;
                                case 3:
                                    imp_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACC_BIOGAS);
                                    break;
                            }
                            imp_stmt.setInt(1, inst_code);
                            ResultSet impianto = imp_stmt.executeQuery();
                            impianto.next();
                            data = new TecniciMainRowData(id_int, desc_int, impianto.getInt(1), impianto.getString(2), impianto.getString(3));
                        }
                        break;
                    }
                    case 2:
                        //Sostituzione parti
                        break;
                    case 3:
                        //Dismissione impianto
                        break;
                    default:
                        break;
                }
                if(data != null){
                    assignedList.getItems().add(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
