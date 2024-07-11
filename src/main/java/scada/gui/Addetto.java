package scada.gui;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class Addetto extends StageController{

    private String username;
    public Button weatherButton;
    public Button reportButton;
    public Button refreshButton;
    public static Addetto schermataAddetto;

    //this static method cannot be moved in GuiController
    //because static methods can be generic, but not with the generic type defined by the class
    public static Addetto newInstance(String username){
        schermataAddetto = GuiConstructor.createInstance("/addetto.fxml",(Addetto instance, Stage stage)->{
            instance.stage = stage;
            instance.username = username;
            //instance.addettiInfo(username);
        });
        return schermataAddetto;
    }

    /**
     * Mostra gli impianti che sono affidati all'addetto identificato dall'username
     * @param username indica l'addetto che ha effettuato l'accesso
     */
    public void addettiInfo(String username) {

    }

    public void weather() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meteo.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        //TODO: La nuova finestra mostra il meteo dell'impianto selezionato
        //NON HO IDEA DI COME CAPIRE CHE IMPIANTO SELEZIONO (SEMPRE CHE SIA UNA COSA POSSIBILE DA FARE)

        /*
        Teoricamente dovrebbe bastare una cosa del genere

        Meteo m = Meteo.newInstance();
        m.getStage().show();

        Poi avendo accesso al controller puoi passargli i parametri che serve

        */
    }

    public void generateReport() {
        //TODO: Genera un file csv con le statistiche di un impianto
        FileChooser file = new FileChooser();
        file.setTitle("Save File");
        File filePath = file.showSaveDialog(schermataAddetto.getStage());
        System.out.println(filePath);
    }

    /**
     * Ricarica la informazioni su impianti e macchinari
     */
    public void refresh() {
        schermataAddetto.addettiInfo(username);
    }
}
