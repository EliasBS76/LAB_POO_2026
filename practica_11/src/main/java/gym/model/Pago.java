package gym.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private final Cliente cliente;
    private final Plan plan;
    private final double monto;
    private final LocalDateTime fecha;
    private final String metodo;   // TARJETA, EFECTIVO, TRANSFERENCIA
    private EstadoPago estado;

    public Pago(Cliente cliente, Plan plan, double monto, String metodo) {
        this.id      = nextId++;
        this.cliente = cliente;
        this.plan    = plan;
        this.monto   = monto;
        this.metodo  = metodo;
        this.fecha   = LocalDateTime.now();
        this.estado  = EstadoPago.PENDIENTE;
    }

    public int         getId()      { return id; }
    public Cliente     getCliente() { return cliente; }
    public Plan        getPlan()    { return plan; }
    public double      getMonto()   { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public String      getMetodo()  { return metodo; }
    public EstadoPago  getEstado()  { return estado; }

    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public static void setNextId(int id) { nextId = id; }
    public static int  getNextId()       { return nextId; }
}
