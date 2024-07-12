package scada.dao;

public class SQLAddetti {

    public static String IMPIANTI = """
        SELECT M.codiceImpianto, M.siglaProvincia, I.uomoInSito
        FROM MONITORAGGIO M JOIN IMPIANTO I ON (M.codiceImpianto = I.codiceImpianto)
        AND (M.siglaProvincia = I.siglaProvincia)
        WHERE usernameAddetto = ?;
    """;

    public static String MACCHINARI = """
        SELECT I.tipologia
        FROM IMPIANTO I JOIN MONITORAGGIO M ON (I.codiceImpianto = M.codiceImpianto)
        AND (I.siglaProvincia = M.siglaProvincia)
        WHERE M.codiceImpianto = ?
        AND M.siglaProvincia = ?;
    """;

    public static String MACCHINARI_FOTOVOLTAICI = """
        SELECT M.codiceInstallazione, M.nomeModello, M.azienda, M.status
        FROM MACCHINARIO M JOIN MACC_FOTOVOLTAICO MF ON (M.codiceInstallazione = MF.codiceInstallazione);
    """;

    public static String MACCHINARI_EOLICI = """
        SELECT M.codiceInstallazione, M.nomeModello, M.azienda, M.status
        FROM MACCHINARIO M JOIN MACC_EOLICO ME ON (M.codiceInstallazione = ME.codiceInstallazione);
    """;

    public static String MACCHINARI_BIOGAS = """
        SELECT M.codiceInstallazione, M.nomeModello, M.azienda, M.status
        FROM MACCHINARIO M JOIN MACC_BIOGAS MB ON (M.codiceInstallazione = MB.codiceInstallazione);
    """;

    public static String START = """
        UPDATE MACCHINARIO
        SET status = 1
        WHERE (codiceInstallazione = ? AND status = 2);
    """;

    public static String STOP = """
        UPDATE MACCHINARIO
        SET status = 2
        WHERE (codiceInstallazione = ? AND status = 1);
    """;

}
