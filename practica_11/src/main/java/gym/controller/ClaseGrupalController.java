package gym.controller;

import gym.exception.GymException;
import gym.model.ClaseGrupal;
import gym.model.Cliente;
import gym.service.DataService;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ClaseGrupalController {

    private final DataService ds = DataService.getInstance();

    public void validar(String nombre, String instructor, int capacidad) throws GymException {
        if (nombre == null || nombre.isBlank())
            throw new GymException("El nombre de la clase no puede estar vacío.");
        if (instructor == null || instructor.isBlank())
            throw new GymException("El instructor no puede estar vacío.");
        if (capacidad <= 0)
            throw new GymException("La capacidad debe ser mayor a 0.");
    }

    public ClaseGrupal crear(String nombre, String instructor,
                             DayOfWeek dia, LocalTime hora, int capacidad) throws GymException {
        validar(nombre, instructor, capacidad);
        ClaseGrupal clase = new ClaseGrupal(nombre, instructor, dia, hora, capacidad);
        ds.agregarClase(clase);
        return clase;
    }

    public void actualizar(ClaseGrupal clase, String nombre, String instructor,
                           DayOfWeek dia, LocalTime hora, int capacidad) throws GymException {
        validar(nombre, instructor, capacidad);
        clase.setNombre(nombre);
        clase.setInstructor(instructor);
        clase.setDia(dia);
        clase.setHora(hora);
        clase.setCapacidadMaxima(capacidad);
    }

    public void eliminar(ClaseGrupal clase) {
        ds.eliminarClase(clase);
    }

    public String inscribirCliente(ClaseGrupal clase, Cliente cliente) throws GymException {
        if (cliente == null) throw new GymException("Selecciona un cliente.");
        if (clase.getLugaresDisponibles() == 0)
            throw new GymException("La clase está llena.");
        if (!clase.inscribir(cliente.getId()))
            throw new GymException("El cliente ya está inscrito en esta clase.");
        return cliente.getNombre() + " inscrito en " + clase.getNombre() + ".";
    }
}
