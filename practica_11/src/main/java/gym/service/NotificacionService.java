package gym.service;

import gym.model.Cliente;
import gym.model.EstadoMembresia;
import gym.model.Membresia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificacionService {

    private static NotificacionService instance;
    private final List<String> notificaciones = new ArrayList<>();

    private NotificacionService() {}

    public static NotificacionService getInstance() {
        if (instance == null) instance = new NotificacionService();
        return instance;
    }

    public void verificarVencimientos() {
        notificaciones.clear();
        for (Cliente cliente : DataService.getInstance().getClientes()) {
            for (Membresia m : cliente.getMembresias()) {
                m.actualizarEstado();

                if (m.getEstado() == EstadoMembresia.ACTIVA) {
                    long dias = m.diasRestantes();
                    if (dias <= 7) {
                        if (m.isRenovacionAutomatica()) {
                            m.renovar();
                            cliente.agregarPuntos(m.getPlan().getPuntosBonus());
                            notificaciones.add("[RENOVADA] Membresía de " + cliente.getNombre()
                                    + " renovada automáticamente.");
                        } else {
                            notificaciones.add("[AVISO] La membresía de " + cliente.getNombre()
                                    + " vence en " + dias + " día(s).");
                        }
                    }
                } else if (m.getEstado() == EstadoMembresia.VENCIDA) {
                    notificaciones.add("[VENCIDA] La membresía de " + cliente.getNombre()
                            + " venció el " + m.getFechaFin() + ".");
                }
            }
        }
    }

    public List<String> getNotificaciones()     { return notificaciones; }
    public int          getCantidad()           { return notificaciones.size(); }
}
