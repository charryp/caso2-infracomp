

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LectorConfiguracion {

	/**
	 * Lee el contenido de un archivo dentro de la carpeta DocsConfiguracion.
	 * @param nombreArchivo Nombre del archivo dentro de DocsConfiguracion
	 * @return El contenido del archivo como String
	 * @throws IOException Si ocurre un error de lectura
	 */
	public static String[] leerArchivo(String nombreArchivo) throws IOException {
		Path ruta = Paths.get("src", "DocsConfiguracion", nombreArchivo);
		return Files.readString(ruta).split("\n");
	}

    public static String getTP(String[] archivo){
        
        return archivo[0];
    }
    public static String getNPROC(String[] archivo){
        
        return archivo[1];
    
    }

    public static String getTAMS(String[] archivo){
        return archivo[2];
    }
}
