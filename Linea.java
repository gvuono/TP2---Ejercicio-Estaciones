import java.util.List;

public class Linea {
    private final int id;
    private final List<Integer> estaciones;

    public Linea(int id, List<Integer> estaciones) {
        this.id = id;
        this.estaciones = estaciones;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getEstaciones() {
        return estaciones;
    }

    public boolean contieneEstacion(int estacionId) {
        return estaciones.contains(estacionId);
    }
}
