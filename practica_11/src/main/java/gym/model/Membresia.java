package gym.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Membresia implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private Cliente cliente;
    private Plan plan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean renovacionAutomatica;
    private EstadoMembresia estado;

    public Membresia(Cliente cliente, Plan plan, boolean renovacionAutomatica) {
        this.id                  = nextId++;
        this.cliente             = cliente;
        this.plan                = plan;
        this.fechaInicio         = LocalDate.now();
        this.fechaFin            = LocalDate.now().plusMonths(1);
        this.renovacionAutomatica = renovacionAutomatica;
        this.estado              = EstadoMembresia.ACTIVA;
    }

    public void renovar() {
        this.fechaInicio = LocalDate.now();
        this.fechaFin    = LocalDate.now().plusMonths(1);
        this.estado      = EstadoMembresia.ACTIVA;
    }

    public boolean estaVencida() {
        return LocalDate.now().isAfter(fechaFin);
    }

    public long diasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
    }

    public void actualizarEstado() {
        if (estaVencida() && estado == EstadoMembresia.ACTIVA) {
            estado = EstadoMembresia.VENCIDA;
        }
    }

    public int     getId()                    { return id; }
    public Cliente getCliente()               { return cliente; }
    public Plan    getPlan()                  { return plan; }
    public LocalDate getFechaInicio()         { return fechaInicio; }
    public LocalDate getFechaFin()            { return fechaFin; }
    public boolean isRenovacionAutomatica()   { return renovacionAutomatica; }
    public EstadoMembresia getEstado()        { return estado; }

    public void setPlan(Plan plan)                              { this.plan = plan; }
    public void setRenovacionAutomatica(boolean val)            { this.renovacionAutomatica = val; }
    public void setEstado(EstadoMembresia estado)               { this.estado = estado; }

    public static void setNextId(int id) { nextId = id; }
    public static int  getNextId()       { return nextId; }

    @Override
    public String toString() {
        return "Membresía #" + id + " [" + plan.getNombre() + " - " + estado + "]";
    }
}
