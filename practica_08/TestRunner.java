package practica8;

import practica8.collections.DatosPrueba;
import practica8.collections.GestoraProductos;
import practica8.domain.Producto;

import java.util.List;
import java.util.Map;

public class TestRunner {

    private static int ok  = 0;
    private static int err = 0;

    public static void main(String[] args) {
        System.out.println("=== Práctica 8 — Test Runner ===\n");

        GestoraProductos g = new GestoraProductos();
        DatosPrueba.cargar(g);

        // 1 — Carga de datos
        check("18 productos cargados", g.totalProductos() == 18);

        // 2 — buscarPorId (HashMap O(1))
        check("buscarPorId(1) existe",   g.buscarPorId(1).isPresent());
        check("buscarPorId(99) vacío",   g.buscarPorId(99).isEmpty());

        // 3 — agregar duplicado
        boolean dup = g.agregar(new Producto(1, "Dup", "X", 1, 1, 2020));
        check("no se agrega id duplicado", !dup);

        // 4 — actualizar
        g.actualizar(2, "Mouse Pro", "Electrónica", 30.0, 50, 2023);
        check("actualizar nombre",   g.buscarPorId(2).get().getNombre().equals("Mouse Pro"));
        check("actualizar precio",   g.buscarPorId(2).get().getPrecio() == 30.0);

        // 5 — eliminar
        g.eliminar(5);
        check("eliminar reduce total", g.totalProductos() == 17);
        check("ya no encontrable",     g.buscarPorId(5).isEmpty());

        // 6 — ordenamiento Comparable (por precio)
        List<Producto> porPrecio = g.ordenadosPorPrecio();
        check("ordenados por precio asc",
            porPrecio.get(0).getPrecio() <= porPrecio.get(porPrecio.size()-1).getPrecio());

        // 7 — ordenamiento Comparator nombre
        List<Producto> porNombre = g.ordenadosPorNombre();
        check("ordenados por nombre asc",
            porNombre.get(0).getNombre().compareTo(
            porNombre.get(porNombre.size()-1).getNombre()) <= 0);

        // 8 — filtrar por categoría
        List<Producto> elec = g.filtrarPorCategoria("Electrónica");
        check("filtrar Electrónica > 0", !elec.isEmpty());
        check("todos son Electrónica",
            elec.stream().allMatch(p -> p.getCategoria().equals("Electrónica")));

        // 9 — buscarPorNombre (Stream)
        check("buscar 'laptop' encuentra algo",
            !g.buscarPorNombre("laptop").isEmpty());

        // 10 — búsqueda compuesta
        List<Producto> comp = g.buscarPorPrecioYStock(20, 100, 10);
        check("búsqueda compuesta precio 20-100 stock>10",
            comp.stream().allMatch(p ->
                p.getPrecio() >= 20 && p.getPrecio() <= 100 && p.getStock() > 10));

        // 11 — precio promedio
        check("precio promedio > 0", g.precioPromedio() > 0);

        // 12 — producto más caro
        check("producto más caro presente", g.productoPlusCaro().isPresent());

        // 13 — categorías HashSet
        check("categorías no vacías", !g.getCategorias().isEmpty());

        // 14 — historial LinkedList
        check("historial no vacío", !g.verHistorial().isEmpty());

        // 15 — agrupados por categoría
        Map<String, List<Producto>> grupos = g.agrupadosPorCategoria();
        check("agrupa por categoría", grupos.containsKey("Electrónica"));

        // 16 — eliminar sin stock (Iterator)
        int eliminados = g.eliminarSinStock();
        check("elimina con stock=0 (cable HDMI)", eliminados >= 1);
        check("cable HDMI ya no está", g.buscarPorId(16).isEmpty());

        // 17 — tiempos de ejecución
        System.out.println();
        g.analizarTiempos();

        System.out.printf("%n=== Resultado: %d pasaron, %d fallaron ===%n", ok, err);
    }

    static void check(String desc, boolean cond) {
        if (cond) { ok++;  System.out.println("  ✔  " + desc); }
        else      { err++; System.out.println("  ✘  " + desc); }
    }
}
