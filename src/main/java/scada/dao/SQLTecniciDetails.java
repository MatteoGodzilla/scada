package scada.dao;

public class SQLTecniciDetails {
    public static String SQL_UOMO_IN_SITO = """
        UPDATE IMPIANTO
        SET uomoInSito = 1
        WHERE (codiceImpianto = ? AND siglaProvincia = ?);
    """;

    public static String SQL_INIZIO_INT_MACCHINARIO = """
        UPDATE MACCHINARIO
        SET status = 3
        WHERE codiceInstallazione = ?;
    """;

    public static String SQL_FINE_INTERVENTO = """
        UPDATE INTERVENTO
        SET completato = 1, note = ?
        WHERE codice = ?;
    """;

    public static String SQL_FINE_INT_MACCHINARIO = """
        UPDATE MACCHINARIO
        SET status = 2
        WHERE (codiceInstallazione = ? AND status = 3);
    """;

    public static String SQL_FINE_INT_IMPIANTO = """
        UPDATE IMPIANTO
        SET inOperazione = 0
        WHERE (codiceImpianto = ? AND siglaProvincia = ?);
    """;
}
