package practica7;

import org.junit.jupiter.api.*;
import practica7.business.Cuenta;
import practica7.business.ServicioBancario;
import practica7.exceptions.*;
import practica7.resources.ConexionBD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Práctica 7
 * Cubre: MontoInvalido, CuentaNoEncontrada, CuentaBloqueada,
 *        SaldoInsuficiente y funcionamiento de ConexionBD (try-with-resources).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServicioBancarioTest {

    private static ServicioBancario servicio;
    private static FileHandler      fileHandler;

    /* ------------------------------------------------------------------ */
    /*  Configuración global: logger a archivo                              */
    /* ------------------------------------------------------------------ */
    @BeforeAll
    static void configurarLogger() throws IOException {
        Files.createDirectories(Path.of("logs"));
        fileHandler = new FileHandler("logs/banco_test.log", false);
        fileHandler.setFormatter(new SimpleFormatter());
        Logger root = Logger.getLogger("");
        root.addHandler(fileHandler);
        root.setLevel(Level.ALL);
    }

    @AfterAll
    static void cerrarLogger() {
        if (fileHandler != null) fileHandler.close();
    }

    @BeforeEach
    void setUp() {
        servicio = new ServicioBancario("jdbc:h2:mem:banco_test");

        Cuenta normal    = new Cuenta("001", 1000.0);
        Cuenta bloqueada = new Cuenta("002", 500.0);
        bloqueada.bloquear("Actividad sospechosa");
        Cuenta sinFondos = new Cuenta("003", 50.0);

        servicio.registrarCuenta(normal);
        servicio.registrarCuenta(bloqueada);
        servicio.registrarCuenta(sinFondos);
    }

    /* ------------------------------------------------------------------ */
    /*  Test 1: MontoInvalidoException                                     */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(1)
    @DisplayName("Retiro con monto negativo lanza MontoInvalidoException")
    void testMontoInvalido() {
        MontoInvalidoException ex = assertThrows(
            MontoInvalidoException.class,
            () -> servicio.retirar("001", -100)
        );
        assertEquals("ERR-003", ex.getErrorCode());
        assertEquals("retirar",  ex.getMetodoOrigen());
        assertEquals(-100.0,     (double) ex.getValorCausante(), 0.001);
        assertNotNull(ex.getTimestamp());
        System.out.println("✔  MontoInvalido → " + ex);
    }

    /* ------------------------------------------------------------------ */
    /*  Test 2: CuentaNoEncontradaException                                */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(2)
    @DisplayName("Retiro de cuenta inexistente lanza CuentaNoEncontradaException")
    void testCuentaNoEncontrada() {
        CuentaNoEncontradaException ex = assertThrows(
            CuentaNoEncontradaException.class,
            () -> servicio.retirar("999", 100)
        );
        assertEquals("ERR-002", ex.getErrorCode());
        assertEquals("999",      ex.getValorCausante());
        System.out.println("✔  CuentaNoEncontrada → " + ex);
    }

    /* ------------------------------------------------------------------ */
    /*  Test 3: CuentaBloqueadaException                                   */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(3)
    @DisplayName("Retiro en cuenta bloqueada lanza CuentaBloqueadaException")
    void testCuentaBloqueada() {
        CuentaBloqueadaException ex = assertThrows(
            CuentaBloqueadaException.class,
            () -> servicio.retirar("002", 100)
        );
        assertEquals("ERR-004", ex.getErrorCode());
        assertTrue(ex.getMessage().contains("bloqueada"));
        System.out.println("✔  CuentaBloqueada → " + ex);
    }

    /* ------------------------------------------------------------------ */
    /*  Test 4: SaldoInsuficienteException                                 */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(4)
    @DisplayName("Retiro mayor al saldo lanza SaldoInsuficienteException")
    void testSaldoInsuficiente() {
        SaldoInsuficienteException ex = assertThrows(
            SaldoInsuficienteException.class,
            () -> servicio.retirar("003", 200)
        );
        assertEquals("ERR-001",  ex.getErrorCode());
        assertEquals(50.0,        ex.getSaldoActual(),     0.001);
        assertEquals(200.0,       ex.getMontoSolicitado(), 0.001);
        System.out.println("✔  SaldoInsuficiente → " + ex);
    }

    /* ------------------------------------------------------------------ */
    /*  Test 5: Retiro exitoso                                             */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(5)
    @DisplayName("Retiro válido actualiza el saldo correctamente")
    void testRetiroExitoso() throws BancoException {
        double saldoRestante = servicio.retirar("001", 300);
        assertEquals(700.0, saldoRestante, 0.001);
        System.out.println("✔  RetiroExitoso → saldo restante: " + saldoRestante);
    }

    /* ------------------------------------------------------------------ */
    /*  Test 6: try-with-resources — ConexionBD se cierra sola             */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(6)
    @DisplayName("ConexionBD se cierra automáticamente con try-with-resources")
    void testConexionBDAutoCierre() {
        ConexionBD[] ref = new ConexionBD[1];
        try (ConexionBD conn = new ConexionBD("jdbc:h2:mem:test_twr")) {
            ref[0] = conn;
            assertTrue(conn.isAbierta());
            conn.ejecutarOperacion("SELECT 1");
        }
        assertFalse(ref[0].isAbierta(),
            "La conexión debe cerrarse automáticamente al salir del bloque");
        System.out.println("✔  try-with-resources → conexión cerrada correctamente");
    }

    /* ------------------------------------------------------------------ */
    /*  Test 7: Transferencia exitosa                                      */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(7)
    @DisplayName("Transferencia válida mueve fondos entre cuentas")
    void testTransferenciaExitosa() throws BancoException {
        assertDoesNotThrow(() -> servicio.transferir("001", "003", 200));
        System.out.println("✔  TransferenciaExitosa completada");
    }

    /* ------------------------------------------------------------------ */
    /*  Test 8: Polimorfismo — captura como BancoException padre           */
    /* ------------------------------------------------------------------ */
    @Test
    @Order(8)
    @DisplayName("Las hijas pueden capturarse como BancoException padre")
    void testPolimorfismo() {
        assertThrows(BancoException.class,
            () -> servicio.retirar("999", 50));
        System.out.println("✔  Polimorfismo → CuentaNoEncontrada capturada como BancoException");
    }
}
