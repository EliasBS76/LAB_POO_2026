package practica7.exceptions;

/**
 * Se lanza cuando el saldo disponible es menor al monto solicitado.
 */
public class SaldoInsuficienteException extends BancoException {

    private final double saldoActual;
    private final double montoSolicitado;

    public SaldoInsuficienteException(double saldoActual,
                                      double montoSolicitado,
                                      String metodoOrigen) {
        super(
            String.format(
                "Saldo insuficiente: tiene $%.2f pero solicitó $%.2f",
                saldoActual, montoSolicitado
            ),
            "ERR-001",
            metodoOrigen,
            montoSolicitado
        );
        this.saldoActual     = saldoActual;
        this.montoSolicitado = montoSolicitado;
    }

    public double getSaldoActual()     { return saldoActual;     }
    public double getMontoSolicitado() { return montoSolicitado; }
}
