package gym.controller;

import gym.exception.GymException;
import gym.model.Cliente;
import gym.model.RegistroAcceso;
import gym.model.TipoAcceso;
import gym.service.DataService;

import java.util.List;

public class AccesoController {

    private final DataService ds = DataService.getInstance();

    public RegistroAcceso registrar(Cliente cliente, TipoAcceso tipo) throws GymException {
        if (cliente == null)
            throw new GymException("Selecciona un cliente.");

        // Obtener todos los accesos de hoy para este cliente, ordenados por tiempo
        List<RegistroAcceso> hoy = ds.getAccesos().stream()
                .filter(a -> a.getCliente().getId() == cliente.getId() && a.esDeHoy())
                .sorted((a, b) -> a.getFechaHora().compareTo(b.getFechaHora()))
                .toList();

        if (tipo == TipoAcceso.ENTRADA) {
            if (cliente.getMembresiaActiva() == null)
                throw new GymException(
                    "\"" + cliente.getNombre() + "\" no tiene membresía activa. Acceso denegado.");

            // Si ya está dentro (último acceso es ENTRADA), no puede entrar de nuevo
            if (!hoy.isEmpty() && hoy.get(hoy.size() - 1).getTipo() == TipoAcceso.ENTRADA)
                throw new GymException(
                    "\"" + cliente.getNombre() + "\" ya tiene una entrada activa sin salida registrada.");

        } else { // SALIDA
            // Debe haber entrado hoy
            if (hoy.isEmpty())
                throw new GymException(
                    "\"" + cliente.getNombre() + "\" no tiene registro de entrada hoy. No puede registrar salida.");

            // Su último movimiento del día debe ser ENTRADA (está dentro)
            if (hoy.get(hoy.size() - 1).getTipo() == TipoAcceso.SALIDA)
                throw new GymException(
                    "\"" + cliente.getNombre() + "\" ya registró salida anteriormente. No hay entrada activa.");
        }

        RegistroAcceso reg = new RegistroAcceso(cliente, tipo);
        ds.agregarAcceso(reg);
        return reg;
    }
}
