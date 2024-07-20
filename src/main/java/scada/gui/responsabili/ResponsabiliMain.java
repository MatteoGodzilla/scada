package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;
import scada.dao.AddettoRecord;
import scada.dao.DAO;
import scada.dao.Impianto;
import scada.dao.Macchinario;
import scada.dao.SQLResponsabili;

public class ResponsabiliMain extends StageController {
    private String username;
    private String regione;
    /* CAMPI PER GESTIONE IMPIANTI/MACCHINARI */
    public TableView<Impianto> tabellaImpiantiGestione;
    public TableView<Macchinario> tabellaMacchinariGestione;
    /* CAMPI PER GESTIONE ADDETTI/IMPIANTI */
    public TableView<AddettoRecord> tabellaAddettiAssegnazione;
    public TableView<Impianto> tabellaImpiantiAssegnazione;
    public TextField textUserAddetto;
    public ComboBox<String> comboProvinciaAssegnazione;
    public ComboBox<Integer> comboCodiceAssegnazione;
    public TextArea textInfoImpiantoAssegnazione;
    public Button buttonAssegnaImpianto;
    public static ResponsabiliMain newInstance(String username) {
        return GuiConstructor.createInstance("/responsabili/ResponsabileDashboard.fxml", (ResponsabiliMain instance, Stage stage)-> {
            instance.stage = stage;
            instance.username = username;
            /* QUERY PER IMPOSTARE ANCHE LA REGIONE DELL'UTENTE LOGGATO */
            try(PreparedStatement stmntRegione = DAO.getDB().prepareStatement(SQLResponsabili.GET_REGIONE_BY_USR)) {
                stmntRegione.setString(1, instance.username);
                ResultSet response = stmntRegione.executeQuery();
                while(response.next()) {
                    instance.regione = response.getString("regione");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /* TABPANE CONTENENTE LA GESTIONE MACCHINARI E IMPIANTI */
            /* INIZIALIZZAZIONE COLONNE TABELLA IMPIANTI */
            TableColumn<Impianto, String> colonnaCodiceImpianto = new TableColumn<>("Codice impianto");
            TableColumn<Impianto, String> colonnaProvinciaImpiantoGestione = new TableColumn<>("Provincia");
            TableColumn<Impianto, String> colonnaIndirizzoImpiantoGestione = new TableColumn<>("Indirizzo");
            TableColumn<Impianto, String> colonnaAreaImpiantoGestione = new TableColumn<>("Dimensioni");
            TableColumn<Impianto, String> colonnaTipologiaImpiantoGestione = new TableColumn<>("Tipologia Impianto");
            /* BINDING CON GETTER CLASSE IMPIANTO */
            colonnaCodiceImpianto.setCellValueFactory(new PropertyValueFactory<>("codice"));
            colonnaProvinciaImpiantoGestione.setCellValueFactory(new PropertyValueFactory<>("provincia"));
            colonnaIndirizzoImpiantoGestione.setCellValueFactory(new PropertyValueFactory<>("indirizzo"));
            colonnaAreaImpiantoGestione.setCellValueFactory(new PropertyValueFactory<>("area"));
            colonnaTipologiaImpiantoGestione.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
            /* AGGIUNTA DELLE COLONNE ALLA TABLEVIEW IMPIANTI */
            instance.tabellaImpiantiGestione.getColumns().add(colonnaCodiceImpianto);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaProvinciaImpiantoGestione);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaIndirizzoImpiantoGestione);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaAreaImpiantoGestione);
            instance.tabellaImpiantiGestione.getColumns().add(colonnaTipologiaImpiantoGestione);
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
            /* PROCEDURA DI RIEMPIMENTO TABELLA MACHINARI IMPIANTO SPECIFICO */
            TableRow<Impianto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {
                    instance.tabellaMacchinariGestione.getItems().clear();
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
                        stmnt.setInt(1, clickedRow.getCodice());
                        stmnt.setString(2, clickedRow.getProvincia());
                        ResultSet response = stmnt.executeQuery();
                        while(response.next()) {
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
            /* FINE TABPANE GESTIONE IMPIANTI E MACCHINARI */
            /* TABPANE GESTIONE ADDETTI E IMPIANTI ASSEGNATI */
            TableColumn<AddettoRecord, String> colonnaUsernameAddetto = new TableColumn<>("Username");
            TableColumn<AddettoRecord, String> colonnaNomeAddetto = new TableColumn<>("Nome");
            TableColumn<AddettoRecord, String> colonnaCognomeAddetto = new TableColumn<>("Cognome");
            colonnaUsernameAddetto.setCellValueFactory(new PropertyValueFactory<>("username"));
            colonnaNomeAddetto.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colonnaCognomeAddetto.setCellValueFactory(new PropertyValueFactory<>("cognome"));
            instance.tabellaAddettiAssegnazione.getColumns().add(colonnaUsernameAddetto);
            instance.tabellaAddettiAssegnazione.getColumns().add(colonnaNomeAddetto);
            instance.tabellaAddettiAssegnazione.getColumns().add(colonnaCognomeAddetto);

            TableColumn<Impianto, String> colonnaCodiceImpiantoAssegnazione = new TableColumn<>("Codice impianto");
            TableColumn<Impianto, String> colonnaProvinciaImpiantoAssegnazione = new TableColumn<>("Provincia");
            TableColumn<Impianto, String> colonnaIndirizzoImpiantoAssegnazione = new TableColumn<>("Indirizzo");
            TableColumn<Impianto, String> colonnaAreaImpiantoAssegnazione = new TableColumn<>("Dimensioni");
            TableColumn<Impianto, String> colonnaTipologiaImpiantoAssegnazione = new TableColumn<>("Tipologia Impianto");
            /* BINDING CON GETTER CLASSE IMPIANTO */
            colonnaCodiceImpiantoAssegnazione.setCellValueFactory(new PropertyValueFactory<>("codice"));
            colonnaProvinciaImpiantoAssegnazione.setCellValueFactory(new PropertyValueFactory<>("provincia"));
            colonnaIndirizzoImpiantoAssegnazione.setCellValueFactory(new PropertyValueFactory<>("indirizzo"));
            colonnaAreaImpiantoAssegnazione.setCellValueFactory(new PropertyValueFactory<>("area"));
            colonnaTipologiaImpiantoAssegnazione.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
            /* AGGIUNTA DELLE COLONNE ALLA TABLEVIEW IMPIANTI ASSEGNAZIONE */
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaCodiceImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaProvinciaImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaIndirizzoImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaAreaImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaTipologiaImpiantoAssegnazione);
            instance.tabellaAddettiAssegnazione.setRowFactory(listaImpianti -> {
                /* PULISCI TABELLA IMPIANTI */
                instance.tabellaImpiantiAssegnazione.getItems().clear();
                /* PROCEDURA DI RIEMPIMENTO TABELLA IMPIANTI ASSEGNATI AD ADDETTO */
                TableRow<AddettoRecord> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                        AddettoRecord clickedRow = row.getItem();
                        instance.tabellaImpiantiAssegnazione.getItems().clear();
                        instance.textUserAddetto.setText(clickedRow.getUsername());
                        try(PreparedStatement stmnt = DAO.getDB().prepareStatement(SQLResponsabili.IMPIANTI_ASSEGNATI_A)) {
                            stmnt.setString(1, clickedRow.getUsername());
                            ResultSet response = stmnt.executeQuery();
                            while(response.next()) {
                                Impianto impianto = new Impianto(response.getInt("codiceImpianto"),
                                    response.getString("siglaProvincia"),
                                    response.getString("indirizzo"),
                                    response.getFloat("area"),
                         false,
                                    response.getInt("tipologia")
                                );
                                instance.tabellaImpiantiAssegnazione.getItems().add(impianto);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    instance.loadComboBoxProvinceAssegnazione();
                    });
                    return row;
                });
                instance.loadAddetti();
        });
    }

    private void loadImpianti() {
        tabellaImpiantiGestione.getItems().clear();
        try (PreparedStatement stmntImpianti = DAO.getDB().prepareStatement(SQLResponsabili.IMPIANTI_REGIONALI)) {
            stmntImpianti.setString(1, this.regione);
            ResultSet result = stmntImpianti.executeQuery();
            while(result.next()){
                Impianto impianto = new Impianto(result.getInt("codiceImpianto"),
                 result.getString("siglaProvincia"),
                 result.getString("indirizzo"),
                 result.getFloat("area"),
                 false,
                 result.getInt("tipologia")
                );
                /* COMPILAZIONE TABELLA GESTIONE IMPIANTI */
                tabellaImpiantiGestione.getItems().add(impianto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAddetti() {
        this.tabellaAddettiAssegnazione.getItems().clear();
        this.tabellaImpiantiAssegnazione.getItems().clear();
        try (PreparedStatement stmntImpianti = DAO.getDB().prepareStatement(SQLResponsabili.ADDETTI_PER_REGIONE)) {
            stmntImpianti.setString(1, this.regione);
            ResultSet result = stmntImpianti.executeQuery();
            while(result.next()){
                AddettoRecord addetto = new AddettoRecord(result.getString("username"),
                result.getString("nome"),
                result.getString("cognome")
                );
                this.tabellaAddettiAssegnazione.getItems().add(addetto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadComboBoxProvinceAssegnazione() {
        this.comboProvinciaAssegnazione.setDisable(false);
        this.comboProvinciaAssegnazione.getItems().clear();
        if(!textUserAddetto.getText().isEmpty()) {
            try (PreparedStatement stmntImpianti = DAO.getDB().prepareStatement(SQLResponsabili.GET_PROVINCE_FROM_REGIONE)) {
                stmntImpianti.setString(1, this.regione);
                ResultSet result = stmntImpianti.executeQuery();
                while(result.next()){
                    /* COMPILAZIONE COMBOBOX ASSEGNAZIONE PROVINCE */
                    this.comboProvinciaAssegnazione.getItems().add(result.getString("sigla"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadComboBoxCodiciAssegnazione() {
        comboCodiceAssegnazione.getItems().clear();
        if(!comboProvinciaAssegnazione.getSelectionModel().getSelectedItem().isEmpty()) {
            try (PreparedStatement stmntImpianti = DAO.getDB().prepareStatement(SQLResponsabili.GET_CODICI_IMPIANTI_FROM_PROVINCIA)) {
                stmntImpianti.setString(1, comboProvinciaAssegnazione.getSelectionModel().getSelectedItem());
                ResultSet result = stmntImpianti.executeQuery();
                while(result.next()){
                    /* COMPILAZIONE COMBOBOX ASSEGNAZIONE CODICI */
                    comboCodiceAssegnazione.getItems().add(result.getInt("codiceImpianto"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openImpiantoCreate() {
        ImpiantoCreateController impiantoCreator = ImpiantoCreateController.newInstance(this.regione);
        impiantoCreator.closingCallback = () -> loadImpianti();
        impiantoCreator.getStage().show();
    }

    public void openMacchinarioCreate() {
        var selected = tabellaImpiantiGestione.getSelectionModel().getSelectedItem();
        if(selected == null)
            return;
        switch(selected.getTipologia()) {
            case 1: /* FOTOVOLTAICO */
                FotovoltaicoCreateController fotovoltaicoCreator = FotovoltaicoCreateController.newInstance(selected.getCodice(), selected.getProvincia());
                fotovoltaicoCreator.getStage().show();
                break;
            case 2: /* EOLICO */
                EolicoCreateController eolicoCreator = EolicoCreateController.newInstance(selected.getCodice(), selected.getProvincia());
                eolicoCreator.getStage().show();
                break;
            case 3:
                BiogasCreateController biogasCreator = BiogasCreateController.newInstance(selected.getCodice(), selected.getProvincia());
                biogasCreator.getStage().show();
                break;
        }
    }

    /* FINESTRA ASSEGNAZIONE IMPIANTI AD ADDETTI*/
    /*TODO*/
    public void assignImpiantoToAddetto() {
        try (PreparedStatement stmntAssegnazione = DAO.getDB().prepareStatement(SQLResponsabili.ASSEGNA_IMPIANTO_A)) {
            stmntAssegnazione.setString(1, this.textUserAddetto.getText());
            stmntAssegnazione.setInt(2, this.comboCodiceAssegnazione.getSelectionModel().getSelectedItem());
            stmntAssegnazione.setString(3, this.comboProvinciaAssegnazione.getSelectionModel().getSelectedItem());
            Integer result = stmntAssegnazione.executeUpdate();
            if(result != 0) {
                DAO.getDB().commit();
            }
            else {
                DAO.getDB().rollback();
                Alert errorMessage = new Alert(AlertType.ERROR);
                errorMessage.setContentText("Assegnazione dell'impianto fallita.");
            }
            /* refresh tabella addetti e pulizia impianti addetto*/
            this.loadAddetti();
            /* pulizia combobox */
            this.comboProvinciaAssegnazione.getItems().clear();
            this.comboCodiceAssegnazione.getItems().clear();
            this.textInfoImpiantoAssegnazione.clear();
            /* bloccaggio bottone e combobox */
            this.comboProvinciaAssegnazione.setDisable(true);
            this.comboCodiceAssegnazione.setDisable(true);
            this.buttonAssegnaImpianto.setDisable(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unlockComboCodiceImpianto() {
        String provincia = comboProvinciaAssegnazione.getSelectionModel().getSelectedItem();
        if(provincia != null) {
            comboCodiceAssegnazione.setDisable(false);
            loadComboBoxCodiciAssegnazione();
        }
        else {
            comboCodiceAssegnazione.setDisable(true);
            comboCodiceAssegnazione.getItems().clear();
        }
        return;
    }

    public void compileInfoImpianto() {
        textInfoImpiantoAssegnazione.clear();
        Integer codice = this.comboCodiceAssegnazione.getSelectionModel().getSelectedItem();
        String provincia = this.comboProvinciaAssegnazione.getSelectionModel().getSelectedItem();
        if(codice != null && provincia != null) {
            try (PreparedStatement stmntInfoImpianto = DAO.getDB().prepareStatement(SQLResponsabili.GET_INFO_IMPIANTO_FROM_PROVINCIA_CODICE)) {
                stmntInfoImpianto.setString(1, provincia);
                stmntInfoImpianto.setInt(2, codice);
                ResultSet result = stmntInfoImpianto.executeQuery();
                String infoString = "";
                while(result.next()){
                   infoString += "Indirizzo di locazione: " + result.getString("indirizzo") + "\n";
                   infoString += "Area occupata: " + Float.toString(result.getFloat("area")) + "\n";
                   /* FIXME MAKE JOIN TO SHOW THE TYPE DESCRIPTION */
                   infoString += "Tipologia dell'impianto: " + Integer.toString(result.getInt("tipologia")) + "\n";
                   textInfoImpiantoAssegnazione.setText(infoString);
                }
                this.buttonAssegnaImpianto.setDisable(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    /**
     * Apre la finestra di creazione di un intervento per un impianto
     */
    public void openInterventiImpiantocreate() {
        InterventoImpiantoCreateController intImpianto = InterventoImpiantoCreateController.newInstance(regione, username);
        intImpianto.getStage().show();
    }

    /**
     * Apre la finestra di creazione di un intervento per un macchinario
     */
    public void openInterventiMacchinarioCreate() {
        InterventoMacchinarioCreateController intMacc = InterventoMacchinarioCreateController.newInstance();
        intMacc.getStage().show();
    }
}