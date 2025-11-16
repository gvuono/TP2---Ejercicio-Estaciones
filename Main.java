import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String input = "subte.in";
        String output = "SUBTE.OUT";
        // Si prefieres pasar argumentos por línea de comando:
        if (args.length >= 2) {
            input = args[0];
            output = args[1];
        }

        try {
            // Leemos todo el archivo manualmente para extraer origen/destino
            BufferedReader br = new BufferedReader(new FileReader(input));
            String first = br.readLine();
            if (first == null) throw new IOException("Archivo vacío");
            String[] parts = first.trim().split("\\s+");
            int N = Integer.parseInt(parts[0]);
            int M = Integer.parseInt(parts[1]);

            List<Linea> lineas = new ArrayList<>(N);
            for (int i = 1; i <= N; i++) {
                String lineaStr = br.readLine();
                while (lineaStr != null && lineaStr.trim().isEmpty()) {
                    lineaStr = br.readLine();
                }
                if (lineaStr == null) throw new IOException("Formato inválido: faltan líneas");
                String[] tokens = lineaStr.trim().split("\\s+");
                int k = Integer.parseInt(tokens[0]);
                List<Integer> estaciones = new ArrayList<>(k);
                for (int t = 1; t <= k; t++) {
                    estaciones.add(Integer.parseInt(tokens[t]));
                }
                lineas.add(new Linea(i, estaciones));
            }

            String ultima = br.readLine();
            while (ultima != null && ultima.trim().isEmpty()) ultima = br.readLine();
            if (ultima == null) throw new IOException("Formato inválido: faltan origen/destino");
            String[] od = ultima.trim().split("\\s+");
            int origen = Integer.parseInt(od[0]);
            int destino = Integer.parseInt(od[1]);

            br.close();

            // Construir red
            RedSubte red = new RedSubte();
            red.agregarLinea(lineas);
            // construir grafo
            red.construirGrafo();

            // Calcular camino mínimo (en número de líneas)
            List<Integer> camino = red.calcularCaminoMinimo(origen, destino);

            // Escribir resultado
            EscritorDeArchivo escritor = new EscritorDeArchivo();
            if (camino.isEmpty()) {
                // Por seguridad: no debería pasar según enunciado, pero escribimos 0 y nada
                escritor.escribirResultado(output, 0, Collections.emptyList());
            } else {
                escritor.escribirResultado(output, camino.size(), camino);
            }

            System.out.println("Proceso completado. Resultado escrito en " + output);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
