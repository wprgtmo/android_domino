package cu.nat.wenisimo.appdomino.models;

public class Pareja {
    private String id;
    private String nombre;
    private String evento_id;
    private String jugador1_id;
    private String jugador2_id;
    private Jugador jugador1;
    private Jugador jugador2;

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(String evento_id) {
        this.evento_id = evento_id;
    }

    public String getJugador1_id() {
        return jugador1_id;
    }

    public void setJugador1_id(String jugador1_id) {
        this.jugador1_id = jugador1_id;
    }

    public String getJugador2_id() {
        return jugador2_id;
    }

    public void setJugador2_id(String jugador2_id) {
        this.jugador2_id = jugador2_id;
    }
}
