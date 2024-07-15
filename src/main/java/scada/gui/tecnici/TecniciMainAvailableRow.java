package scada.gui.tecnici;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;
import java.util.function.*;

public class TecniciMainAvailableRow implements GuiController {
    public AnchorPane root;
    public Label codeLabel;
    public Label int_type;
    public Label imp_id;
    public Label address;

    public int code;
    public Consumer<Integer> onDoubleClick;

    public static TecniciMainAvailableRow newInstance(TecniciMainRowData data){
        return GuiConstructor.createInstance("/tecnici/tecnici-main-available-row.fxml", (TecniciMainAvailableRow instance, AnchorPane row) -> {
            instance.root = row;
            instance.code = data.code;
            instance.codeLabel.setText("" + data.code);
            instance.int_type.setText(data.int_type);
            instance.imp_id.setText("Impianto " + data.imp_id + "(" + data.imp_sigla + ")");
            instance.address.setText(data.imp_via);
        });
    }

    public void onClick(MouseEvent event){
        if(event.getClickCount() == 2)
            onRedeem();
    }

    public void setOnRedeem(Consumer<Integer> r){
        onDoubleClick = r;
    }

    public void onRedeem(){
        onDoubleClick.accept(code);
    }
}
