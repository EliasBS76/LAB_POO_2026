package gym.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClaseGrupal implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private String nombre;
    private String instructor;
    private DayOfWeek dia;
    private LocalTime hora;
    private int capacidadMaxima;
    private final List<Integer> clienteIdsInscritos;

    public ClaseGrupal(String nombre, String instructor, DayOfWeek dia, LocalTime hora, int capacidad) {
        this.id                   = nextId++;
        this.nombre               = nombre;
        this.instructor           = instructor;
        this.dia                  = dia;
        this.hora                 = hora;
        this.capacidadMaxima      = capacidad;
        this.clienteIdsInscritos  = new ArrayList<>();
    }

    public boolean inscribir(int clienteId) {
        if (clienteIdsInscritos.size() < capacidadMaxima && !clienteIdsInscritos.contains(clienteId)) {
            clienteIdsInscritos.add(clienteId);
            return true;
        }
        return false;
    }

    public boolean desinscribir(int clienteId) {
        return clienteIdsInscritos.remove(Integer.valueOf(clienteId));
    }

    public int getLugaresDisponibles() { return capacidadMaxima - clienteIdsInscritos.size(); }
    public int getInscritos()          { return clienteIdsInscritos.size(); }

    public int     getId()             { return id; }
    public String  getNombre()         { return nombre; }
    public String  getInstructor()     { return instructor; }
    public DayOfWeek getDia()          { return dia; }
    public LocalTime getHora()         { return hora; }
    public int getCapacidadMaxima()    { return capacidadMaxima; }
    public List<Integer> getClienteIdsInscritos() { return clienteIdsInscritos; }

    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setInstructor(String i)        { this.instructor = i; }
    public void setDia(DayOfWeek dia)          { this.dia = dia; }
    public void setHora(LocalTime hora)        { this.hora = hora; }
    public void setCapacidadMaxima(int cap)    { this.capacidadMaxima = cap; }

    public static void setNextId(int id) { nextId = id; }
    public static int  getNextId()       { return nextId; }

    @Override
    public String toString() {
        return nombre + " — " + instructor + " (" + dia + " " + hora + ")";
    }
}
