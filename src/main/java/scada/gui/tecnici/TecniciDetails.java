package scada.gui.tecnici;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import scada.dao.Impianto;
import scada.dao.Macchinario;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.StageController;

public class TecniciDetails extends StageController{

    public TextArea detailsImpianto;
    public TextArea detailsMacchinario;

    public static TecniciDetails newInstance(Impianto impianto, Macchinario macchinario){
        return GuiConstructor.createInstance("/tecnici/tecnici-details.fxml",(TecniciDetails instance, Stage stage) ->{
            instance.stage = stage;
            instance.loadDetailsImpianto(impianto);
            instance.loadDetailsMacchinario(macchinario);
        });
    }

    private void loadDetailsImpianto(Impianto impianto) {
        if(impianto == null){
            detailsImpianto.setText("Nessuna informazione aggiuntiva");
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("Codice Impianto:");
            builder.append(impianto.getCodice());
            builder.append('\n');
            builder.append("Provincia:");
            builder.append(impianto.getProvincia());
            builder.append('\n');
            builder.append("Indirizzo:");
            builder.append(impianto.getIndirizzo());
            builder.append('\n');
            builder.append("Area:");
            builder.append(impianto.getArea());
            builder.append('\n');
            builder.append("Uomo In Sito:");
            builder.append(impianto.isUomoInSito() ? "Presente" : "Assente");
            builder.append('\n');
            builder.append("Tipologia:");
            builder.append(impianto.getTipologia());
            builder.append('\n');
            detailsImpianto.setText(builder.toString());
        }
    }

    private void loadDetailsMacchinario(Macchinario macchinario) {
        if(macchinario == null){
            detailsMacchinario.setText("Nessuna informazione aggiuntiva");
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("Codice Installazione:");
            builder.append(macchinario.getCodiceInstallazione());
            builder.append('\n');
            builder.append("Data Installazione:");
            builder.append(macchinario.getDataInstallazione());
            builder.append('\n');
            builder.append("Tipologia:");
            builder.append(macchinario.getTipologia());
            builder.append('\n');
            builder.append("Azienda:");
            builder.append(macchinario.getAzienda());
            builder.append('\n');
            builder.append("Nome Modello:");
            builder.append(macchinario.getNomeModello());
            builder.append('\n');
            builder.append("Garanzia:");
            builder.append(macchinario.getDurataGaranzia());
            builder.append('\n');
            builder.append("Status:");
            builder.append(macchinario.getStatus());
            builder.append('\n');
            detailsMacchinario.setText(builder.toString());
        }
    }
}
