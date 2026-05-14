package practica_08;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import practica_09.Estudiante;

public class StudentManager {

    private List<Estudiante> estudiantes = new ArrayList<>();

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    // ─── Lectura de CSV ────────────────────────────────────────────────────────

    public void cargarCSV(String rutaCSV) {
        File archivo = new File(rutaCSV);
        if (!archivo.exists()) {
            System.out.println("Archivo CSV no encontrado: " + rutaCSV);
            return;
        }

        estudiantes.clear();
        int lineasOk = 0;
        int lineasError = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Saltar encabezado
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] partes = linea.split(",");
                if (partes.length < 4) {
                    System.out.println("  [AVISO] Línea ignorada (formato incorrecto): " + linea);
                    lineasError++;
                    continue;
                }

                try {
                    String nombre   = partes[0].trim();
                    int matricula   = Integer.parseInt(partes[1].trim());
                    String carrera  = partes[2].trim();
                    double promedio = Double.parseDouble(partes[3].trim());

                    estudiantes.add(new Estudiante(nombre, matricula, carrera, promedio));
                    lineasOk++;
                } catch (NumberFormatException e) {
                    System.out.println("  [AVISO] Datos inválidos en línea: " + linea);
                    lineasError++;
                }
            }

            System.out.printf("CSV cargado: %d estudiantes importados, %d líneas ignoradas.%n",
                    lineasOk, lineasError);
        } catch (IOException e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
    }

    // ─── Mostrar lista ──────────────────────────────────────────────────────────

    public void mostrarEstudiantes() {
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes cargados.");
            return;
        }
        System.out.println("\n--- Lista de Estudiantes ---");
        for (Estudiante e : estudiantes) {
            System.out.println(e);
        }
        System.out.println("Total: " + estudiantes.size() + " estudiante(s).");
    }

    // ─── Serialización ──────────────────────────────────────────────────────────

    public void guardarObjeto(String archivoSalida) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(archivoSalida)))) {
            oos.writeObject(estudiantes);
            System.out.println("Lista serializada en '" + archivoSalida + "'.");
        } catch (IOException e) {
            System.out.println("Error al serializar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarObjeto(String archivoDat) {
        File archivo = new File(archivoDat);
        if (!archivo.exists()) {
            System.out.println("Archivo '" + archivoDat + "' no encontrado.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(archivo)))) {
            estudiantes = (List<Estudiante>) ois.readObject();
            System.out.println("Objeto deserializado: " + estudiantes.size() + " estudiantes cargados.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al deserializar: " + e.getMessage());
        }
    }

    // ─── Exportación a XML (elemento de decisión propia) ───────────────────────

    public void exportarXML(String archivoXML) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<estudiantes>\n");

        for (Estudiante e : estudiantes) {
            sb.append("  <estudiante>\n");
            sb.append("    <nombre>").append(escaparXML(e.getNombre())).append("</nombre>\n");
            sb.append("    <matricula>").append(e.getMatricula()).append("</matricula>\n");
            sb.append("    <carrera>").append(escaparXML(e.getCarrera())).append("</carrera>\n");
            sb.append("    <promedio>").append(e.getPromedio()).append("</promedio>\n");
            sb.append("  </estudiante>\n");
        }

        sb.append("</estudiantes>\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoXML))) {
            writer.write(sb.toString());
            System.out.println("XML exportado a '" + archivoXML + "' (" + estudiantes.size() + " registros).");
        } catch (IOException e) {
            System.out.println("Error al exportar XML: " + e.getMessage());
        }
    }

    // Escapa caracteres especiales para XML válido
    private String escaparXML(String texto) {
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
