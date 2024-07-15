package scada.gui.tecnici;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import scada.gui.fxml.GuiConstructor;
import scada.gui.fxml.GuiController;

public class TecniciMainAssignedRow implements GuiController {
    public AnchorPane root;
    public Label code;
    public Label int_type;
    public Label imp_id;
    public Label address;

    public Runnable onDoubleClick;

    public static TecniciMainAssignedRow newInstance(TecniciMainRowData data){
        return GuiConstructor.createInstance("/tecnici/tecnici-main-assigned-row.fxml", (TecniciMainAssignedRow instance, AnchorPane row) -> {
            instance.root = row;
            instance.code.setText("" + data.code);
            instance.int_type.setText(data.int_type);
            instance.imp_id.setText("Impianto " + data.imp_id + "(" + data.imp_sigla + ")");
            instance.address.setText(data.imp_via);
        });
    }

    public void onClick(MouseEvent event){
        if(event.getClickCount() == 2)
            onDoubleClick.run();
    }

    public void setOnDoubleClick(Runnable r){
        onDoubleClick = r;
    }
}
