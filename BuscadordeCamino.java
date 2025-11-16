package EjercicioTP2;

import java.util.*;

public class BuscadordeCamino {
    private final List<List<Integer>> grafo; // grafo de líneas (1..N). grafo.get(i) -> vecinos de línea i

    public BuscadordeCamino(List<List<Integer>> grafo) {
        this.grafo = grafo;
    }

    /**
     * BFS multi-source: parte de todas las líneas en lineasOrigen y busca
     * alcanzar cualquier línea en lineasDestino. Devuelve la secuencia de IDs
     * de líneas (en orden) o lista vacía si no hay camino.
     */
    public List<Integer> bfsCaminoMinimo(Set<Integer> lineasOrigen, Set<Integer> lineasDestino) {
        int n = grafo.size() - 1; // grafo indexado 1..N
        boolean[] visited = new boolean[n + 1];
        int[] parent = new int[n + 1]; // parent[line] = linea anterior en el camino
        Arrays.fill(parent, -1);

        Queue<Integer> q = new ArrayDeque<>();
        // Si alguna línea está en ambos conjuntos, devolvemos esa línea
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
            for (int vecino : grafo.get(cur)) {
                if (!visited[vecino]) {
                    visited[vecino] = true;
                    parent[vecino] = cur;
                    q.add(vecino);
                }
            }
        }

        if (encontrado == -1) {
            return Collections.emptyList(); // según el enunciado siempre hay camino, pero por si acaso
        }

        // Reconstruir camino
        LinkedList<Integer> path = new LinkedList<>();
        int cur = encontrado;
        while (cur != -1) {
            path.addFirst(cur);
            cur = parent[cur];
        }
        return path;
    }
}
