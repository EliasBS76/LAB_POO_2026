package practica7.exceptions;

/**
 * Se lanza cuando no existe una cuenta con el número proporcionado.
 */
public class CuentaNoEncontradaException extends BancoException {

    public CuentaNoEncontradaException(String numeroCuenta,
                                       String metodoOrigen) {
        super(
            "No existe ninguna cuenta con número: " + numeroCuenta,
            "ERR-002",
            metodoOrigen,
            numeroCuenta
        );
    }
}
