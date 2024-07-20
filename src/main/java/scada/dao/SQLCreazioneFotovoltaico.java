package scada.dao;

public class SQLCreazioneFotovoltaico {
    public static String GET_AZIENDE = """
        SELECT DISTINCT(azienda) FROM MODELLO;
    """;

    public static String GET_MODELLO_FROM_AZIENDA = """
        SELECT nome, area FROM MODELLO
        WHERE azienda = ? AND area <= ?;
    """;

    public static String GET_CODICE_INTERNO = """
        SELECT MAX(mf.codiceInterno)+1 FROM MACC_FOTOVOLTAICO mf
        WHERE mf.codiceImpianto = ? && mf.siglaProvincia = ?
    """;

    public static String CREAZIONE_FOTOVOLTAICO = """
        INSERT INTO MACC_FOTOVOLTAICO (codiceImpianto, siglaProvincia, codiceInstallazione, codiceInterno, celle, kwhMax, angolo)
        VALUES (?, ?, ?, ?, ?, ?, ?);
    """;
}
