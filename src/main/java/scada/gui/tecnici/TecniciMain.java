package scada.gui.tecnici;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
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
    private String provincia;

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
                        TecniciMainAssignedRow rowController = TecniciMainAssignedRow.newInstance(item);
                        AnchorPane row = rowController.root;
                        row.prefWidthProperty().bind(this.prefWidthProperty());
                        rowController.setOnDoubleClick(() -> instance.openDetails());
                        setGraphic(row);
                    }
                }
            });
            //this is absolutely not the ideal way to do this, i know that
            instance.availableList.setCellFactory(listview -> new ListCell<>(){
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
                        TecniciMainAvailableRow rowController = TecniciMainAvailableRow.newInstance(item);
                        AnchorPane row = rowController.root;
                        row.prefWidthProperty().bind(this.prefWidthProperty());
                        rowController.setOnRedeem(code -> instance.redeem(code));
                        setGraphic(row);
                    }
                }
            });
            instance.getTecniciInfo();
            instance.refresh();
        });
    }

    public void openDetails(){
        var selected = assignedList.getSelectionModel().getSelectedItem();
        TecniciDetails details = TecniciDetails.newInstance(selected.code, selected.type, selected.getImpiantoRef(), selected.getMacchinarioRef());
        details.closingCallback = () -> refresh();
        details.getStage().show();
    }

    public void redeem(int int_code){
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setHeaderText("Confermare l'assegnazione volontaria di questo intervento?");
        confirm.showAndWait();
        if(confirm.getResult() != ButtonType.OK)
            return;

        try (var stmt = DAO.getDB().prepareStatement(SQLTecnici.ASSEGNAZIONE)) {
            stmt.setString(1, username);
            stmt.setInt(2, int_code);
            if(stmt.executeUpdate() > 0){
                DAO.getDB().commit();
            } else {
                DAO.getDB().rollback();
            }
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void refresh(){
        assignedList.getItems().clear();
        availableList.getItems().clear();
        //Assigned
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLTecnici.INTERVENTI)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int id_int = result.getInt(1);
                int type_int = result.getInt(2);
                String desc_int = result.getString(3);
                var data = getRowData(id_int, type_int, desc_int);
                if(data != null){
                    assignedList.getItems().add(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Available
        try (PreparedStatement statement = DAO.getDB().prepareStatement(SQLTecnici.DISPONIBILI)) {
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int id_int = result.getInt(1);
                int type_int = result.getInt(2);
                String desc_int = result.getString(3);
                var data = getRowData(id_int, type_int, desc_int);
                if(data != null){
                    availableList.getItems().add(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getTecniciInfo(){
        try (var stmt = DAO.getDB().prepareStatement(SQLTecnici.GET_PROVINCIA)) {
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            result.next();
            provincia = result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TecniciMainRowData getRowData(int id_int, int type_int, String desc_int){
        switch(type_int){
            case 1:
                //Controllo preventivo
                try (var macc_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_MACCHINARIO)) {
                    macc_stmt.setInt(1, id_int);
                    ResultSet macc = macc_stmt.executeQuery();
                    macc.next();
                    int inst_code = macc.getInt(1);
                    Macchinario macchinario = Macchinario.findFromInstCode(inst_code);
                    Impianto impianto = Impianto.findFromMacchinario(macchinario);
                    if(impianto.getProvincia().equals(provincia))
                        return new TecniciMainRowData(id_int, type_int, desc_int, impianto, macchinario);
                } catch (SQLException e){
                    e.printStackTrace();
                }
                break;
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
                    if(impianto.getProvincia().equals(provincia))
                        return new TecniciMainRowData(id_int, type_int, desc_int, impianto, macchinario);
                } catch (SQLException e){
                    e.printStackTrace();
                }
                break;
            case 3:
                //Dismissione impianto
                try (var imp_stmt = DAO.getDB().prepareStatement(SQLTecnici.INT_IMPIANTO)) {
                    imp_stmt.setInt(1, id_int);
                    ResultSet imp = imp_stmt.executeQuery();
                    imp.next();
                    Impianto impianto = Impianto.findFromCodiceProvincia(imp.getInt(1), imp.getString(2));
                    if(impianto.getProvincia().equals(provincia))
                        return new TecniciMainRowData(id_int, type_int, desc_int, impianto, null);
                } catch (SQLException e){
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
