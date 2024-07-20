package scada.dao;

public class InterventiRecord {
    private Integer codiceIntervento;
    private Boolean completato;
    private String usernameTecnico;
    private String descrizioneIntervento;
    private String noteIntervento;

    public InterventiRecord(Integer codiceIntervento, Boolean completato, String usernameTecnico, String descrizioneIntervento, String noteIntervento) {
        this.codiceIntervento = codiceIntervento;
        this.completato = completato;
        this.usernameTecnico = usernameTecnico;
        this.descrizioneIntervento = descrizioneIntervento;
        this.noteIntervento = noteIntervento;
    }
    public String getNoteIntervento() {
        return noteIntervento;
    }

    public Boolean getCompletato() {
        return completato;
    }
    public String getUsernameTecnico() {
        return usernameTecnico;
    }
    public Integer getCodiceIntervento() {
        return codiceIntervento;
    }

    public String getDescrizioneIntervento() {
        return descrizioneIntervento;
    }
}
