package scada.gui.responsabili;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
            instance.loadImpiantiGestione();
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

            try (PreparedStatement stmntImpianti = DAO.getDB().prepareStatement(SQLResponsabili.ADDETTI_PER_REGIONE)) {
                stmntImpianti.setString(1, instance.regione);
                ResultSet result = stmntImpianti.executeQuery();
                while(result.next()){
                    AddettoRecord addetto = new AddettoRecord(result.getString("username"),
                    result.getString("nome"),
                    result.getString("cognome") 
                    );
                    instance.tabellaAddettiAssegnazione.getItems().add(addetto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            instance.tabellaAddettiAssegnazione.setRowFactory(listaImpianti -> {
                /* PULISCI TABELLA IMPIANTI */
                instance.tabellaImpiantiAssegnazione.getItems().clear();
                /* PROCEDURA DI RIEMPIMENTO TABELLA IMPIANTI ASSEGNATI AD ADDETTO */
                TableRow<AddettoRecord> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {
                        AddettoRecord clickedRow = row.getItem();
                        instance.textUserAddetto.setText(clickedRow.getUsername());
                        try(PreparedStatement stmnt = DAO.getDB().prepareStatement(SQLResponsabili.GET_PROVINCIE_FROM_REGIONE))
                        {
                            stmnt.setString(1, instance.regione);
                            ResultSet response = stmnt.executeQuery();
                            while(response.next()) {
                                /* FIXME TROVARE COME COMPILARE COMBOBOX */
                                //sigle.add(response.getString("sigla"));
                            }
                        }catch(SQLException e) {
                            e.printStackTrace();
                        }
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
                    });
                    return row;
                });
        });
    }

    private void loadImpiantiGestione() {
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
                System.out.println(impianto.getCodice());
                tabellaImpiantiGestione.getItems().add(impianto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openImpiantoCreate() {
        ImpiantoCreateController impiantoCreator = ImpiantoCreateController.newInstance(this.regione);
        impiantoCreator.closingCallback = () -> loadImpiantiGestione();
        impiantoCreator.getStage().show();
    }

    public void openMacchinarioCreate() {
        /* TODO: NECESSARIO CLOSING CALLBACK?? */
        var selected = tabellaImpiantiGestione.getSelectionModel().getSelectedItem();
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
        /* LITERALLY ONE QUERY AND ONE REFRESH OF THE TABLE, NEED THE USERNAME OF THE CLICKED ROW */
        return;
    }

    public void filterByProvinciaAssegnazione() {
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