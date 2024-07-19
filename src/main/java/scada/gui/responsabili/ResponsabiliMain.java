package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
            instance.stage = stage;
            instance.username = username;
            /* INIZIALIZZAZIONE COLONNE TABELLA IMPIANTI */
            TableColumn<Impianto, String> colonnaCodiceImpianto = new TableColumn<>("Codice impianto");
            TableColumn<Impianto, String> colonnaProvincia = new TableColumn<>("Provincia");
            TableColumn<Impianto, String> colonnaIndirizzo = new TableColumn<>("Indirizzo");
            TableColumn<Impianto, String> colonnaAreaImpianto = new TableColumn<>("Dimensioni");
            TableColumn<Impianto, String> colonnaTipologiaImpianto = new TableColumn<>("Tipologia Impianto");
            /* BINDING CON GETTER CLASSE IMPIANTO */
            colonnaCodiceImpianto.setCellValueFactory(new PropertyValueFactory<>("codiceImpianto"));
            colonnaProvincia.setCellValueFactory(new PropertyValueFactory<>("siglaProvincia"));
            colonnaIndirizzo.setCellValueFactory(new PropertyValueFactory<>("indirizzo"));
            colonnaAreaImpianto.setCellValueFactory(new PropertyValueFactory<>("area"));
            colonnaTipologiaImpianto.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
            /* AGGIUNTA DELLE COLONNE ALLA TABLEVIEW IMPIANTI */
            instance.tabellaImpiantiGestione.getColumns().add(colonnaCodiceImpianto);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaProvincia);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaIndirizzo);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaAreaImpianto);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaTipologiaImpianto);
            /* INIZIALIZZAZIONE COLONNE TABELLA MACCHINARI */
            TableColumn<Macchinario, String> colonnaCodiceMacchinario = new TableColumn<>("Codice");
            TableColumn<Macchinario, String> colonnaDataInstallazioneMacchinario = new TableColumn<>("Data Installazione");
            TableColumn<Macchinario, String> colonnaAziendaMacchinario = new TableColumn<>("Azienda produttrice");
            TableColumn<Macchinario, String> colonnaModelloMacchinario = new TableColumn<>("Modello");
            /* BINDING CON GETTER CLASSE MACCHINARIO */
            colonnaCodiceMacchinario.setCellValueFactory(new PropertyValueFactory<>("codiceInstallazione"));
            colonnaDataInstallazioneMacchinario.setCellValueFactory(new PropertyValueFactory<>("dataInstallazione"));
            colonnaAziendaMacchinario.setCellValueFactory(new PropertyValueFactory<>("azienda"));
            colonnaModelloMacchinario.setCellValueFactory(new PropertyValueFactory<>("nomeModello"));
            /* AGGIUNTA DELLE COLONNE ALLA TABLEVIEW MACCHINARI */
            instance.tabellaMacchinariGestione.getColumns().add(colonnaCodiceMacchinario);
            instance.tabellaMacchinariGestione.getColumns().add(colonnaDataInstallazioneMacchinario);
            instance.tabellaMacchinariGestione.getColumns().add(colonnaAziendaMacchinario);
            instance.tabellaMacchinariGestione.getColumns().add(colonnaModelloMacchinario);
            /* RENDIAMO SELEZIONABILE SOLO UNA RIGA PER VOLTA */
            instance.tabellaImpiantiGestione.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            /* CONFIGURIAMO EVENTO ON SELECTION DELLA RIGA PER COMPILARE LA TABLEVIEW DEI MACCHINARI */
            instance.tabellaImpiantiGestione.setRowFactory(listaImpianti -> {
            /* PULISCI TABELLA MACCHINARI */
            instance.tabellaMacchinariGestione.getItems().clear();
            /* PROCEDURA DI RIEMPIMENTO TABELLA MACHINARI IMPIANTO SPECIFICO */
            TableRow<Impianto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                /* FIXME: NON CONVINTO CHE LA CONDIZIONE SIA CORRETTA */
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
                        stmnt.setString(1, clickedRow.getProvincia());
                        ResultSet response = stmnt.executeQuery();
                        while(response.next()) {
                            /*TODO:aggiunta macchinario in tabella */
                            Macchinario macchinario = new Macchinario(
                                response.getInt("codiceInstallazione"),
                                response.getString("dataInstallazione"), clickedRow.getTipologia(),
                                response.getString("azienda"),
                                response.getString("nomeModello")
                            );
                            instance.tabellaMacchinariGestione.getItems().add(macchinario);
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
                Impianto impianto = new Impianto(result.getInt("codImpianto"),
                 result.getString("siglaProvincia"),
                 result.getString("indirizzo"),
                 result.getFloat("area"),
                 false,
                 result.getInt("tipologia")
                );
                tabellaImpiantiGestione.getItems().add(impianto);
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