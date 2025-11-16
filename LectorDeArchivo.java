import java.io.*;
import java.util.*;

public class LectorDeArchivo {
    /**
     * Lee el archivo y devuelve una RedSubte completamente poblada.
     * Se asume el formato del enunciado.
     */
    public RedSubte leer(String ruta) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String first = br.readLine();
            if (first == null) throw new IOException("Archivo vacío");
            String[] parts = first.trim().split("\\s+");
            int N = Integer.parseInt(parts[0]);
            int M = Integer.parseInt(parts[1]); // no se usa directamente aquí

            List<Linea> lineas = new ArrayList<>(N);

            for (int i = 1; i <= N; i++) {
                String lineaStr = br.readLine();
                while (lineaStr != null && lineaStr.trim().isEmpty()) {
                    // saltar líneas en blanco si existieran
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

            // última línea: origen y destino
            String ultima = br.readLine();
            while (ultima != null && ultima.trim().isEmpty()) {
                ultima = br.readLine();
            }
            if (ultima == null) throw new IOException("Formato inválido: faltan origen/destino");
            // NOTA: no procesamos origen/destino aquí. Main lo leerá aparte.
            // Pero para construir la RedSubte solo necesitamos las líneas.
            RedSubte red = new RedSubte();
            red.agregarLinea(lineas);
            // No construimos el grafo aquí; lo hará RedSubte cuando sea necesario.
            return red;
        }
    }
}
