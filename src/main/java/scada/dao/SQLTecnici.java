package scada.dao;

public class SQLTecnici {
    public static String INTERVENTI = """
        SELECT i.codice, i.tipo, t.descrizione FROM INTERVENTO i
        JOIN INT_TIPO t on (i.tipo = t.tipo)
        WHERE usernameTecnico = ?;
    """;

    public static String INT_MACCHINARIO = """
        SELECT m.codiceInstallazione, m.tipologia FROM INT_MACCHINARIO im
        JOIN MACCHINARIO m on (m.codiceInstallazione = im.codiceInstallazione)
        WHERE im.codiceIntervento = ?
    """;

    public static String INT_MACC_FOTO = """
        SELECT i.codiceImpianto, i.siglaProvincia, i.indirizzo FROM MACC_FOTOVOLTAICO mf
        JOIN IMPIANTO i on (mf.codiceImpianto = i.codiceImpianto && mf.siglaProvincia = i.siglaProvincia)
        WHERE mf.codiceInstallazione = ?
    """;

    public static String INT_MACC_EOLICO = """
        SELECT i.codiceImpianto, i.siglaProvincia, i.indirizzo FROM MACC_EOLICO mf
        JOIN IMPIANTO i on (mf.codiceImpianto = i.codiceImpianto && mf.siglaProvincia = i.siglaProvincia)
        WHERE mf.codiceInstallazione = ?
    """;

    public static String INT_MACC_BIOGAS = """
        SELECT i.codiceImpianto, i.siglaProvincia, i.indirizzo FROM MACC_BIOGAS mf
        JOIN IMPIANTO i on (mf.codiceImpianto = i.codiceImpianto && mf.siglaProvincia = i.siglaProvincia)
        WHERE mf.codiceInstallazione = ?
    """;
}
