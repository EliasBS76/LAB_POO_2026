package practica7.business;

import practica7.exceptions.*;
import practica7.resources.ConexionBD;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

/**
 * Servicio bancario principal.
 * - Lanza y maneja todas las excepciones personalizadas.
 * - Registra errores mediante java.util.logging.
 * - Usa try-with-resources para la conexión a BD.
 */
public class ServicioBancario {

    private static final Logger log =
        Logger.getLogger(ServicioBancario.class.getName());

    private final Map<String, Cuenta> cuentas = new HashMap<>();
    private final String              urlBD;

    /* ------------------------------------------------------------------ */
    /*  Constructor                                                         */
    /* ------------------------------------------------------------------ */
    public ServicioBancario(String urlBD) {
        this.urlBD = urlBD;
    }

    /* ------------------------------------------------------------------ */
    /*  Operaciones públicas                                                */
    /* ------------------------------------------------------------------ */

    /** Registra una nueva cuenta. */
    public void registrarCuenta(Cuenta cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        log.info("Cuenta registrada: " + cuenta.getNumeroCuenta());
    }

    /**
     * Realiza un retiro de la cuenta indicada.
     * Usa try-with-resources para la conexión a BD.
     *
     * @throws MontoInvalidoException       si monto <= 0
     * @throws CuentaNoEncontradaException  si la cuenta no existe
     * @throws CuentaBloqueadaException     si la cuenta está bloqueada
     * @throws SaldoInsuficienteException   si el saldo no alcanza
     */
    public double retirar(String numeroCuenta, double monto)
        throws BancoException {

        // Validación de monto (checked: el compilador obliga a manejarla)
        if (monto <= 0) {
            MontoInvalidoException ex =
                new MontoInvalidoException(monto, "retirar");
            log.warning(ex.toString());
            throw ex;
        }

        try (ConexionBD conn = new ConexionBD(urlBD)) {

            Cuenta cuenta = buscarCuenta(numeroCuenta, "retirar");

            if (cuenta.isBloqueada()) {
                CuentaBloqueadaException ex = new CuentaBloqueadaException(
                    numeroCuenta, cuenta.getMotivoBloq(), "retirar");
                log.warning(ex.toString());
                throw ex;
            }

            if (cuenta.getSaldo() < monto) {
                SaldoInsuficienteException ex = new SaldoInsuficienteException(
                    cuenta.getSaldo(), monto, "retirar");
                log.warning(ex.toString());
                throw ex;
            }

            conn.ejecutarOperacion("UPDATE cuentas SET saldo = saldo - " + monto
                + " WHERE numero = '" + numeroCuenta + "'");

            cuenta.setSaldo(cuenta.getSaldo() - monto);
            log.info(String.format("Retiro exitoso: cuenta=%s monto=%.2f saldo=%.2f",
                numeroCuenta, monto, cuenta.getSaldo()));

            return cuenta.getSaldo();
        }
        // La conexión se cierra automáticamente al salir del bloque (éxito o excepción)
    }

    /**
     * Realiza una transferencia entre dos cuentas.
     *
     * @throws BancoException en cualquier condición de error
     */
    public void transferir(String origen, String destino, double monto)
        throws BancoException {

        if (monto <= 0) {
            MontoInvalidoException ex =
                new MontoInvalidoException(monto, "transferir");
            log.warning(ex.toString());
            throw ex;
        }

        try (ConexionBD conn = new ConexionBD(urlBD)) {

            Cuenta cOrigen  = buscarCuenta(origen,  "transferir");
            Cuenta cDestino = buscarCuenta(destino, "transferir");

            if (cOrigen.isBloqueada()) {
                CuentaBloqueadaException ex = new CuentaBloqueadaException(
                    origen, cOrigen.getMotivoBloq(), "transferir");
                log.warning(ex.toString());
                throw ex;
            }

            if (cOrigen.getSaldo() < monto) {
                SaldoInsuficienteException ex = new SaldoInsuficienteException(
                    cOrigen.getSaldo(), monto, "transferir");
                log.warning(ex.toString());
                throw ex;
            }

            conn.ejecutarOperacion("BEGIN TRANSACTION");
            cOrigen.setSaldo(cOrigen.getSaldo() - monto);
            cDestino.setSaldo(cDestino.getSaldo() + monto);
            conn.ejecutarOperacion("COMMIT");

            log.info(String.format(
                "Transferencia exitosa: %s -> %s | monto=%.2f",
                origen, destino, monto));
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Helpers privados                                                    */
    /* ------------------------------------------------------------------ */

    private Cuenta buscarCuenta(String numeroCuenta, String metodo)
        throws CuentaNoEncontradaException {

        Cuenta c = cuentas.get(numeroCuenta);
        if (c == null) {
            CuentaNoEncontradaException ex =
                new CuentaNoEncontradaException(numeroCuenta, metodo);
            log.warning(ex.toString());
            throw ex;
        }
        return c;
    }
}
