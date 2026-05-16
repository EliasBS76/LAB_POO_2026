package gym.model;

import java.io.Serializable;

public class Recompensa implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private String descripcion;
    private int puntosNecesarios;
    private boolean disponible;

    public Recompensa(String descripcion, int puntosNecesarios) {
        this.id               = nextId++;
        this.descripcion      = descripcion;
        this.puntosNecesarios = puntosNecesarios;
        this.disponible       = true;
    }

    public int     getId()               { return id; }
    public String  getDescripcion()      { return descripcion; }
    public int     getPuntosNecesarios() { return puntosNecesarios; }
    public boolean isDisponible()        { return disponible; }

    public void setDescripcion(String descripcion)       { this.descripcion = descripcion; }
    public void setPuntosNecesarios(int puntos)          { this.puntosNecesarios = puntos; }
    public void setDisponible(boolean disponible)        { this.disponible = disponible; }

    public static void setNextId(int id) { nextId = id; }

    @Override
    public String toString() { return descripcion + " (" + puntosNecesarios + " pts)"; }
}
