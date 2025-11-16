import java.io.*;
import java.util.List;

public class EscritorDeArchivo {
    public void escribirResultado(String ruta, int cantidad, List<Integer> lineas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(String.valueOf(cantidad));
            bw.newLine();
            for (int i = 0; i < lineas.size(); i++) {
                if (i > 0) bw.write(" ");
                bw.write(String.valueOf(lineas.get(i)));
            }
            bw.newLine();
        }
    }
}
