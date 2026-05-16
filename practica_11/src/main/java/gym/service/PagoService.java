package gym.service;

import gym.model.EstadoPago;
import gym.model.Pago;
import javafx.concurrent.Task;


public class PagoService extends Task<Boolean> {

    private final Pago pago;

    public PagoService(Pago pago) {
        this.pago = pago;
    }

    @Override
    protected Boolean call() throws Exception {
        pago.setEstado(EstadoPago.PROCESANDO);

        step(0, 5, "Verificando datos del cliente…");
        Thread.sleep(600);

        step(1, 5, "Conectando con el procesador de pagos…");
        Thread.sleep(900);

        step(2, 5, "Validando método de pago: " + pago.getMetodo() + "…");
        Thread.sleep(700);

        step(3, 5, "Autorizando transacción de $" + String.format("%.2f", pago.getMonto()) + "…");
        Thread.sleep(800);

        step(4, 5, "Confirmando con el banco…");
        Thread.sleep(500);

        // Simulación: 88 % aprobado, 12 % rechazado
        boolean aprobado = Math.random() > 0.12;
        pago.setEstado(aprobado ? EstadoPago.COMPLETADO : EstadoPago.RECHAZADO);

        updateProgress(5, 5);
        updateMessage(aprobado
                ? "Pago APROBADO — $" + String.format("%.2f", pago.getMonto())
                : "Pago RECHAZADO — Por favor intente con otro método");
        return aprobado;
    }

    private void step(int current, int total, String msg) {
        updateProgress(current, total);
        updateMessage(msg);
    }
}
