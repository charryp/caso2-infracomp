
import java.io.IOException;

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
    private int[][] matriz3;
    private ArrayList<ArrayList<Integer>> infoAuxiliar;

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
    public int[][] getMatriz3() {
        return matriz3;
    }
    public void setMatriz3(int[][] matriz3) {
        this.matriz3 = matriz3;
    }
    public ArrayList<ArrayList<Integer>> getInfoAuxiliar() {
        return infoAuxiliar;
    }
    public void setInfoAuxiliar(ArrayList<ArrayList<Integer>> infoAuxiliar) {
        this.infoAuxiliar = infoAuxiliar;
    }

    
	public void ejecutarOpcion1(String ruta) throws IOException{
        ArrayList<String> info = leerArchivo(ruta);
        int TP = Integer.parseInt(getTP(info));
        int NPROC = Integer.parseInt(getNPROC(info));
        ArrayList<ArrayList<Integer>> tams = getTAMS(info);

        for(int i = 0, j = 1 ; i < NPROC; i++, j++){
            System.out.println("Iniciando proceso " + j);
            int filas = tams.get(i).get(0);
            int columnas = tams.get(i).get(1);
            int NR = filas * columnas * 3; //3 matrices
            int NP = (int) Math.ceil((NR * 4) / TP); //4 bytes por int
            setMatriz1(generarMatrizRandom(filas, columnas));
            setMatriz2(generarMatrizRandom(filas, columnas));
            List<Integer> data = ejecutarSimulador(matriz1, matriz2, TP, NP);

            generarArchivo(TP, filas, columnas, NR, NP, data, i);
        }

    }
	public static ArrayList<String> leerArchivo(String nombreArchivo) throws IOException {
		Path ruta = Paths.get("DocsConfiguracion", nombreArchivo);
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

    public List<Integer> ejecutarSimulador(int[][] matrizA, int[][] matrizB, int TP, int NP){
        int[][] matrizC = sumarMatrices(matrizA.length, matrizA[0].length);
        setMatriz3(matrizC);
        System.out.println("Matrices sumadas exitosamente!!");
        ArrayList<Integer> infoA = rawMajorOrderGenerator(matrizA);
        ArrayList<ArrayList<Integer>> infoAuxiliarA = generarAuxiliar(1, matrizA);
        ArrayList<Integer> infoB = rawMajorOrderGenerator(matrizB);
        ArrayList<ArrayList<Integer>> infoAuxiliarB = generarAuxiliar(2, matrizB);
        ArrayList<Integer> infoC = rawMajorOrderGenerator(matrizC);
        ArrayList<ArrayList<Integer>> infoAuxiliarC = generarAuxiliar(3, matrizC);
        ArrayList<Integer> info = new ArrayList<>();
        info.addAll(infoA);
        info.addAll(infoB);
        info.addAll(infoC);
        ArrayList<ArrayList<Integer>> infoAuxiliar = new ArrayList<>();
        infoAuxiliar.addAll(infoAuxiliarA);
        infoAuxiliar.addAll(infoAuxiliarB);
        infoAuxiliar.addAll(infoAuxiliarC);
        setInfoAuxiliar(infoAuxiliar);
        //ACA SE DEBEN HACER LAS PAGINANCIONES.
        PageTableEntry pageTableEntry = new PageTableEntry();
        int index = 0;
        for(int i = 0 ; i < NP; i++){
            Page pagina = new Page(TP, i);
            for(int j = 0; j < TP && index < info.size(); j++, index++){
                pagina.addInfo(info.get(index));
            }
            pageTableEntry.addPage(i, pagina);
        }
        return info;

    }
    public ArrayList<ArrayList<Integer>>  generarAuxiliar(int num, int[][] matriz){
        ArrayList<ArrayList<Integer>> info = new ArrayList<>();
        for(int i = 0 ; i < matriz.length; i++){
            for(int j = 0 ; j < matriz[0].length; j++){
                ArrayList<Integer> auxiliar = new ArrayList<>();
                auxiliar.add(num);
                auxiliar.add(i);
                auxiliar.add(j);
                info.add(auxiliar);
            }
        }
        return info;
    }

    public ArrayList<Integer> rawMajorOrderGenerator(int[][] matriz){
        ArrayList<Integer> info = new ArrayList<>();
        for(int i = 0 ; i < matriz.length; i++){
            for(int j = 0 ; j < matriz[0].length; j++){
                info.add(matriz[i][j]);
            }
        }
        return info;
    }

    public void generarArchivo(int TP, int NF, int NC, int NR, int NP, List<Integer> data, int numProceso) throws IOException{
        Path carpeta = Paths.get("SalidaOpcion1");
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }
        String nombreArchivo = "proc" + numProceso + ".txt";
        Path rutaArchivo = carpeta.resolve(nombreArchivo);
        StringBuilder sb = new StringBuilder();
        sb.append("TP=" + TP).append("\n");
        sb.append("NF=" + NF).append("\n");
        sb.append("NC=" + NC).append("\n");
        sb.append("NR=" + NR).append("\n");
        sb.append("NP=" + NP).append("\n");
        for(int i = 0; i < data.size() ; i++){
            String infoCelda;
            infoCelda = "M" + getInfoAuxiliar().get(i).get(0) + ": [" + getInfoAuxiliar().get(i).get(1) + "-" + getInfoAuxiliar().get(i).get(2) + "], ";
            infoCelda += i / TP + ", ";
            infoCelda += i % TP + ", ";
            infoCelda += "r";
            sb.append(infoCelda).append("\n");
        }

        Files.write(rutaArchivo, sb.toString().getBytes());
    }

}
