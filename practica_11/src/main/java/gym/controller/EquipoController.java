package gym.controller;

import gym.exception.GymException;
import gym.model.Equipo;
import gym.model.EstadoEquipo;
import gym.service.DataService;

import java.time.LocalDate;

public class EquipoController {

    private final DataService ds = DataService.getInstance();

    public void validar(String nombre, String tipo, int cantidad) throws GymException {
        if (nombre == null || nombre.isBlank())
            throw new GymException("El nombre del equipo no puede estar vacío.");
        if (tipo == null || tipo.isBlank())
            throw new GymException("El tipo de equipo no puede estar vacío.");
        if (cantidad <= 0)
            throw new GymException("La cantidad debe ser mayor a 0.");
    }

    public Equipo crear(String nombre, String tipo, int cantidad,
                        EstadoEquipo estado) throws GymException {
        validar(nombre, tipo, cantidad);
        Equipo e = new Equipo(nombre, tipo, cantidad);
        e.setEstado(estado);
        ds.agregarEquipo(e);
        return e;
    }

    public void actualizar(Equipo equipo, String nombre, String tipo,
                           int cantidad, EstadoEquipo estado) throws GymException {
        validar(nombre, tipo, cantidad);
        equipo.setNombre(nombre);
        equipo.setTipo(tipo);
        equipo.setCantidad(cantidad);
        equipo.setEstado(estado);
    }

    public void registrarMantenimiento(Equipo equipo) {
        equipo.setUltimoMantenimiento(LocalDate.now());
        equipo.setEstado(EstadoEquipo.EN_MANTENIMIENTO);
    }

    public void marcarDisponible(Equipo equipo) {
        equipo.setEstado(EstadoEquipo.DISPONIBLE);
    }

    public void eliminar(Equipo equipo) {
        ds.eliminarEquipo(equipo);
    }
}
