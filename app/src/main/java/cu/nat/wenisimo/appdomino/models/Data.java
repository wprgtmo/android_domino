package cu.nat.wenisimo.appdomino.models;

public class Data {
    private Integer id;
    private Integer numero;
    private Integer boleta_id;
    private Integer puntos;
    private Integer duracion;
    private Integer pareja_ganadora;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getBoleta_id() {
        return boleta_id;
    }

    public void setBoleta_id(Integer boleta_id) {
        this.boleta_id = boleta_id;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getPareja_ganadora() {
        return pareja_ganadora;
    }

    public void setPareja_ganadora(Integer pareja_ganadora) {
        this.pareja_ganadora = pareja_ganadora;
    }
}
