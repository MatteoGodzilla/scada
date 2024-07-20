package scada.dao;

public class InterventiRecord {
    private Integer codiceIntervento;
    private Integer tipoIntervento;
    private String descrizioneIntervento;
    public InterventiRecord(Integer codiceIntervento, Integer tipoIntervento, String descrizioneIntervento) {
        this.codiceIntervento = codiceIntervento;
        this.tipoIntervento = tipoIntervento;
        this.descrizioneIntervento = descrizioneIntervento;
    }
    public Integer getCodiceIntervento() {
        return codiceIntervento;
    }
    public Integer getTipoIntervento() {
        return tipoIntervento;
    }
    public String getDescrizioneIntervento() {
        return descrizioneIntervento;
    }
}
