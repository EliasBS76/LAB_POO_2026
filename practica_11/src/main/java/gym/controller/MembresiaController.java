package gym.controller;

import gym.exception.MembresiaException;
import gym.model.Cliente;
import gym.model.EstadoMembresia;
import gym.model.Membresia;
import gym.model.Plan;
import gym.service.DataService;

import java.util.ArrayList;
import java.util.List;

public class MembresiaController {

    private final DataService ds = DataService.getInstance();

    public List<Membresia> todasLasMembresias() {
        List<Membresia> todas = new ArrayList<>();
        for (Cliente c : ds.getClientes())
            todas.addAll(c.getMembresias());
        return todas;
    }

    public void cambiarPlan(Membresia m, Plan nuevoPlan) throws MembresiaException {
        if (nuevoPlan == null) throw new MembresiaException("Debe seleccionar un plan.");
        m.setPlan(nuevoPlan);
    }

    public void suspender(Membresia m) throws MembresiaException {
        if (m.getEstado() == EstadoMembresia.SUSPENDIDA)
            throw new MembresiaException("La membresía ya está suspendida.");
        m.setEstado(EstadoMembresia.SUSPENDIDA);
    }

    public void renovar(Membresia m) {
        m.renovar();
        m.getCliente().agregarPuntos(m.getPlan().getPuntosBonus());
    }

    public void toggleRenovacion(Membresia m) {
        m.setRenovacionAutomatica(!m.isRenovacionAutomatica());
    }
}
