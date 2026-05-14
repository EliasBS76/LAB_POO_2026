package practica_09;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;


public class Main 
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileIOManager ioManager = new FileIOManager();
        ItemManager itemManager = new ItemManager();
        String dirPath = "data";

        try {
            ioManager.createDirectory(dirPath);
        } catch (Exception e) {
            System.out.println("Error inicializando: " + e.getMessage());
        }

        while (true) {
            System.out.println("\n--- SISTEMA DE GESTION I/O ---");
            System.out.println("1. Escribir texto");
            System.out.println("2. Leer texto");
            System.out.println("3. Guardar objetos (Serializacion)");
            System.out.println("4. Cargar objetos");
            System.out.println("5. Procesar CSV");
            System.out.println("6. Exportar a JSON alternativo");
            System.out.println("7. Crear Backup");
            System.out.println("8. Probar archivo binario");
            System.out.println("9. Salir");
            System.out.print("Opcion: ");
            
            String option = scanner.nextLine();
            
            try {
                switch (option) {
                    case "1":
                        System.out.print("Ingresa el texto: ");
                        ioManager.writeText(dirPath + "/texto.txt", scanner.nextLine());
                        System.out.println("Guardado.");
                        break;
                    case "2":
                        System.out.println("Contenido:\n" + ioManager.readText(dirPath + "/texto.txt"));
                        break;
                    case "3":
                        List<Item> listToSave = new ArrayList<>();
                        listToSave.add(new Item("101", "Monitor", 250.50));
                        listToSave.add(new Item("102", "Teclado", 45.00));
                        itemManager.saveObjects(dirPath + "/items.dat", listToSave);
                        System.out.println("Objetos serializados correctamente.");
                        break;
                    case "4":
                        List<Item> loaded = itemManager.loadObjects(dirPath + "/items.dat");
                        for (Item i : loaded) {
                            System.out.println(i.getId() + " | " + i.getName() + " | " + i.getPrice());
                        }
                        break;
                    case "5":
                        System.out.print("Ruta del CSV (ej. data/datos.csv): ");
                        List<Item> csvItems = itemManager.processCSV(scanner.nextLine());
                        for (Item i : csvItems) {
                            System.out.println("Cargado: " + i.getName());
                        }
                        break;
                    case "6":
                        List<Item> listToExport = new ArrayList<>();
                        listToExport.add(new Item("201", "Raton", 20.00));
                        listToExport.add(new Item("202", "Webcam", 60.00));
                        itemManager.exportToJson(dirPath + "/export.json", listToExport);
                        System.out.println("Exportado a JSON exitosamente.");
                        break;
                    case "7":
                        System.out.print("Ruta del archivo a respaldar: ");
                        ioManager.createBackup(scanner.nextLine());
                        System.out.println("Backup creado con timestamp.");
                        break;
                    case "8":
                        String testBinary = "Datos binarios de prueba";
                        ioManager.writeBinary(dirPath + "/binario.bin", testBinary.getBytes(StandardCharsets.UTF_8));
                        byte[] readData = ioManager.readBinary(dirPath + "/binario.bin");
                        System.out.println("Leido binario: " + new String(readData, StandardCharsets.UTF_8));
                        break;
                    case "9":
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (Exception e) {
                System.out.println("Error en la operacion: " + e.getMessage());
            }
        }
    }
    
}
