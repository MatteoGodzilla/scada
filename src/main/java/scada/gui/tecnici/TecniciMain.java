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
import scada.dao.Impianto;
import scada.dao.Macchinario;
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
                        TecniciMainRow rowController = TecniciMainRow.newInstance(item);
                        AnchorPane row = rowController.root;
                        row.prefWidthProperty().bind(this.prefWidthProperty());
                        rowController.setOnDoubleClick(() -> instance.openDetails());
                        setGraphic(row);
                    }
                }
            });
            instance.InitialLoad();
        });
    }

    public void openDetails(){
        var selected = assignedList.getSelectionModel().getSelectedItem();
        TecniciDetails details = TecniciDetails.newInstance(selected.getImpiantoRef(), selected.getMacchinarioRef());
        details.getStage().show();
    }


    public void refresh(){
        System.out.println("REFRESH");
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
                        //Controllo preventivo
                        try (var macc_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACCHINARIO)) {
                            macc_stmt.setInt(1, id_int);
                            ResultSet macc = macc_stmt.executeQuery();
                            macc.next();
                            int inst_code = macc.getInt(1);
                            Macchinario macchinario = Macchinario.findFromInstCode(inst_code);
                            Impianto impianto = Impianto.findFromMacchinario(macchinario);

                            data = new TecniciMainRowData(id_int, desc_int, impianto, macchinario);
                        }
                        break;
                    }
                    case 2:
                        //Sostituzione parti
                        try (var macc_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACCHINARIO)) {
                            //Controllo preventivo
                            macc_stmt.setInt(1, id_int);
                            ResultSet macc = macc_stmt.executeQuery();
                            macc.next();
                            int inst_code = macc.getInt(1);
                            Macchinario macchinario = Macchinario.findFromInstCode(inst_code);
                            Impianto impianto = Impianto.findFromMacchinario(macchinario);

                            data = new TecniciMainRowData(id_int, desc_int, impianto, macchinario);
                        }
                        break;
                    case 3:
                        //Dismissione impianto
                        try (var imp_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_IMPIANTO)) {
                            imp_stmt.setInt(1, id_int);
                            ResultSet imp = imp_stmt.executeQuery();
                            imp.next();
                            Impianto impianto = Impianto.findFromCodiceProvincia(imp.getInt(1), imp.getString(2));
                            data = new TecniciMainRowData(id_int, desc_int, impianto, null);
                        }
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
