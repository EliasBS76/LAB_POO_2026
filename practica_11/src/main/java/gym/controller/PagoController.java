package gym.controller;

import gym.exception.GymException;
import gym.model.Cliente;
import gym.model.Pago;
import gym.model.Plan;
import gym.service.DataService;

public class PagoController {

    private final DataService ds = DataService.getInstance();

    public Pago crearPago(Cliente cliente, String metodo) throws GymException {
        if (cliente == null)
            throw new GymException("Selecciona un cliente.");
        if (metodo == null || metodo.isBlank())
            throw new GymException("Selecciona un método de pago.");

        var membresiaActiva = cliente.getMembresiaActiva();
        Plan plan;
        double monto;

        if (membresiaActiva != null) {
            plan  = membresiaActiva.getPlan();
            monto = plan.getPrecioFinal();
        } else {
            throw new GymException("El cliente no tiene membresía activa para cobrar.");
        }

        Pago pago = new Pago(cliente, plan, monto, metodo);
        ds.agregarPago(pago);
        return pago;
    }

    public double totalRecaudado() {
        return ds.getPagos().stream()
                .filter(p -> p.getEstado() == gym.model.EstadoPago.COMPLETADO)
                .mapToDouble(Pago::getMonto).sum();
    }
}
