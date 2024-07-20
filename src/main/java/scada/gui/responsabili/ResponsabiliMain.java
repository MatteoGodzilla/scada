package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import scada.dao.InterventiRecord;
import scada.dao.Macchinario;
import scada.dao.SQLResponsabili;
import scada.dao.Tipologia;

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
    /* CAMPI GESTIONE INTERVENTI */
    public TableView<InterventiRecord> tabellaInterventi;
    public TextArea textNoteInterventi;
    public ComboBox<String> comboTecniciIncaricati;
    public CheckBox checkInterventiCompletati;

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
            // colonnaTipologiaImpiantoGestione.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
            colonnaTipologiaImpiantoGestione.setCellValueFactory(param ->
                new SimpleStringProperty(Tipologia.fromCode(param.getValue().getTipologia()))
            );

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
                    instance.loadMacchinariDaImpianto(row.getItem());
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
            // colonnaTipologiaImpiantoAssegnazione.setCellValueFactory(new PropertyValueFactory<>("tipologia"));
            colonnaTipologiaImpiantoAssegnazione.setCellValueFactory(param ->
                new SimpleStringProperty(Tipologia.fromCode(param.getValue().getTipologia()))
            );

            /* AGGIUNTA DELLE COLONNE ALLA TABLEVIEW IMPIANTI ASSEGNAZIONE */
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaCodiceImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaProvinciaImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaIndirizzoImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaAreaImpiantoAssegnazione);
            instance.tabellaImpiantiAssegnazione.getColumns().add(colonnaTipologiaImpiantoAssegnazione);
            instance.tabellaAddettiAssegnazione.setRowFactory(listaImpianti -> {
            /* PULISCI TABELLA IMPIANTI ASSEGNAZIONE */
            instance.tabellaImpiantiAssegnazione.getItems().clear();
            /* PROCEDURA DI RIEMPIMENTO TABELLA IMPIANTI ASSEGNATI AD ADDETTO */
            TableRow<AddettoRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    instance.loadImpiantiDaAddetto(row.getItem());
                }
                });
                return row;
            });
            instance.loadAddetti();
            instance.loadComboTecnici();
            /* FINE TABPANE GESTIONE ADDETTI E IMPIANTI ASSEGNATI */
            /* INIZIO TABPANE GESTIONE INTERVENTI */
            TableColumn<InterventiRecord, String> colonnaCodiceIntervento = new TableColumn<>("Codice intervento");
            TableColumn<InterventiRecord, String> colonnaStatusIntervento = new TableColumn<>("Completato");
            TableColumn<InterventiRecord, String> colonnaUsernameTecnicoIntervento = new TableColumn<>("Tecnico incaricato");
            TableColumn<InterventiRecord, String> colonnaDescrizioneIntervento = new TableColumn<>("Descrizione intervento");
            TableColumn<InterventiRecord, String> colonnaNoteIntervento = new TableColumn<>("Note");
            colonnaCodiceIntervento.setCellValueFactory(new PropertyValueFactory<>("codiceIntervento"));
            colonnaDescrizioneIntervento.setCellValueFactory(new PropertyValueFactory<>("descrizioneIntervento"));
            colonnaStatusIntervento.setCellValueFactory(new PropertyValueFactory<>("completato"));
            colonnaUsernameTecnicoIntervento.setCellValueFactory(new PropertyValueFactory<>("usernameTecnico"));
            colonnaNoteIntervento.setCellValueFactory(new PropertyValueFactory<>("noteIntervento"));
            instance.tabellaInterventi.getColumns().add(colonnaCodiceIntervento);
            instance.tabellaInterventi.getColumns().add(colonnaStatusIntervento);
            instance.tabellaInterventi.getColumns().add(colonnaUsernameTecnicoIntervento);
            instance.tabellaInterventi.getColumns().add(colonnaDescrizioneIntervento);
            instance.tabellaInterventi.getColumns().add(colonnaNoteIntervento);
            instance.tabellaInterventi.setRowFactory(listaInterventi -> {
            TableRow<InterventiRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {
                    instance.textNoteInterventi.clear();
                    instance.textNoteInterventi.setText(row.getItem().getNoteIntervento());
                }
                });
                return row;
            });
            instance.loadInterventi();
        });
    }



    /* METODI CONTROLLER E UTILITY FINESTRA GESTIONE IMPIANTI E MACCHINARI */
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

    private void loadMacchinariDaImpianto(Impianto clickedRow) {
        this.tabellaMacchinariGestione.getItems().clear();
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
                this.tabellaMacchinariGestione.getItems().add(macchinario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openImpiantoCreate() {
        ImpiantoCreateController impiantoCreator = ImpiantoCreateController.newInstance(this.regione);
        impiantoCreator.onCloseRunnable = () -> loadImpianti();
        impiantoCreator.getStage().show();
    }

    public void openMacchinarioCreate() {
        var selected = tabellaImpiantiGestione.getSelectionModel().getSelectedItem();
        if(selected == null)
            return;
        switch(selected.getTipologia()) {
            case 1: /* FOTOVOLTAICO */
                FotovoltaicoCreateController fotovoltaicoCreator = FotovoltaicoCreateController.newInstance(selected.getCodice(), selected.getProvincia());
                fotovoltaicoCreator.onCloseRunnable = () -> loadMacchinariDaImpianto(selected);
                fotovoltaicoCreator.getStage().show();
                break;
            case 2: /* EOLICO */
                EolicoCreateController eolicoCreator = EolicoCreateController.newInstance(selected.getCodice(), selected.getProvincia());
                eolicoCreator.onCloseRunnable = () -> loadMacchinariDaImpianto(selected);
                eolicoCreator.getStage().show();
                break;
            case 3: /* BIOGAS */
                BiogasCreateController biogasCreator = BiogasCreateController.newInstance(selected.getCodice(), selected.getProvincia());
                biogasCreator.onCloseRunnable = () -> loadMacchinariDaImpianto(selected);
                biogasCreator.getStage().show();
                break;
        }
    }

    /* METODI CONTROLLER E UTILITY FINESTRA ASSEGNAZIONE IMPIANTI AD ADDETTI*/
    private void loadAddetti() {
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

    /* FIXME: QUERY RITORNA CODICI DI IMPIANTI GIA' ASSEGNATI */
    private void loadImpiantiDaAddetto(AddettoRecord clickedRow) {
        this.tabellaImpiantiAssegnazione.getItems().clear();
        this.textUserAddetto.setText(clickedRow.getUsername());
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
                this.tabellaImpiantiAssegnazione.getItems().add(impianto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.loadComboBoxProvinceAssegnazione();
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
            try (PreparedStatement stmntCodiciDaAssegnare = DAO.getDB().prepareStatement(SQLResponsabili.IMPIANTI_NON_ASSEGNATI_A)) {
                stmntCodiciDaAssegnare.setString(1, this.textUserAddetto.getText());
                stmntCodiciDaAssegnare.setString(2, comboProvinciaAssegnazione.getSelectionModel().getSelectedItem());
                ResultSet result = stmntCodiciDaAssegnare.executeQuery();
                while(result.next()){
                    /* COMPILAZIONE COMBOBOX ASSEGNAZIONE CODICI */
                    comboCodiceAssegnazione.getItems().add(result.getInt("codiceImpianto"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

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
            /* Refresh tabella addetti e pulizia impianti addetto*/
            this.loadAddetti();
            /* Pulizia combobox */
            this.comboProvinciaAssegnazione.getItems().clear();
            this.comboCodiceAssegnazione.getItems().clear();
            this.textInfoImpiantoAssegnazione.clear();
            /* Disattivazione bottone e combobox */
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
                   infoString += "Tipologia dell'impianto: " + Tipologia.fromCode(result.getInt("tipologia")) + "\n";
                   textInfoImpiantoAssegnazione.setText(infoString);
                }
                this.buttonAssegnaImpianto.setDisable(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* METODI CONTROLLER E UTILITY FINESTRA GESTIONE INTERVENTI */
    /* TODO BISOGNA DECIDERE COME GESTIRE IL FILTRO */
    private void loadInterventiGeneric(String query) {
        this.tabellaInterventi.getItems().clear();
        try (PreparedStatement stmntInterventi = DAO.getDB().prepareStatement(query)) {
            stmntInterventi.setString(1, this.username);
            ResultSet result = stmntInterventi.executeQuery();
            while(result.next()){
                InterventiRecord intervento = new InterventiRecord(result.getInt("codice"),
                    result.getBoolean("completato"),
                    result.getString("usernameTecnico"),
                    result.getString("descrizione"),
                    result.getString("note")
                );
                this.tabellaInterventi.getItems().add(intervento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadComboTecnici() {
        this.comboTecniciIncaricati.getItems().clear();
        try (PreparedStatement stmntTecnici = DAO.getDB().prepareStatement(SQLResponsabili.LISTA_TECNICI_REGIONALI)) {
            stmntTecnici.setString(1, this.regione);
            ResultSet result = stmntTecnici.executeQuery();
            while(result.next()){
                this.comboTecniciIncaricati.getItems().add(result.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadInterventi() {
        this.loadInterventiGeneric(SQLResponsabili.INTERVENTI);
    }

    public void filterInterventiTable() {
        if (this.comboTecniciIncaricati.getSelectionModel().getSelectedItem() != null && this.checkInterventiCompletati.isSelected()) {
            this.loadInterventiCompletatiPerTecnico();
        } else if (this.comboTecniciIncaricati.getSelectionModel().getSelectedItem() != null && !this.checkInterventiCompletati.isSelected()) {
            this.loadInterventiPerTecnico();
        } else if (this.comboTecniciIncaricati.getSelectionModel().getSelectedItem() == null && this.checkInterventiCompletati.isSelected()){
            this.loadInterventiCompletati();
        } else {
            this.loadInterventi();
        }
    }
    private void loadInterventiCompletati() {
        this.loadInterventiGeneric(SQLResponsabili.INTERVENTI_COMPLETATI);
    }

    private void loadInterventiPerTecnico() {
        this.loadInterventiGenericWithTwoParameters(SQLResponsabili.INTERVENTI_PER_TECNICO);
    }

    private void loadInterventiGenericWithTwoParameters(String query) {
        this.tabellaInterventi.getItems().clear();
        try (PreparedStatement stmntInterventi = DAO.getDB().prepareStatement(query)) {
            stmntInterventi.setString(1, this.username);
            stmntInterventi.setString(2, this.comboTecniciIncaricati.getSelectionModel().getSelectedItem());
            ResultSet result = stmntInterventi.executeQuery();
            while(result.next()){
                InterventiRecord intervento = new InterventiRecord(result.getInt("codice"),
                    result.getBoolean("completato"),
                    result.getString("usernameTecnico"),
                    result.getString("descrizione"),
                    result.getString("note")
                );
                this.tabellaInterventi.getItems().add(intervento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadInterventiCompletatiPerTecnico() {
        this.loadInterventiGenericWithTwoParameters(SQLResponsabili.INTERVENTI_COMPLETATI_PER_TECNICO);
    }

    public void refreshTableInterventi() {
        this.loadInterventi();
        this.checkInterventiCompletati.setSelected(false);
        this.loadComboTecnici();
    }
    /**
     * Apre la finestra di creazione di un intervento per un impianto
     */
    public void openInterventiImpiantocreate() {
        InterventoImpiantoCreateController intImpianto = InterventoImpiantoCreateController.newInstance(regione, username);
        intImpianto.onCloseRunnable = () -> this.refreshTableInterventi();
        intImpianto.getStage().show();
    }

    /**
     * Apre la finestra di creazione di un intervento per un macchinario
     */
    public void openInterventiMacchinarioCreate() {
        InterventoMacchinarioCreateController interventoMacchinarioCreator = InterventoMacchinarioCreateController.newInstance(regione, username);
        interventoMacchinarioCreator.onCloseRunnable = () -> this.refreshTableInterventi();
        interventoMacchinarioCreator.getStage().show();
    }
}