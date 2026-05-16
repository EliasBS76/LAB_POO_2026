package gym.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private String nombre;
    private String tipo;
    private int cantidad;
    private EstadoEquipo estado;
    private LocalDate ultimoMantenimiento;

    public Equipo(String nombre, String tipo, int cantidad) {
        this.id                  = nextId++;
        this.nombre              = nombre;
        this.tipo                = tipo;
        this.cantidad            = cantidad;
        this.estado              = EstadoEquipo.DISPONIBLE;
        this.ultimoMantenimiento = LocalDate.now();
    }

    public int         getId()                   { return id; }
    public String      getNombre()               { return nombre; }
    public String      getTipo()                 { return tipo; }
    public int         getCantidad()             { return cantidad; }
    public EstadoEquipo getEstado()              { return estado; }
    public LocalDate   getUltimoMantenimiento()  { return ultimoMantenimiento; }

    public void setNombre(String nombre)                  { this.nombre = nombre; }
    public void setTipo(String tipo)                      { this.tipo = tipo; }
    public void setCantidad(int cantidad)                 { this.cantidad = cantidad; }
    public void setEstado(EstadoEquipo estado)            { this.estado = estado; }
    public void setUltimoMantenimiento(LocalDate fecha)   { this.ultimoMantenimiento = fecha; }

    public static void setNextId(int id) { nextId = id; }
    public static int  getNextId()       { return nextId; }

    @Override
    public String toString() { return nombre + " (" + tipo + ") x" + cantidad; }
}
