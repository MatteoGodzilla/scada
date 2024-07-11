package scada.dao;

public class SQLTecnici {
    //manca il controllo sulla provincia
    public static String INTERVENTI_SCELTI = """
        SELECT I.codice, I.usernameResponsabile, T.Descrizione FROM INTERVENTO I
        JOIN TIPOLOGIA T ON (T.codice = I.tipo)
        WHERE usernameTecnico = ? ;
    """;

    //manca il controllo sulla provincia
    public static String INTERVENTI_DISPONIBILI = """
        SELECT I.codice, I.usernameResponsabile, T.Descrizione FROM INTERVENTO I
        JOIN TIPOLOGIA T ON (T.codice = I.tipo)
        WHERE usernameTecnico = null;
    """;
}
