package scada.gui.tecnici;

public class TecniciMainRowData {
    public int code;
    public String int_type;
    public int imp_id;
    public String imp_sigla;
    public String imp_via;

    public TecniciMainRowData(int code, String int_type, int imp_id, String imp_sigla, String imp_via) {
        this.code = code;
        this.int_type = int_type;
        this.imp_id = imp_id;
        this.imp_sigla = imp_sigla;
        this.imp_via = imp_via;
    }
}
