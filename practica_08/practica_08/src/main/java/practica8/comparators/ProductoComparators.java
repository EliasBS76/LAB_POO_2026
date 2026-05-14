package practica8.comparators;

import practica8.domain.Producto;
import java.util.Comparator;

/**
 * Comparadores personalizados para Producto.
 */
public class ProductoComparators {

    /** Criterio 1: por nombre alfabético */
    public static final Comparator<Producto> POR_NOMBRE =
        Comparator.comparing(Producto::getNombre);

    /** Criterio 2: por stock descendente (más disponible primero) */
    public static final Comparator<Producto> POR_STOCK_DESC =
        Comparator.comparingInt(Producto::getStock).reversed();

    /** Criterio compuesto: categoría, luego precio ascendente */
    public static final Comparator<Producto> POR_CATEGORIA_PRECIO =
        Comparator.comparing(Producto::getCategoria)
                  .thenComparing(Producto::getPrecio);

    private ProductoComparators() {}
}
