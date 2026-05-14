package practica8.collections;

import practica8.comparators.ProductoComparators;
import practica8.domain.Producto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase gestora que administra productos usando cuatro estructuras distintas:
 *
 *  1. HashMap<Integer, Producto>  — acceso O(1) por id (clave única)
 *  2. ArrayList<Producto>         — lista ordenada para mostrar / iterar
 *  3. LinkedList<Producto>        — cola de productos recién agregados (FIFO)
 *  4. HashSet<String>             — categorías únicas sin duplicados
 */
public class GestoraProductos {

    /* ------------------------------------------------------------------ */
    /*  Estructuras de colección                                            */
    /* ------------------------------------------------------------------ */

    /**
     * HashMap: búsqueda por ID en O(1).
     * Si usáramos ArrayList necesitaríamos iterar todos los elementos → O(n).
     */
    private final Map<Integer, Producto> catalogo = new HashMap<>();

    /**
     * ArrayList: acceso por índice O(1), ideal para mostrar listas ordenadas
     * y para Streams. Mejor que LinkedList cuando la mayoría son lecturas.
     */
    private final List<Producto> listaOrdenada = new ArrayList<>();

    /**
     * LinkedList: inserción/eliminación al inicio O(1).
     * Funciona como historial FIFO de los últimos productos agregados.
     */
    private final Deque<Producto> historial = new LinkedList<>();
    private static final int MAX_HISTORIAL = 5;

    /**
     * HashSet: garantiza unicidad en O(1).
     * Mantener categorías en un Set evita duplicados automáticamente;
     * con ArrayList habría que verificar manualmente.
     */
    private final Set<String> categorias = new HashSet<>();

    /* ------------------------------------------------------------------ */
    /*  CRUD                                                                */
    /* ------------------------------------------------------------------ */

    public boolean agregar(Producto p) {
        if (catalogo.containsKey(p.getId())) return false;

        catalogo.put(p.getId(), p);
        listaOrdenada.add(p);
        categorias.add(p.getCategoria());

        historial.addFirst(p);
        if (historial.size() > MAX_HISTORIAL) historial.removeLast();

        return true;
    }

    public Optional<Producto> buscarPorId(int id) {
        return Optional.ofNullable(catalogo.get(id));  // O(1)
    }

    public boolean actualizar(int id, String nombre, String categoria,
                              double precio, int stock, int anio) {
        Producto p = catalogo.get(id);
        if (p == null) return false;

        categorias.add(categoria);
        p.setNombre(nombre);
        p.setCategoria(categoria);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setAnioLanzamiento(anio);
        return true;
    }

    public boolean eliminar(int id) {
        Producto p = catalogo.remove(id);
        if (p == null) return false;
        listaOrdenada.remove(p);
        historial.remove(p);
        recalcularCategorias();
        return true;
    }

    public List<Producto> listarTodos() {
        return Collections.unmodifiableList(listaOrdenada);
    }

    /* ------------------------------------------------------------------ */
    /*  Ordenamiento                                                        */
    /* ------------------------------------------------------------------ */

    public List<Producto> ordenadosPorPrecio() {
        return listaOrdenada.stream()
            .sorted()                                // usa Comparable (precio)
            .collect(Collectors.toList());
    }

    public List<Producto> ordenadosPorNombre() {
        return listaOrdenada.stream()
            .sorted(ProductoComparators.POR_NOMBRE)
            .collect(Collectors.toList());
    }

    public List<Producto> ordenadosPorStockDesc() {
        return listaOrdenada.stream()
            .sorted(ProductoComparators.POR_STOCK_DESC)
            .collect(Collectors.toList());
    }

    public List<Producto> ordenadosPorCategoriaPrecio() {
        return listaOrdenada.stream()
            .sorted(ProductoComparators.POR_CATEGORIA_PRECIO)
            .collect(Collectors.toList());
    }

    /* ------------------------------------------------------------------ */
    /*  Búsqueda y filtrado con Streams                                     */
    /* ------------------------------------------------------------------ */

    /** Busca por nombre (contiene, sin importar mayúsculas). */
    public List<Producto> buscarPorNombre(String texto) {
        String t = texto.toLowerCase();
        return listaOrdenada.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(t))
            .collect(Collectors.toList());
    }

    /** Filtra por categoría exacta. */
    public List<Producto> filtrarPorCategoria(String categoria) {
        return listaOrdenada.stream()
            .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
            .collect(Collectors.toList());
    }

    /**
     * BÚSQUEDA COMPUESTA (Elemento de Decisión Propia):
     * Productos cuyo precio está entre [precioMin, precioMax]
     * Y cuyo stock es mayor que stockMinimo.
     *
     * Relevancia: permite al encargado de compras identificar productos
     * accesibles para el cliente (rango de precio) que además tienen
     * suficiente inventario para cubrir una promoción.
     */
    public List<Producto> buscarPorPrecioYStock(double precioMin,
                                                double precioMax,
                                                int stockMinimo) {
        return listaOrdenada.stream()
            .filter(p -> p.getPrecio() >= precioMin)
            .filter(p -> p.getPrecio() <= precioMax)
            .filter(p -> p.getStock()  >  stockMinimo)
            .sorted()
            .collect(Collectors.toList());
    }

    /** Estadísticas con Stream: precio promedio. */
    public double precioPromedio() {
        return listaOrdenada.stream()
            .mapToDouble(Producto::getPrecio)
            .average()
            .orElse(0);
    }

    /** Producto más caro usando Stream::max. */
    public Optional<Producto> productoPlusCaro() {
        return listaOrdenada.stream().max(Comparator.naturalOrder());
    }

    /** Agrupa productos por categoría usando Collectors.groupingBy. */
    public Map<String, List<Producto>> agrupadosPorCategoria() {
        return listaOrdenada.stream()
            .collect(Collectors.groupingBy(Producto::getCategoria));
    }

    /* ------------------------------------------------------------------ */
    /*  Iteradores                                                          */
    /* ------------------------------------------------------------------ */

    /** Recorre con Iterator explícito y elimina stock = 0. */
    public int eliminarSinStock() {
        int eliminados = 0;
        Iterator<Producto> it = listaOrdenada.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (p.getStock() == 0) {
                it.remove();
                catalogo.remove(p.getId());
                historial.remove(p);
                eliminados++;
            }
        }
        recalcularCategorias();
        return eliminados;
    }

    /** Historial FIFO de los últimos productos agregados (LinkedList). */
    public List<Producto> verHistorial() {
        return new ArrayList<>(historial);
    }

    /** Categorías únicas disponibles (HashSet). */
    public Set<String> getCategorias() {
        return Collections.unmodifiableSet(categorias);
    }

    public int totalProductos() { return catalogo.size(); }

    /* ------------------------------------------------------------------ */
    /*  Análisis de tiempo                                                  */
    /* ------------------------------------------------------------------ */

    public void analizarTiempos() {
        System.out.println("\n=== Análisis de tiempo de ejecución ===");

        // HashMap get O(1)
        long ini = System.nanoTime();
        catalogo.get(1);
        long fin = System.nanoTime();
        System.out.printf("  HashMap.get(id=1)         : %,d ns  → O(1)%n", fin - ini);

        // ArrayList stream + filter
        ini = System.nanoTime();
        listaOrdenada.stream().filter(p -> p.getPrecio() > 100).count();
        fin = System.nanoTime();
        System.out.printf("  Stream filter precio>100  : %,d ns  → O(n)%n", fin - ini);

        // ArrayList sort (Comparable)
        List<Producto> copia = new ArrayList<>(listaOrdenada);
        ini = System.nanoTime();
        Collections.sort(copia);
        fin = System.nanoTime();
        System.out.printf("  Collections.sort %d items  : %,d ns  → O(n log n)%n",
            copia.size(), fin - ini);

        // HashSet contains O(1)
        ini = System.nanoTime();
        categorias.contains("Electrónica");
        fin = System.nanoTime();
        System.out.printf("  HashSet.contains(cat)     : %,d ns  → O(1)%n", fin - ini);

        // Búsqueda compuesta
        ini = System.nanoTime();
        buscarPorPrecioYStock(50, 500, 5);
        fin = System.nanoTime();
        System.out.printf("  Búsqueda compuesta Stream : %,d ns  → O(n)%n", fin - ini);
    }

    /* ------------------------------------------------------------------ */
    /*  Helpers privados                                                    */
    /* ------------------------------------------------------------------ */

    private void recalcularCategorias() {
        categorias.clear();
        listaOrdenada.forEach(p -> categorias.add(p.getCategoria()));
    }
}
