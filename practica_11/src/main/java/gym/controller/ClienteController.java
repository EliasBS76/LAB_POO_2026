package gym.controller;

import gym.exception.ClienteException;
import gym.exception.MembresiaException;
import gym.model.Cliente;
import gym.model.Membresia;
import gym.model.Plan;
import gym.service.DataService;

public class ClienteController {

    private final DataService ds = DataService.getInstance();

    public void validar(String nombre, String email, String telefono) throws ClienteException {
        if (nombre == null || nombre.isBlank() || nombre.length() < 3)
            throw new ClienteException("El nombre debe tener al menos 3 caracteres.");
        if (email == null || !email.matches("[^@]+@[^@]+\\.[^@]+"))
            throw new ClienteException("El correo electrónico no es válido.");
        if (telefono == null || !telefono.matches("[0-9\\-+() ]{7,15}"))
            throw new ClienteException("El teléfono debe tener entre 7 y 15 dígitos.");
    }

    public Cliente crear(String nombre, String email, String telefono,
                         Plan plan, boolean renovacionAuto) throws ClienteException {
        validar(nombre, email, telefono);
        Cliente cliente = new Cliente(nombre, email, telefono);
        if (plan != null) {
            Membresia m = new Membresia(cliente, plan, renovacionAuto);
            cliente.getMembresias().add(m);
            cliente.agregarPuntos(plan.getPuntosBonus());
        }
        ds.agregarCliente(cliente);
        return cliente;
    }

    public void actualizar(Cliente cliente, String nombre, String email, String telefono,
                           Plan plan, boolean renovacionAuto) throws ClienteException {
        validar(nombre, email, telefono);
        cliente.setNombre(nombre);
        cliente.setEmail(email);
        cliente.setTelefono(telefono);
        if (plan != null) {
            Membresia activa = cliente.getMembresiaActiva();
            if (activa != null) {
                activa.setPlan(plan);
                activa.setRenovacionAutomatica(renovacionAuto);
            } else {
                Membresia nueva = new Membresia(cliente, plan, renovacionAuto);
                cliente.getMembresias().add(nueva);
                cliente.agregarPuntos(plan.getPuntosBonus());
            }
        }
    }

    public void eliminar(Cliente cliente) {
        ds.eliminarCliente(cliente);
    }

    public void renovarMembresia(Cliente cliente) throws MembresiaException {
        Membresia m = cliente.getMembresiaActiva();
        if (m == null)
            m = cliente.getMembresias().isEmpty() ? null
                : cliente.getMembresias().get(cliente.getMembresias().size() - 1);
        if (m == null)
            throw new MembresiaException("El cliente no tiene ninguna membresía registrada.");
        m.renovar();
        cliente.agregarPuntos(m.getPlan().getPuntosBonus());
    }
}
