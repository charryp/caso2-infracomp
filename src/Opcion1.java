
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Opcion1 {
    private int[][] matriz1;
    private int[][] matriz2;

    public int[][] getMatriz1() {
        return matriz1;
    }

    public void setMatriz1(int[][] matriz1) {
        this.matriz1 = matriz1;
    }

    public int[][] getMatriz2() {
        return matriz2;
    }

    public void setMatriz2(int[][] matriz2) {
        this.matriz2 = matriz2;
    }

    
	public void ejecutarOpcion1(String ruta) throws IOException{
        ArrayList<String> info = leerArchivo(ruta);
        int TP = Integer.parseInt(getTP(info));
        int NPROC = Integer.parseInt(getNPROC(info));
        ArrayList<ArrayList<Integer>> tams = getTAMS(info);

        for(int i = 0 ; i < NPROC; i++){
            System.out.println("Iniciando proceso " + (i + 1));
        }
        setMatriz1(generarMatrizRandom(tams[0], tams[1]));
        setMatriz2(generarMatrizRandom(tams[0], tams[1]));
        ejecutarSimulador(matriz1, matriz2);
        //Inicio proceso 2
        setMatriz1(generarMatrizRandom(tams[2], tams[3]));
        setMatriz2(generarMatrizRandom(tams[2], tams[3]));
        ejecutarSimulador(matriz1, matriz2);

    }
	public static ArrayList<String> leerArchivo(String nombreArchivo) throws IOException {
		Path ruta = Paths.get("src", "DocsConfiguracion", nombreArchivo);
		return new ArrayList<>(Arrays.asList(Files.readString(ruta).split("\n")));
	}

    public static String getTP(ArrayList<String> archivo){
        
        return archivo.get(0).replace("TP=", "");
    }
    public static String getNPROC(ArrayList<String> archivo){
        
        return archivo.get(1).replace("NPROC=", "");
    
    }

    public static ArrayList<ArrayList<Integer>> getTAMS(ArrayList<String> archivo){
        ArrayList<ArrayList<Integer>> allTAMS = new ArrayList<>();
        for(int i = 2 ; i < archivo.size(); i++){
            String[] tams = archivo.get(i).replace("TAMS=", "").split(",");
            for(String tam : tams){
                String[] dimensiones = tam.split("x");
                ArrayList<Integer> tamanos = new ArrayList<>();
                tamanos.add(Integer.parseInt(dimensiones[0]));
                tamanos.add(Integer.parseInt(dimensiones[1]));
                allTAMS.add(tamanos);
            }
        }

        return allTAMS;
    }

    public static int[][] generarMatrizRandom(int filas, int columnas) {
    int[][] matriz = new int[filas][columnas];
    Random rand = new Random();
    for (int i = 0; i < filas; i++) {
        for (int j = 0; j < columnas; j++) {
            matriz[i][j] = rand.nextInt();
        }
    }
    return matriz;
    }

	public int[][] sumarMatrices(int pnf, int pnc){
        int filas = pnf;
        int columnas = pnc;
        int[][] matriz3 = new int[filas][columnas];

        for(int i = 0 ; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                matriz3[i][j] = matriz1[i][j] + matriz2[i][j];
            }
        }
        return matriz3;
    }

    public void ejecutarSimulador(int[][] matrizA, int[][] matrizB){

    }

	public void generarArchivos(int numProcesos, int TP, int NF, int NC, int NR, int NP, List<String> direcciones) throws IOException{
		for(int i = 0 ; i < numProcesos; i++){
			Path ruta = Paths.get("src", "DocsConfiguracion", "salida" + i + ".txt");
			String contenido = "Texto a guardar";
			Files.write(ruta, Integer.toString(TP)+"\\n".getBytes());
		}
		
	}

}
