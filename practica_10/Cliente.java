public class Cliente {
    private final int id;
    private final double monto;
    private final boolean esDeposito;

    public Cliente(int id, double monto, boolean esDeposito) {
        this.id = id;
        this.monto = monto;
        this.esDeposito = esDeposito;
    }

    public int getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public boolean isEsDeposito() {
        return esDeposito;
    }
}