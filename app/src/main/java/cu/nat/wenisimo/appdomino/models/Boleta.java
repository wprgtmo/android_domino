package cu.nat.wenisimo.appdomino.models;


import java.util.ArrayList;

public class Boleta {
    private String id;
    private String evento_id;
    private String ronda_id;
    private String mesa_id;
    private String es_valida;
    private String creado;
    private String actualizado;
    private String tiempo_juego;
    private Mesa mesa;
    private Ronda ronda;
    private ArrayList<BoletaPareja> boleta_parejas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(String evento_id) {
        this.evento_id = evento_id;
    }

    public String getRonda_id() {
        return ronda_id;
    }

    public void setRonda_id(String ronda_id) {
        this.ronda_id = ronda_id;
    }

    public String getMesa_id() {
        return mesa_id;
    }

    public void setMesa_id(String mesa_id) {
        this.mesa_id = mesa_id;
    }

    public String getEs_valida() {
        return es_valida;
    }

    public void setEs_valida(String es_valida) {
        this.es_valida = es_valida;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getActualizado() {
        return actualizado;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
    }

    public String getTiempo_juego() {
        return tiempo_juego;
    }

    public void setTiempo_juego(String tiempo_juego) {
        this.tiempo_juego = tiempo_juego;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Ronda getRonda() {
        return ronda;
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
    }

    public ArrayList<BoletaPareja> getBoleta_parejas() {
        return boleta_parejas;
    }

    public void setBoleta_parejas(ArrayList<BoletaPareja> boleta_parejas) {
        this.boleta_parejas = boleta_parejas;
    }
}
