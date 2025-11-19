package EjercicioTP2;

import java.util.*;

public class BuscadordeCamino {
    private final List<List<Integer>> grafo; 

    public BuscadordeCamino(List<List<Integer>> grafo) {
        this.grafo = grafo;
    }

    /** El BFS arranca todas las lineas en lineasOrigen e intenta alcanzarlas en lineasDestino
     * Si encuentra camino lo devuelvo sino devolvera vacio.
     */
    public List<Integer> bfsCaminoMinimo(Set<Integer> lineasOrigen, Set<Integer> lineasDestino) {
        int n = grafo.size() - 1; // grafo indexado 1..N
        boolean[] visited = new boolean[n + 1];
        int[] parent = new int[n + 1]; // parent[line] -> apunta a la linea anterior 
        Arrays.fill(parent, -1);

        Queue<Integer> q = new ArrayDeque<>();
        
        List<Integer> origenOrdenado = new ArrayList<>(lineasOrigen);
        Collections.sort(origenOrdenado);
        // Si se encuentra linea se devuelve
        for (int s : lineasOrigen) {
            if (lineasDestino.contains(s)) {
                return Collections.singletonList(s);
            }
        }

        for (int s : lineasOrigen) {
            if (!visited[s]) {
                visited[s] = true;
                parent[s] = -1;
                q.add(s);
            }
        }

        int encontrado = -1;
        while (!q.isEmpty()) {
            int cur = q.poll();
            // Si cur es una línea destino, terminamos
            if (lineasDestino.contains(cur)) {
                encontrado = cur;
                break;
            }
            
            List<Integer> vecinos = new ArrayList<>(grafo.get(cur));
            Collections.sort(vecinos);
            
            for (int vecino : grafo.get(cur)) {
                if (!visited[vecino]) {
                    visited[vecino] = true;
                    parent[vecino] = cur;
                    q.add(vecino);
                }
            }
        }

        if (encontrado == -1) {
            return Collections.emptyList(); // esto por si no hay camino 
        }

        LinkedList<Integer> path = new LinkedList<>();
        int cur = encontrado;
        while (cur != -1) {
            path.addFirst(cur);
            cur = parent[cur];
        }
        return path;
    }
}
