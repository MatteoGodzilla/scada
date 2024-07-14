package scada.dao;

public class SQLAddetti {

    public static String IMPIANTI = """
        SELECT M.codiceImpianto, M.siglaProvincia, I.uomoInSito
        FROM MONITORAGGIO M JOIN IMPIANTO I ON (M.codiceImpianto = I.codiceImpianto)
        AND (M.siglaProvincia = I.siglaProvincia)
        WHERE usernameAddetto = ?;
    """;

    public static String TIPOLOGIA = """
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

    public static String METEO = """
        SELECT IR.ts, IR.vento, IR.uv
        FROM IMP_RILEVAZIONE IR
        WHERE IR.codiceImpianto = ?
        AND IR.siglaProvincia = ?
        ORDER BY IR.ts DESC
        LIMIT 1;
    """;

    public static String REPORT_IMPIANTO_FOTOVOLTAICO = """
        SELECT SUM(MP.kwh) AS Produzione
        FROM MACC_PRODUZIONE MP JOIN MACC_FOTOVOLTAICO MF ON (MP.codiceInstallazione = MF.codiceInstallazione)
        WHERE MF.codiceIMpianto = ?
        AND MF.siglaProvincia = ?;
    """;

    public static String REPORT_IMPIANTO_EOLICO = """
        SELECT SUM(MP.kwh) AS Produzione
        FROM MACC_PRODUZIONE MP JOIN MACC_EOLICO ME ON (MP.codiceInstallazione = ME.codiceInstallazione)
        WHERE ME.codiceIMpianto = ?
        AND ME.siglaProvincia = ?;
    """;

    public static String REPORT_IMPIANTO_BIOGAS = """
        SELECT SUM(MP.kwh) AS Produzione
        FROM MACC_PRODUZIONE MP JOIN MACC_BIOGAS MB ON (MP.codiceInstallazione = MB.codiceInstallazione)
        WHERE MB.codiceIMpianto = ?
        AND MB.siglaProvincia = ?;
    """;
}
