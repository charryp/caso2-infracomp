import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReferenceLoader {

    public Process loadFromFile(int pid, Path file) {
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            Integer TP = null, NF = null, NC = null, NR = null, NP = null;

            // Lee cabecera
            for (int i = 0; i < 5; i++) {
                line = br.readLine();
                if (line == null) throw new IOException("Archivo corto: falta cabecera");
                line = line.trim();
                if (line.startsWith("TP=")) TP = Integer.parseInt(line.substring(3).trim());
                else if (line.startsWith("NF=")) NF = Integer.parseInt(line.substring(3).trim());
                else if (line.startsWith("NC=")) NC = Integer.parseInt(line.substring(3).trim());
                else if (line.startsWith("NR=")) NR = Integer.parseInt(line.substring(3).trim());
                else if (line.startsWith("NP=")) NP = Integer.parseInt(line.substring(3).trim());
                else throw new IOException("Cabecera inesperada: " + line);
            }

            if (TP == null || NF == null || NC == null || NR == null || NP == null) {
                throw new IOException("Cabecera incompleta en " + file);
            }

            System.out.println("PROC " + pid + " == Leyendo archivo de configuración ==");
            System.out.println("PROC " + pid + "leyendo TP. Tam Páginas: " + TP);
            System.out.println("PROC " + pid + "leyendo NF. Num Filas: " + NF);
            System.out.println("PROC " + pid + "leyendo NC. Num Cols: " + NC);
            System.out.println("PROC " + pid + "leyendo NR. Num Referencias: " + NR);
            System.out.println("PROC " + pid + "leyendo NP. Num Paginas: " + NP);
            System.out.println("PROC " + pid + "== Terminó de leer archivo de configuración ==");

            // Lee referencias
            List<DVReference> refs = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Formato: M1:[0-0],0,0,r
                String[] parts = line.split(",");
                if (parts.length != 4) continue; // ignora rarezas
                String etiqueta = parts[0].trim();
                int vpn = Integer.parseInt(parts[1].trim());
                int offset = Integer.parseInt(parts[2].trim());
                char op = parts[3].trim().toLowerCase().charAt(0);
                refs.add(new DVReference(etiqueta, vpn, offset, op));
            }
            // Si NR no coincide, no fallamos, pero usamos refs.size()
            int nrEff = refs.size();

            return new Process(pid, TP, NF, NC, nrEff, NP, refs);

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo " + file + ": " + e.getMessage(), e);
        }
    }
}

