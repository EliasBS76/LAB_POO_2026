package practica8.domain;

import java.util.Objects;

/**
 * Clase de dominio: Producto de una tienda.
 * Implementa Comparable por precio (orden natural).
 */
public class Producto implements Comparable<Producto> {

    private final int    id;
    private       String nombre;
    private       String categoria;
    private       double precio;
    private       int    stock;
    private       int    anioLanzamiento;

    public Producto(int id, String nombre, String categoria,
                    double precio, int stock, int anioLanzamiento) {
        this.id              = id;
        this.nombre          = nombre;
        this.categoria       = categoria;
        this.precio          = precio;
        this.stock           = stock;
        this.anioLanzamiento = anioLanzamiento;
    }

    /* ---- Comparable: orden natural por precio ---- */
    @Override
    public int compareTo(Producto otro) {
        return Double.compare(this.precio, otro.precio);
    }

    /* ---- equals / hashCode por id ---- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto p)) return false;
        return id == p.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format(
            "Producto{id=%d, nombre='%s', cat='%s', precio=$%.2f, stock=%d, año=%d}",
            id, nombre, categoria, precio, stock, anioLanzamiento);
    }

    /* ---- Getters y Setters ---- */
    public int    getId()              { return id; }
    public String getNombre()          { return nombre; }
    public String getCategoria()       { return categoria; }
    public double getPrecio()          { return precio; }
    public int    getStock()           { return stock; }
    public int    getAnioLanzamiento() { return anioLanzamiento; }

    public void setNombre(String nombre)                   { this.nombre = nombre; }
    public void setCategoria(String categoria)             { this.categoria = categoria; }
    public void setPrecio(double precio)                   { this.precio = precio; }
    public void setStock(int stock)                        { this.stock = stock; }
    public void setAnioLanzamiento(int anio)               { this.anioLanzamiento = anio; }
}
