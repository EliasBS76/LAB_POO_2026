package practica7.resources;

import java.util.logging.Logger;

/**
 * Simula una conexión a base de datos.
 * Implementa AutoCloseable para usarse con try-with-resources.
 */
public class ConexionBD implements AutoCloseable {

    private static final Logger log = Logger.getLogger(ConexionBD.class.getName());

    private final String url;
    private       boolean abierta;

    public ConexionBD(String url) {
        this.url    = url;
        this.abierta = true;
        log.info("Conexión abierta -> " + url);
    }

    public boolean isAbierta() { return abierta; }

    public void ejecutarOperacion(String operacion) {
        if (!abierta) throw new IllegalStateException("Conexión ya cerrada");
        log.info("Ejecutando en BD [" + url + "]: " + operacion);
    }

    @Override
    public void close() {
        if (abierta) {
            abierta = false;
            log.info("Conexión cerrada -> " + url);
        }
    }
}
