package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;
import scada.dao.DAO;
import scada.dao.Impianto;
import scada.dao.Macchinario;
import scada.dao.SQLResponsabili;

public class ResponsabiliMain extends StageController {
    private String username;
    private String regione;
    public TableView<Impianto> tabellaImpiantiGestione;
    public TableView<Macchinario> tabellaMacchinariGestione;
    public static ResponsabiliMain newInstance(String username) {
        return GuiConstructor.createInstance("/responsabili/ResponsabileDashboard.fxml", (ResponsabiliMain instance, Stage stage)-> {
            TableColumn<Integer, String> codiceImpianto = new TableColumn<>(); /*FIXME: UNDERSTAND HOW TO INSERT THE VALUES IN A GOOD WAY, THIS IS ONLY A SAMPLE */
            instance.stage = stage;
            instance.username = username;
            instance.tabellaImpiantiGestione.setRowFactory(listaImpianti -> {
                TableRow<Impianto> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {
                    Impianto clickedRow = row.getItem();
                    String selectedQuery = "";
                    switch(clickedRow.getTipologia()) {
                        case 1: /*FOTOVOLTAICO*/
                            selectedQuery = SQLResponsabili.LISTA_MACCHINARI_IMPIANTO_FOTOVOLTAICO;
                        break;
                        case 2: /*EOLICO*/
                            selectedQuery = SQLResponsabili.LISTA_MACCHINARI_IMPIANTO_EOLICO;
                        break;
                        case 3: /*BIOGAS*/
                            selectedQuery = SQLResponsabili.LISTA_MACCHINARI_IMPIANTO_BIOGAS;
                        break;
                    }
                    try(PreparedStatement stmnt = DAO.getDB().prepareStatement(selectedQuery)) {
                        stmnt.setInt(0, clickedRow.getCodice());
                        ResultSet response = stmnt.executeQuery();
                        while(response.next()) {
                            /*TODO:aggiunta macchinario in tabella */
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                });
                return row ;

            });
            instance.loadImpianti();
        });
    }

    private void loadImpianti() {
        tabellaImpiantiGestione.getItems().clear();
        try(PreparedStatement stmntRegione = DAO.getDB().prepareStatement(SQLResponsabili.GET_REGIONE_BY_USR)) {
            stmntRegione.setString(0, this.username);
            ResultSet response = stmntRegione.executeQuery();
            while(response.next()) {
                this.regione = response.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement stmntImpianti = DAO.getDB().prepareStatement(SQLResponsabili.IMPIANTI_REGIONALI)) {
            stmntImpianti.setString(0, this.regione);
            ResultSet result = stmntImpianti.executeQuery();
            while(result.next()){
                /*TODO: aggiunta impianto in tabella */
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openImpiantoCreate() {
        ImpiantoCreateController impiantoCreator = ImpiantoCreateController.newInstance(this.regione);
        impiantoCreator.closingCallback = () -> loadImpianti();
        impiantoCreator.getStage().show();
    }

    public void openMacchinarioCreate() {
        // TODO
        // var selected = tabellaImpiantiGestione.getSelectionModel().getSelectedItem();
        /* SWITCH SU TIPOLOGIA CON APERTURA CORRETTO FORM DI CREAZIONE */
        // creator.closingCallback = () -> loadImpianti();
        // creator.getStage().show();
    }

    /* FINESTRA ASSEGNAZIONE IMPIANTI AD ADDETTI*/
    /*TODO*/
    public void assignImpiantoToAddetto() {
        /* LITERALLY ONE QUERY AND ONE REFRESH OF THE TABLE */
        return;
    }

    /* FINESTRA GESTIONE INTERVENTI */
    /* TODO BISOGNA DECIDERE COME GESTIRE IL FILTRO */
    public void filterByTipologiaInterventi() {
        return;
    }
    /*TODO*/
    public void filterByTechnicianInterventi() {
        return;
    }
    /*TODO*/
    public void showOnlyCompletedInterventi() {
        return;
    }
    /*TODO*/
    public void openInterventiImpiantocreate() {
        return;
    }

    /*TODO*/
    public void openInterventiMacchinarioCreate() {
        return;
    }
}