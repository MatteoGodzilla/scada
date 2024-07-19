package scada.dao;

public class SQLResponsabili {

    public static String INTERVENTI = """
        SELECT I.codice, I.tipo, T.descrizione FROM INTERVENTO I
        JOIN INT_TIPO T on (I.tipo = T.tipo);
    """;

    public static String LISTA_MACCHINARI_IMPIANTO_FOTOVOLTAICO = """
        SELECT MF.codiceInstallazione, MF.siglaProvincia, MA.azienda, MA.nomeModello FROM MACC_FOTOVOLTAICO MF
        JOIN MACCHINARIO MA ON (MF.codiceInstallazione = MA.codiceInstallazione) WHERE MF.codiceImpianto = ?;
    """;

    public static String LISTA_MACCHINARI_IMPIANTO_EOLICO = """
        SELECT ME.codiceInstallazione, ME.siglaProvincia, MA.azienda, MA.nomeModello FROM MACC_EOLICO ME
        JOIN MACCHINARIO MA ON (ME.codiceInstallazione = MA.codiceInstallazione) WHERE ME.codiceImpianto = ?;
    """;

    public static String LISTA_MACCHINARI_IMPIANTO_BIOGAS = """
        SELECT MB.codiceInstallazione, MB.siglaProvincia, MA.azienda, MA.nomeModello FROM MACC_BIOGAS MB
        JOIN MACCHINARIO MA ON (MB.codiceInstallazione = MA.codiceInstallazione) WHERE MB.codiceImpianto = ?;
    """;

    public static String GET_REGIONE_BY_USR = """
        SELECT U.regione FROM USR_RESPONSABILE WHERE U.username = ?;
    """;

    public static String CREAZIONE_INTERVENTI = """
        INSERT INTO INTERVENTO (usernameResponsabile, tipo) VALUES (?, ?);
    """;

    public static String CREAZIONE_INTERVENTO_MACCHINARIO = """
        INSERT INTO INT_MACCHINARIO (codiceInstallazione, codiceIntervento) VALUES (?, ?);
    """;

    public static String CREAZIONE_INTERVENTO_IMPIANTO = """
        INSERT INTO INT_IMPIANTO (codiceImpianto, codiceIntervento, siglaProvincia) VALUES (?, ?, ?);
    """;

    public static String INTERVENTI_COMPLETATI = """
        SELECT I.codice, I.tipo, T.descrizione, I.note FROM INTERVENTO I
        JOIN INT_TIPO T on (I.tipo = T.tipo)
        WHERE completato = 1;
    """;

    public static String INTERVENTI_PER_TIPOLOGIA ="""
        SELECT I.codice, I.tipo, T.descrizione FROM INTERVENTO I
        JOIN INT_TIPO T on (I.tipo = T.tipo) WHERE T.descrizione = ?;
    """;

    public static String MODELLI_MACCHINARI = """
        SELECT M.azienda, M.nome, M.area FROM MODELLO;
    """;

    private static String CALCOLO_SPAZIO_DISPONIBILE = """
        SELECT SUM(M.area) FROM MODELLO M WHERE M.azienda, M.nomeModello IN
        (SELECT MA.azienda, MA.nomeModello FROM MACCHINARIO MA WHERE MA.codiceInstallazione IN
    """;

    public static String CALCOLO_SPAZIO_DISPONIBILE_BIOGAS = CALCOLO_SPAZIO_DISPONIBILE+"""
        (SELECT MB.codiceInstallazione FROM MACC_BIOGAS MB WHERE MB.codiceImpianto = ?));
    """;

    public static String CALCOLO_SPAZIO_DISPONIBILE_EOLICO = CALCOLO_SPAZIO_DISPONIBILE+"""
        (SELECT ME.codiceInstallazione FROM MACC_EOLICO ME WHERE ME.codiceImpianto = ?));
    """;

    public static String CALCOLO_SPAZIO_DISPONIBILE_FOTOVOLTAICO = CALCOLO_SPAZIO_DISPONIBILE+"""
        (SELECT MF.codiceInstallazione FROM MACC_FOTOVOLTAICO MF WHERE MF.codiceImpianto = ?));
    """;

    public static String LISTA_TIPOLOGIE = """
        SELECT T.descrizione FROM INT_TIPO;
    """;

    public static String LISTA_TECNICI_REGIONALI = """
        SELECT T.username FROM USR_TECNICO WHERE T.siglaProvincia IN
        (SELECT P.siglaProvincia FROM PROVINCIA P WHERE P.regione = ?);
    """;

    public static String IMPIANTI_REGIONALI = """
        SELECT I.codImpianto, I.siglaProvincia, I.indirizzo, I.area, I.tipologia FROM IMPIANTO I
        WHERE I.siglaProvincia IN (SELECT P.siglaProvincia FROM PROVINCIA P
        WHERE P.regione = ?);
    """;

    public static String IMPIANTI_NON_ASSEGNATI_A = """
        SELECT I.codImpianto, I.siglaProvincia, I.indirizzo, I.area, I.tipologia FROM IMPIANTO I
        WHERE I.codImpianto NOT IN (SELECT M.codiceImpianto FROM MONITORAGGIO M WHERE M.usernameAddetto = ?)
        AND I.siglaProvincia IN (SELECT P.siglaProvincia FROM PROVINCIA P WHERE P.regione = ?);
    """;

    public static String ADDETTI_PER_REGIONE = """
        SELECT A.username, A.nome, A.cognome FROM USR_ADDETTO WHERE A.regione = ?;
    """;

    public static String IMPIANTI_ASSEGNATI_A = """
        SELECT I.codImpianto, I.siglaProvincia, I.indirizzo, I.area, I.tipologia FROM IMPIANTO I
        WHERE I.codImpianto IN (SELECT m.codiceImpianto FROM MONITORAGGIO M WHERE M.usernameAddetto = ?);
    """;

    public static String LISTA_GARANZIE = """
        SELECT G.descrizione, G.costo, G.durataAnni FROM GARANZIA G WHERE G.azienda = ? AND G.nomeModello = ?;
    """;

    public static String CREAZIONE_IMPIANTO = """
        INSERT INTO IMPIANTO (siglaProvincia, indirizzo, area, tipologia) VALUES (?, ?, ?, ?);
    """;

    public static String CREAZIONE_BIOGAS = """
        INSERT INTO MACC_BIOGAS (codiceImpianto, siglaProvincia, codiceInstallazione, codiceInterno, kwhOttimo, kgBatteri, kgUmido) VALUES (?, ?, ?, ?, ?, ?, ?);
    """;

    public static String CREAZIONE_EOLICO = """
        INSERT INTO MACC_EOLICO (codiceImpianto, siglaProvincia, codiceInstallazione, codiceInterno) VALUES (?, ?, ?, ?);
    """;

    public static String CREAZIONE_FOTOVOLTAICO = """
        INSERT INTO MACC_FOTOVOLTAICO (codiceImpianto, siglaProvincia, codiceInstallazione, codiceInterno, celle, kwhMax, angolo) VALUES (?, ?, ?, ?, ?, ?, ?);
    """;

    public static String CREAZIONE_MACCHINARIO = """
        INSERT INTO MACCHINARIO (dataInstallazione, tipologia, azienda, nomeModello, durataGaranzia) VALUES (?, ?, ?, ?, ?);
    """;

    public static String ASSEGNA_IMPIANTO_A = """
        INSERT INTO MONITORAGGIO (usernameAddetto, codiceImpianto, siglaProvincia) VALUES (?, ?, ?);
    """;
}


