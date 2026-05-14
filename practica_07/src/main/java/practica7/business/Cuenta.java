package practica7.business;

/**
 * Modelo simple de una cuenta bancaria.
 */
public class Cuenta {

    private final String numeroCuenta;
    private       double saldo;
    private       boolean bloqueada;
    private       String motivoBloqueo;

    public Cuenta(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo        = saldoInicial;
        this.bloqueada    = false;
    }

    public String  getNumeroCuenta() { return numeroCuenta; }
    public double  getSaldo()        { return saldo;        }
    public boolean isBloqueada()     { return bloqueada;    }
    public String  getMotivoBloq()   { return motivoBloqueo; }

    public void setSaldo(double saldo)       { this.saldo = saldo; }
    public void bloquear(String motivo) {
        this.bloqueada     = true;
        this.motivoBloqueo = motivo;
    }
}
