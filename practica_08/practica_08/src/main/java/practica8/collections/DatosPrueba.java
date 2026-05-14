package practica8.collections;

import practica8.domain.Producto;

/**
 * Datos de prueba — 18 productos.
 */
public class DatosPrueba {

    public static void cargar(GestoraProductos g) {
        Producto[] productos = {
            new Producto( 1, "Laptop Pro 15",        "Electrónica",  1299.99,  8, 2023),
            new Producto( 2, "Mouse Inalámbrico",    "Electrónica",    25.50, 45, 2022),
            new Producto( 3, "Teclado Mecánico",     "Electrónica",    89.00, 20, 2021),
            new Producto( 4, "Monitor 27\"",         "Electrónica",   349.00, 12, 2023),
            new Producto( 5, "Audífonos Bluetooth",  "Electrónica",    59.99, 30, 2022),
            new Producto( 6, "Silla Ergonómica",     "Oficina",       299.00,  6, 2022),
            new Producto( 7, "Escritorio Standing",  "Oficina",       450.00,  3, 2023),
            new Producto( 8, "Lámpara LED",          "Oficina",        35.00, 50, 2021),
            new Producto( 9, "Cuaderno A4",          "Papelería",       4.50, 200, 2023),
            new Producto(10, "Plumas (caja 12)",     "Papelería",       8.00, 150, 2022),
            new Producto(11, "Mochila Laptop",       "Accesorios",     55.00, 25, 2023),
            new Producto(12, "Hub USB-C 7en1",       "Electrónica",    45.00, 18, 2022),
            new Producto(13, "Webcam 1080p",         "Electrónica",    79.99, 14, 2021),
            new Producto(14, "Alfombrilla XL",       "Accesorios",     22.00, 40, 2022),
            new Producto(15, "Soporte Monitor",      "Accesorios",     65.00, 10, 2023),
            new Producto(16, "Cable HDMI 2m",        "Accesorios",      9.99,  0, 2021),  // sin stock
            new Producto(17, "Archivero 3 cajones",  "Oficina",       185.00,  5, 2020),
            new Producto(18, "Tablet 10\"",          "Electrónica",   399.00,  7, 2023),
        };
        for (Producto p : productos) g.agregar(p);
    }

    private DatosPrueba() {}
}
