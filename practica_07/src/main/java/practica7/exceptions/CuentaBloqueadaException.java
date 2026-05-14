package practica7.exceptions;

/**
 * Se lanza cuando se intenta operar sobre una cuenta bloqueada.
 */
public class CuentaBloqueadaException extends BancoException {

    public CuentaBloqueadaException(String numeroCuenta,
                                    String motivo,
                                    String metodoOrigen) {
        super(
            String.format(
                "La cuenta %s está bloqueada. Motivo: %s",
                numeroCuenta, motivo
            ),
            "ERR-004",
            metodoOrigen,
            numeroCuenta
        );
    }
}
