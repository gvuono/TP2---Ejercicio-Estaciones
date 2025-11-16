import java.util.*;

public class RedSubte {
    // estacion -> listado de líneas que pasan por esa estación
    private final Map<Integer, List<Integer>> estacionALineas = new HashMap<>();
    private final List<Linea> lineas = new ArrayList<>(); // indexadas por id-1 usualmente
    // grafo de adyacencia entre líneas: se construye en construirGrafo()
    private List<List<Integer>> grafoLineas = new ArrayList<>();

    public RedSubte() {
    }

    /**
     * Agrega varias líneas al objeto RedSubte.
     * Espera que las líneas tengan IDs en 1..N y únicos.
     */
    public void agregarLinea(List<Linea> nuevasLineas) {
        for (Linea l : nuevasLineas) {
            lineas.add(l);
            int idLinea = l.getId();
            for (int est : l.getEstaciones()) {
                estacionALineas.computeIfAbsent(est, k -> new ArrayList<>()).add(idLinea);
            }
        }
    }

    /**
     * Construye el grafo de líneas: conexión entre líneas que comparten al menos una estación.
     * Se usa un Set interno por nodo para evitar duplicados.
     */
    public void construirGrafo() {
        int N = lineas.size();
        // grafo indexado de 1..N => tamaño N+1
        List<Set<Integer>> temp = new ArrayList<>(N + 1);
        for (int i = 0; i <= N; i++) temp.add(new HashSet<>());

        // Para cada estación, conecto todas las líneas que pasan por ella
        for (Map.Entry<Integer, List<Integer>> entry : estacionALineas.entrySet()) {
            List<Integer> listaLineas = entry.getValue();
            int k = listaLineas.size();
            for (int i = 0; i < k; i++) {
                for (int j = i + 1; j < k; j++) {
                    int a = listaLineas.get(i);
                    int b = listaLineas.get(j);
                    temp.get(a).add(b);
                    temp.get(b).add(a);
                }
            }
        }

        // Convertir a List<List<Integer>>
        grafoLineas = new ArrayList<>(N + 1);
        grafoLineas.add(Collections.emptyList()); // índice 0 no usado
        for (int i = 1; i <= N; i++) {
            grafoLineas.add(new ArrayList<>(temp.get(i)));
        }
    }

    public List<Integer> obtenerLineasQuePasanPor(int estacion) {
        return estacionALineas.getOrDefault(estacion, Collections.emptyList());
    }

    /**
     * Calcula el camino mínimo (en número de líneas) entre origen y destino,
     * devolviendo la secuencia de IDs de líneas.
     */
    public List<Integer> calcularCaminoMinimo(int estacionOrigen, int estacionDestino) {
        List<Integer> originLines = obtenerLineasQuePasanPor(estacionOrigen);
        List<Integer> destLines = obtenerLineasQuePasanPor(estacionDestino);

        // Convertir a sets
        Set<Integer> setOrigen = new HashSet<>(originLines);
        Set<Integer> setDestino = new HashSet<>(destLines);

        if (setOrigen.isEmpty() || setDestino.isEmpty()) {
            return Collections.emptyList(); // por seguridad
        }

        // Si grafo no construido, construirlo
        if (grafoLineas == null || grafoLineas.size() <= 1) {
            construirGrafo();
        }

        BuscadorDeCamino buscador = new BuscadorDeCamino(grafoLineas);
        return buscador.bfsCaminoMinimo(setOrigen, setDestino);
    }

    // getters básicos si se necesitan
    public List<Linea> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    public Map<Integer, List<Integer>> getEstacionALineas() {
        return Collections.unmodifiableMap(estacionALineas);
    }
}
