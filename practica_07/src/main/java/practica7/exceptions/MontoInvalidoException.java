package practica7.exceptions;

/**
 * Se lanza cuando el monto de una operación es inválido (negativo o cero).
 */
public class MontoInvalidoException extends BancoException {

    public MontoInvalidoException(double monto, String metodoOrigen) {
        super(
            String.format(
                "El monto $%.2f no es válido; debe ser mayor a cero", monto
            ),
            "ERR-003",
            metodoOrigen,
            monto
        );
    }
}
