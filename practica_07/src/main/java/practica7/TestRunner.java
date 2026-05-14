package practica7;

import practica7.business.Cuenta;
import practica7.business.ServicioBancario;
import practica7.exceptions.*;
import practica7.resources.ConexionBD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;

public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException {
        // Configurar logger a archivo
        Files.createDirectories(Path.of("logs"));
        FileHandler fh = new FileHandler("logs/banco_test.log", false);
        fh.setFormatter(new SimpleFormatter());
        Logger root = Logger.getLogger("");
        root.addHandler(fh);
        root.setLevel(Level.ALL);

        System.out.println("=== Práctica 7 — Test Runner ===\n");

        test1_MontoInvalido();
        test2_CuentaNoEncontrada();
        test3_CuentaBloqueada();
        test4_SaldoInsuficiente();
        test5_RetiroExitoso();
        test6_TryWithResources();
        test7_TransferenciaExitosa();
        test8_Polimorfismo();

        System.out.printf("\n=== Resultado: %d pasaron, %d fallaron ===%n",
            passed, failed);
        fh.close();
    }

    // ---------------------------------------------------------------
    static void test1_MontoInvalido() {
        ServicioBancario s = servicio();
        try {
            s.retirar("001", -100);
            fail("test1 — MontoInvalido");
        } catch (MontoInvalidoException ex) {
            assert "ERR-003".equals(ex.getErrorCode());
            assert "retirar".equals(ex.getMetodoOrigen());
            pass("test1 — MontoInvalidoException lanzada correctamente → " + ex);
        } catch (BancoException ex) {
            fail("test1 — excepción inesperada: " + ex);
        }
    }

    static void test2_CuentaNoEncontrada() {
        ServicioBancario s = servicio();
        try {
            s.retirar("999", 100);
            fail("test2 — CuentaNoEncontrada");
        } catch (CuentaNoEncontradaException ex) {
            pass("test2 — CuentaNoEncontradaException lanzada → " + ex);
        } catch (BancoException ex) {
            fail("test2 — excepción inesperada: " + ex);
        }
    }

    static void test3_CuentaBloqueada() {
        ServicioBancario s = servicio();
        Cuenta bl = new Cuenta("002", 500);
        bl.bloquear("Actividad sospechosa");
        s.registrarCuenta(bl);
        try {
            s.retirar("002", 100);
            fail("test3 — CuentaBloqueada");
        } catch (CuentaBloqueadaException ex) {
            pass("test3 — CuentaBloqueadaException lanzada → " + ex);
        } catch (BancoException ex) {
            fail("test3 — excepción inesperada: " + ex);
        }
    }

    static void test4_SaldoInsuficiente() {
        ServicioBancario s = servicio();
        Cuenta c = new Cuenta("003", 50);
        s.registrarCuenta(c);
        try {
            s.retirar("003", 200);
            fail("test4 — SaldoInsuficiente");
        } catch (SaldoInsuficienteException ex) {
            assert ex.getSaldoActual() == 50.0;
            assert ex.getMontoSolicitado() == 200.0;
            pass("test4 — SaldoInsuficienteException lanzada → " + ex);
        } catch (BancoException ex) {
            fail("test4 — excepción inesperada: " + ex);
        }
    }

    static void test5_RetiroExitoso() {
        ServicioBancario s = servicio();
        try {
            double saldo = s.retirar("001", 300);
            assert saldo == 700.0 : "Saldo esperado 700 pero fue " + saldo;
            pass("test5 — Retiro exitoso, saldo restante: " + saldo);
        } catch (BancoException ex) {
            fail("test5 — excepción inesperada: " + ex);
        }
    }

    static void test6_TryWithResources() {
        ConexionBD[] ref = new ConexionBD[1];
        try (ConexionBD conn = new ConexionBD("jdbc:h2:mem:test_twr")) {
            ref[0] = conn;
            conn.ejecutarOperacion("SELECT 1");
        }
        if (!ref[0].isAbierta()) {
            pass("test6 — ConexionBD cerrada automáticamente (try-with-resources OK)");
        } else {
            fail("test6 — ConexionBD NO se cerró");
        }
    }

    static void test7_TransferenciaExitosa() {
        ServicioBancario s = servicio();
        Cuenta dest = new Cuenta("003", 50);
        s.registrarCuenta(dest);
        try {
            s.transferir("001", "003", 200);
            pass("test7 — Transferencia exitosa");
        } catch (BancoException ex) {
            fail("test7 — excepción inesperada: " + ex);
        }
    }

    static void test8_Polimorfismo() {
        ServicioBancario s = servicio();
        try {
            s.retirar("999", 50);
            fail("test8 — Polimorfismo");
        } catch (BancoException ex) {
            pass("test8 — Capturada como BancoException padre: " + ex.getClass().getSimpleName());
        }
    }

    // ---------------------------------------------------------------
    static ServicioBancario servicio() {
        ServicioBancario s = new ServicioBancario("jdbc:h2:mem:banco");
        s.registrarCuenta(new Cuenta("001", 1000.0));
        return s;
    }

    static void pass(String msg) {
        passed++;
        System.out.println("  ✔  " + msg);
    }

    static void fail(String msg) {
        failed++;
        System.out.println("  ✘  " + msg);
    }
}
