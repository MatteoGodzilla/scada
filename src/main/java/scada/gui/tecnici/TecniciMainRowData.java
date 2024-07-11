package scada.gui.tecnici;

public class TecniciMainRowData {
    public String userResponsabile;
    public int code;
    public int type;

    public TecniciMainRowData(int code, String usernameResponsabile, int type){
        this.code = code;
        this.userResponsabile = usernameResponsabile;
        this.type = type;
    }
}
