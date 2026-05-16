package gym.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegistroAcceso implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private final int id;
    private final Cliente cliente;
    private final TipoAcceso tipo;
    private final LocalDateTime fechaHora;

    public RegistroAcceso(Cliente cliente, TipoAcceso tipo) {
        this.id        = nextId++;
        this.cliente   = cliente;
        this.tipo      = tipo;
        this.fechaHora = LocalDateTime.now();
    }

    public int           getId()       { return id; }
    public Cliente       getCliente()  { return cliente; }
    public TipoAcceso    getTipo()     { return tipo; }
    public LocalDateTime getFechaHora(){ return fechaHora; }

    public boolean esDeHoy() {
        return fechaHora.toLocalDate().equals(LocalDate.now());
    }

    public static void setNextId(int id) { nextId = id; }
    public static int  getNextId()       { return nextId; }
}
