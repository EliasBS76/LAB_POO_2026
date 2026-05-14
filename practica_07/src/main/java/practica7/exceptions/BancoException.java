package practica7.exceptions;

import java.time.LocalDateTime;

/**
 * Excepción base del sistema bancario.
 * Información de contexto adicional:
 *  - errorCode   : código interno para búsqueda rápida en logs
 *  - timestamp   : momento exacto en que ocurrió el error
 *  - metodOrigen : nombre del método donde se lanzó
 *  - valorCausante: valor concreto que disparó el problema
 */
public class BancoException extends Exception {

    private final String        errorCode;
    private final LocalDateTime timestamp;
    private final String        metodoOrigen;
    private final Object        valorCausante;

    public BancoException(String mensaje,
                          String errorCode,
                          String metodoOrigen,
                          Object valorCausante) {
        super(mensaje);
        this.errorCode     = errorCode;
        this.timestamp     = LocalDateTime.now();
        this.metodoOrigen  = metodoOrigen;
        this.valorCausante = valorCausante;
    }

    public BancoException(String mensaje,
                          String errorCode,
                          String metodoOrigen,
                          Object valorCausante,
                          Throwable causa) {
        super(mensaje, causa);
        this.errorCode     = errorCode;
        this.timestamp     = LocalDateTime.now();
        this.metodoOrigen  = metodoOrigen;
        this.valorCausante = valorCausante;
    }

    public String        getErrorCode()     { return errorCode;     }
    public LocalDateTime getTimestamp()     { return timestamp;     }
    public String        getMetodoOrigen()  { return metodoOrigen;  }
    public Object        getValorCausante() { return valorCausante; }

    @Override
    public String toString() {
        return String.format(
            "[%s] %s | código=%s | origen=%s | valor=%s | %s",
            timestamp, getClass().getSimpleName(),
            errorCode, metodoOrigen, valorCausante, getMessage()
        );
    }
}
