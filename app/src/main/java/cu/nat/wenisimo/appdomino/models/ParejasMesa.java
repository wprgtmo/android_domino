package cu.nat.wenisimo.appdomino.models;

public class ParejasMesa {
    private String id;
    private String boleta_id;
    private String pareja_id;
    private String salidor;
    private String tantos;
    private String resultado;
    private String ganador;
    private String inicio;
    private String duracion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoleta_id() {
        return boleta_id;
    }

    public void setBoleta_id(String boleta_id) {
        this.boleta_id = boleta_id;
    }

    public String getPareja_id() {
        return pareja_id;
    }

    public void setPareja_id(String pareja_id) {
        this.pareja_id = pareja_id;
    }

    public String getSalidor() {
        return salidor;
    }

    public void setSalidor(String salidor) {
        this.salidor = salidor;
    }

    public String getTantos() {
        return tantos;
    }

    public void setTantos(String tantos) {
        this.tantos = tantos;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
