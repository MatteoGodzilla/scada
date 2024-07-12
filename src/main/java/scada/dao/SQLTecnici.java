package scada.dao;

public class SQLTecnici {
    public static String INTERVENTI = """
        SELECT i.codice, i.tipo, t.descrizione FROM INTERVENTO i
        JOIN INT_TIPO t on (i.tipo = t.tipo)
        WHERE usernameTecnico = ?;
    """;

    public static String INT_MACCHINARIO = """
        SELECT im.codiceInstallazione FROM INT_MACCHINARIO im
        WHERE im.codiceIntervento = ?
    """;

    public static String INT_IMPIANTO = """
        SELECT ii.codiceImpianto, ii.siglaProvincia FROM INT_IMPIANTO ii
        WHERE ii.codiceIntervento = ?
    """;
}
