package gym.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private String nombre;
    private String email;
    private String telefono;
    private int puntos;
    private final List<Membresia> membresias;

    public Cliente(String nombre, String email, String telefono) {
        this.id        = nextId++;
        this.nombre    = nombre;
        this.email     = email;
        this.telefono  = telefono;
        this.puntos    = 0;
        this.membresias = new ArrayList<>();
    }

    public int     getId()       { return id; }
    public String  getNombre()   { return nombre; }
    public String  getEmail()    { return email; }
    public String  getTelefono() { return telefono; }
    public int     getPuntos()   { return puntos; }
    public List<Membresia> getMembresias() { return membresias; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setEmail(String email)       { this.email = email; }
    public void setTelefono(String tel)      { this.telefono = tel; }
    public void setPuntos(int puntos)        { this.puntos = puntos; }
    public void agregarPuntos(int puntos)    { this.puntos += puntos; }

    public Membresia getMembresiaActiva() {
        return membresias.stream()
                .filter(m -> m.getEstado() == EstadoMembresia.ACTIVA)
                .findFirst().orElse(null);
    }

    public static void setNextId(int id) { nextId = id; }
    public static int  getNextId()       { return nextId; }

    @Override
    public String toString() { return nombre + " (" + email + ")"; }
}
