package gym.controller;

import gym.exception.GymException;
import gym.model.Cliente;
import gym.model.RegistroAcceso;
import gym.model.TipoAcceso;
import gym.service.DataService;

public class AccesoController {

    private final DataService ds = DataService.getInstance();

    public RegistroAcceso registrar(Cliente cliente, TipoAcceso tipo) throws GymException {
        if (cliente == null)
            throw new GymException("Selecciona un cliente.");

        if (tipo == TipoAcceso.ENTRADA && cliente.getMembresiaActiva() == null)
            throw new GymException(
                "\"" + cliente.getNombre() + "\" no tiene membresía activa. Acceso denegado.");

        RegistroAcceso reg = new RegistroAcceso(cliente, tipo);
        ds.agregarAcceso(reg);
        return reg;
    }
}
