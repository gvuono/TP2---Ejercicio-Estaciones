package EjercicioTP2;

import java.util.*;

public class RedSubte {
    // mapeamos las estaciones de cada linea
    private final Map<Integer, List<Integer>> estacionALineas = new HashMap<>();
    private final List<Linea> lineas = new ArrayList<>(); 

    private List<List<Integer>> grafoLineas = new ArrayList<>();

    public RedSubte() {
    }

    public void agregarLinea(List<Linea> nuevasLineas) {
        for (Linea l : nuevasLineas) {
            int idLinea = l.getId();

            // buscamos que la linea concuerde con la id 
            while (lineas.size() <= idLinea) {
                lineas.add(null);
            }

            // Insertar la línea en su índice correcto
            lineas.set(idLinea, l);

            // Registrar estaciones → líneas
            for (int est : l.getEstaciones()) {
                estacionALineas
                    .computeIfAbsent(est, k -> new ArrayList<>())
                    .add(idLinea);
            }
        }
    }


    /*
     Se construye el grafo conectado las lineas que comparten estacion
     */
    public void construirGrafo() {
        int N = lineas.size();
        List<Set<Integer>> temp = new ArrayList<>(N + 1);
        for (int i = 0; i <= N; i++) temp.add(new HashSet<>());

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
        grafoLineas.add(Collections.emptyList()); 
        for (int i = 1; i <= N; i++) {
            grafoLineas.add(new ArrayList<>(temp.get(i)));
        }
    }

    public List<Integer> obtenerLineasQuePasanPor(int estacion) {
        return estacionALineas.getOrDefault(estacion, Collections.emptyList());
    }

    //buscamos el camino minimo
    public List<Integer> calcularCaminoMinimo(int estacionOrigen, int estacionDestino) {
        List<Integer> originLines = obtenerLineasQuePasanPor(estacionOrigen);
        List<Integer> destLines = obtenerLineasQuePasanPor(estacionDestino);
        
        if (estacionOrigen == estacionDestino) {
            return new ArrayList<>(); // sin líneas
        }

        Set<Integer> setOrigen = new HashSet<>(originLines);
        Set<Integer> setDestino = new HashSet<>(destLines);
        
        if (setOrigen.isEmpty() || setDestino.isEmpty()) {
            return Collections.emptyList();
        }
        
        if (grafoLineas == null || grafoLineas.size() <= 1) {
            construirGrafo();
        }

        BuscadordeCamino buscador = new BuscadordeCamino(grafoLineas);
        return buscador.bfsCaminoMinimo(setOrigen, setDestino);
    }
    
    public List<Linea> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    public Map<Integer, List<Integer>> getEstacionALineas() {
        return Collections.unmodifiableMap(estacionALineas);
    }
}
