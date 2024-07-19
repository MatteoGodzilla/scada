package scada.dao;

public class AddettoRecord {
    private String username;
    private String nome;
    private String cognome;
    public AddettoRecord(String username, String nome, String cognome) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
    }
    public String getUsername() {
        return username;
    }
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
}
