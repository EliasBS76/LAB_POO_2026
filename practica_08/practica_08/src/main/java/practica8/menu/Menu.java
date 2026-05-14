package practica8.menu;

import practica8.collections.DatosPrueba;
import practica8.collections.GestoraProductos;
import practica8.domain.Producto;

import java.util.*;

public class Menu {

    private static final GestoraProductos gestora = new GestoraProductos();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        DatosPrueba.cargar(gestora);
        System.out.println("✔  18 productos cargados.\n");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int op = leerInt("Opción: ");
            System.out.println();
            switch (op) {
                case  1 -> listarTodos();
                case  2 -> buscarPorId();
                case  3 -> agregar();
                case  4 -> actualizar();
                case  5 -> eliminar();
                case  6 -> ordenar();
                case  7 -> filtrar();
                case  8 -> busquedaCompuesta();
                case  9 -> estadisticas();
                case 10 -> gestora.analizarTiempos();
                case 11 -> verHistorial();
                case 12 -> eliminarSinStock();
                case  0 -> salir = true;
                default  -> System.out.println("  Opción no válida.");
            }
            System.out.println();
        }
        System.out.println("¡Hasta luego!");
    }

    /* ------------------------------------------------------------------ */

    static void mostrarMenu() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      GESTORA DE PRODUCTOS — P08      ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Listar todos                     ║");
        System.out.println("║  2. Buscar por ID                    ║");
        System.out.println("║  3. Agregar producto                 ║");
        System.out.println("║  4. Actualizar producto              ║");
        System.out.println("║  5. Eliminar producto                ║");
        System.out.println("║  6. Ordenar (menú secundario)        ║");
        System.out.println("║  7. Filtrar por categoría / nombre   ║");
        System.out.println("║  8. Búsqueda compuesta               ║");
        System.out.println("║  9. Estadísticas (Streams)           ║");
        System.out.println("║ 10. Análisis de tiempos              ║");
        System.out.println("║ 11. Ver historial reciente           ║");
        System.out.println("║ 12. Eliminar productos sin stock     ║");
        System.out.println("║  0. Salir                            ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    /* ---- opciones ---- */

    static void listarTodos() {
        List<Producto> lista = gestora.listarTodos();
        System.out.printf("  Total: %d productos%n", lista.size());
        lista.forEach(p -> System.out.println("  " + p));
    }

    static void buscarPorId() {
        int id = leerInt("  ID a buscar: ");
        gestora.buscarPorId(id).ifPresentOrElse(
            p -> System.out.println("  Encontrado: " + p),
            () -> System.out.println("  No existe el ID " + id)
        );
    }

    static void agregar() {
        System.out.print("  ID       : "); int id  = leerInt("");
        System.out.print("  Nombre   : "); String nom = sc.nextLine().trim();
        System.out.print("  Categoría: "); String cat = sc.nextLine().trim();
        System.out.print("  Precio   : "); double pre = Double.parseDouble(sc.nextLine().trim());
        System.out.print("  Stock    : "); int sto = Integer.parseInt(sc.nextLine().trim());
        System.out.print("  Año      : "); int ano = Integer.parseInt(sc.nextLine().trim());
        boolean ok = gestora.agregar(new Producto(id, nom, cat, pre, sto, ano));
        System.out.println(ok ? "  ✔ Producto agregado." : "  ✘ El ID ya existe.");
    }

    static void actualizar() {
        int id = leerInt("  ID a actualizar: ");
        gestora.buscarPorId(id).ifPresentOrElse(p -> {
            System.out.println("  Actual: " + p);
            System.out.print("  Nuevo nombre   [" + p.getNombre()   + "]: ");
            String nom = sc.nextLine().trim();
            System.out.print("  Nueva categoría[" + p.getCategoria()+ "]: ");
            String cat = sc.nextLine().trim();
            System.out.print("  Nuevo precio   [" + p.getPrecio()   + "]: ");
            double pre = Double.parseDouble(sc.nextLine().trim());
            System.out.print("  Nuevo stock    [" + p.getStock()    + "]: ");
            int sto = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Nuevo año      [" + p.getAnioLanzamiento() + "]: ");
            int ano = Integer.parseInt(sc.nextLine().trim());
            gestora.actualizar(id, nom, cat, pre, sto, ano);
            System.out.println("  ✔ Actualizado.");
        }, () -> System.out.println("  ID no encontrado."));
    }

    static void eliminar() {
        int id = leerInt("  ID a eliminar: ");
        System.out.println(gestora.eliminar(id)
            ? "  ✔ Eliminado."
            : "  ✘ ID no encontrado.");
    }

    static void ordenar() {
        System.out.println("  1. Por precio (Comparable)");
        System.out.println("  2. Por nombre (Comparator)");
        System.out.println("  3. Por stock descendente (Comparator)");
        System.out.println("  4. Por categoría + precio (Comparator compuesto)");
        int op = leerInt("  Criterio: ");
        List<Producto> lista = switch (op) {
            case 1 -> gestora.ordenadosPorPrecio();
            case 2 -> gestora.ordenadosPorNombre();
            case 3 -> gestora.ordenadosPorStockDesc();
            case 4 -> gestora.ordenadosPorCategoriaPrecio();
            default -> List.of();
        };
        lista.forEach(p -> System.out.println("  " + p));
    }

    static void filtrar() {
        System.out.println("  1. Por categoría");
        System.out.println("  2. Por nombre (contiene)");
        int op = leerInt("  Tipo: ");
        if (op == 1) {
            System.out.println("  Categorías: " + gestora.getCategorias());
            System.out.print("  Categoría: "); String cat = sc.nextLine().trim();
            gestora.filtrarPorCategoria(cat).forEach(p -> System.out.println("  " + p));
        } else {
            System.out.print("  Texto: "); String t = sc.nextLine().trim();
            gestora.buscarPorNombre(t).forEach(p -> System.out.println("  " + p));
        }
    }

    static void busquedaCompuesta() {
        System.out.println("  Busca productos en rango de precio CON stock suficiente.");
        System.out.print("  Precio mínimo : "); double pmin = Double.parseDouble(sc.nextLine().trim());
        System.out.print("  Precio máximo : "); double pmax = Double.parseDouble(sc.nextLine().trim());
        System.out.print("  Stock mínimo  : "); int    smin = Integer.parseInt(sc.nextLine().trim());
        List<Producto> res = gestora.buscarPorPrecioYStock(pmin, pmax, smin);
        System.out.printf("  Resultados (%d):%n", res.size());
        res.forEach(p -> System.out.println("  " + p));
    }

    static void estadisticas() {
        System.out.printf("  Total productos  : %d%n", gestora.totalProductos());
        System.out.printf("  Precio promedio  : $%.2f%n", gestora.precioPromedio());
        gestora.productoPlusCaro().ifPresent(
            p -> System.out.println("  Más caro         : " + p));
        System.out.println("  Categorías       : " + gestora.getCategorias());
        System.out.println("\n  --- Por categoría ---");
        gestora.agrupadosPorCategoria().forEach((cat, lista) ->
            System.out.printf("  %-15s : %d productos%n", cat, lista.size()));
    }

    static void verHistorial() {
        System.out.println("  Últimos productos agregados (LinkedList / FIFO):");
        gestora.verHistorial().forEach(p -> System.out.println("  " + p));
    }

    static void eliminarSinStock() {
        int n = gestora.eliminarSinStock();
        System.out.printf("  ✔ %d producto(s) eliminado(s) por stock = 0.%n", n);
    }

    /* ---- helper ---- */
    static int leerInt(String prompt) {
        System.out.print(prompt);
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
