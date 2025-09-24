import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static String[] menuOpciones = new String[] {
				"0- Salir de la aplicacion",
				"1- Ejecutar la opcion 1\n",
                "2- Ejecutar la opcion 2\n"};
    
    public static final String input(String mensaje) {
		try {
			System.out.print(mensaje);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		} catch (IOException e) {
			System.out.println("Error leyendo de la consola");
			e.printStackTrace();
		}
		return null;
	}
    public static void mostrarMenu(String[] menuOpciones) {
		System.out.println("\n");
		for (String opcion : menuOpciones) {
			System.out.println(opcion);
		}
	}

    public static void main(String args[]) throws IOException{
        System.out.println("Bienvenido a la aplicacion...   ");
        System.out.println("--------------------------------");
        System.out.println("IMPORTANTE: Los archivos de configuracion debe ubicarlos en la carpeta DocsConfiguracion");
        System.out.println("IMPORTANTE: La opcion 2 no puede ejecutarse hasta que la opcion 1 haya sido ejecutada exitosamente");
		System.out.println("Entendido? Presione Enter para continuar...");
        System.out.println("--------------------------------");
        System.out.println("A continuacion, las opciones de la aplicacion: ");
        boolean continuar = true;

        while (continuar) {
				mostrarMenu(menuOpciones);
				int seleccion = Integer.parseInt(input("Seleccione una opcion: "));

				switch (seleccion) {
					case 0:
						input("Desea salir de la App? Presione Enter...");
						System.out.println("-----------------------------------------------------");
						System.out.println("Vuelva pronto!!");
						System.out.println("-----------------------------------------------------");
						System.exit(0);

					case 1:
						input("Desea continuar con la opcion 1? Presiones Enter...");
						String ruta = input("Ingrese el nombre del archivo de configuracion (incluya la extension .txt): ");
						Opcion1 opcion1 = new Opcion1();
						opcion1.ejecutarOpcion1(ruta);
						System.out.println("-----------------------------------------------------");
						System.out.println("Revise la carpeta SalidaOpcion1!! Puede consultar los archivos de respuesta alli.");
						System.out.println("-----------------------------------------------------");
						break;
					case 2:
						input("Desea continuar con la opcion 2? Presiones Enter...");
						//Opcion 2
						System.out.println("-----------------------------------------------------");
						System.out.println("La actividad ha sido calificada exitosamente!!");
						System.out.println("-----------------------------------------------------");
						break;
					default:
						input("La seleccion no es valida. Presione Enter y vuelva a intentar...");
						break;
				}

    }

    
    
}
}
