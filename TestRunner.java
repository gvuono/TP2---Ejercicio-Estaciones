package EjercicioTP2;

import java.nio.file.*;
import java.util.*;

public class TestRunner {

    static class TestCase {
        String name;
        String input;
        String expectedOutput;

        TestCase(String name, String input, String expectedOutput) {
            this.name = name;
            this.input = input;
            this.expectedOutput = expectedOutput;
        }
    }

    public static void main(String[] args) throws Exception {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase("A_origen_eq_destino",
            "3 5\n" +
            "3 1 2 3\n" +
            "3 3 4 5\n" +
            "2 2 5\n" +
            "4 4\n",
            "0\n\n"));

        tests.add(new TestCase("B_misma_linea",
            "2 6\n" +
            "5 1 2 3 4 5\n" +
            "3 6 2 7\n" +
            "1 5\n",
            "1\n1\n"));

        tests.add(new TestCase("C_un_trasbordo",
            "3 8\n" +
            "3 1 2 3\n" +
            "3 3 4 5\n" +
            "3 5 6 7\n" +
            "1 7\n",
            "3\n1 2 3\n"));

        tests.add(new TestCase("D_min_trasbordos",
            "3 10\n" +
            "6 1 2 3 4 5 6\n" +
            "3 1 7 8\n" +
            "4 8 9 10 6\n" +
            "1 10\n",
            // Aceptamos "1 3" o "2 3" etc. como válidos; aquí esperamos una posible salida.
            // Para el test runner usaremos una comprobación flexible: si T==2 OK.
            "2\n2 3\n"));

        tests.add(new TestCase("E_hub",
            "6 7\n" +
            "2 1 2\n" +
            "2 1 3\n" +
            "2 1 4\n" +
            "2 1 5\n" +
            "2 1 6\n" +
            "2 1 7\n" +
            "2 7\n",
            "2\n1 6\n"));

        tests.add(new TestCase("F_duplicados",
            "3 5\n" +
            "5 1 2 3 2 4\n" +
            "2 4 5\n" +
            "3 5 2 1\n" +
            "1 5\n",
            "1\n3\n"));

        tests.add(new TestCase("G_no_conectado",
            "3 6\n" +
            "2 1 2\n" +
            "2 3 4\n" +
            "2 5 6\n" +
            "1 6\n",
            "0\n\n"));

        tests.add(new TestCase("H_ids_grandes",
            "2 100000\n" +
            "3 1 50000 99999\n" +
            "2 99999 100000\n" +
            "1 100000\n",
            "2\n1 2\n"));

        // Nota: El test K (gran generador) lo dejamos como manual o generar dinámicamente más abajo.

        // Directorios
        Path base = Paths.get(System.getProperty("user.dir"));
        Path inputDir = base.resolve("test_inputs");
        Path outputDir = base.resolve("test_outputs");
        Files.createDirectories(inputDir);
        Files.createDirectories(outputDir);

        int passed = 0;
        for (TestCase tc : tests) {
            System.out.println("=== Ejecutando test: " + tc.name + " ===");
            Path inFile = inputDir.resolve(tc.name + ".in");
            Path outFile = outputDir.resolve(tc.name + ".out");

            Files.write(inFile, tc.input.getBytes());

            // Ejecutar Main con args: inputPath outputPath
            try {
                Main.main(new String[]{inFile.toString(), outFile.toString()});
            } catch (Exception e) {
                System.err.println("Main lanzó excepción: " + e.getMessage());
            }

            // Leer lo generado (si existe)
            String generated = "";
            if (Files.exists(outFile)) {
                generated = new String(Files.readAllBytes(outFile)).replaceAll("\\r\\n", "\n");
            } else {
                System.out.println("Archivo de salida no generado: " + outFile);
            }

            String expected = tc.expectedOutput.replaceAll("\\r\\n", "\n");

            // Comparación flexible para test D: aceptamos cualquier T==2 (demostración)
            boolean ok;
            if (tc.name.equals("D_min_trasbordos")) {
                // parse generated T
                try {
                    String[] lines = generated.trim().split("\\n");
                    int Tgen = Integer.parseInt(lines[0].trim());
                    ok = (Tgen == 2);
                } catch (Exception ex) {
                    ok = false;
                }
            } else {
                // Igualdad exacta (ignorando espacios finales)
                ok = normalize(generated).equals(normalize(expected));
            }

            if (ok) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                System.out.println("--- Esperado ---");
                System.out.print(tc.expectedOutput);
                System.out.println("---- Generado ----");
                System.out.print(generated);
                System.out.println("------------------");
            }
            System.out.println();
        }

        System.out.println("Resumen: " + passed + " / " + tests.size() + " tests pasaron.");
    }

    static String normalize(String s) {
        if (s == null) return "";
        // Trim trailing spaces on each line and ensure trailing newline
        String[] lines = s.replaceAll("\\r\\n", "\n").split("\\n");
        StringBuilder sb = new StringBuilder();
        for (String l : lines) {
            sb.append(l.stripTrailing()).append("\n");
        }
        return sb.toString();
    }
}
