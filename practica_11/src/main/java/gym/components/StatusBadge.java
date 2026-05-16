package gym.components;

import gym.model.EstadoEquipo;
import gym.model.EstadoMembresia;
import javafx.scene.control.Label;

/**
 * Componente personalizado: Label estilizada que muestra el estado de una
 * membresía o equipo con color de fondo semántico definido en el CSS externo.
 */
public class StatusBadge extends Label {

    public StatusBadge() {
        getStyleClass().add("status-badge");
    }

    public void setEstado(EstadoMembresia estado) {
        getStyleClass().removeAll("badge-activa", "badge-vencida", "badge-suspendida");
        switch (estado) {
            case ACTIVA     -> { setText("ACTIVA");      getStyleClass().add("badge-activa"); }
            case VENCIDA    -> { setText("VENCIDA");     getStyleClass().add("badge-vencida"); }
            case SUSPENDIDA -> { setText("SUSPENDIDA");  getStyleClass().add("badge-suspendida"); }
        }
    }

    public void setEstado(EstadoEquipo estado) {
        getStyleClass().removeAll("badge-activa", "badge-vencida", "badge-suspendida");
        switch (estado) {
            case DISPONIBLE          -> { setText("DISPONIBLE");     getStyleClass().add("badge-activa"); }
            case EN_MANTENIMIENTO    -> { setText("MANTENIMIENTO");  getStyleClass().add("badge-suspendida"); }
            case FUERA_DE_SERVICIO   -> { setText("FUERA SERVICIO"); getStyleClass().add("badge-vencida"); }
        }
    }

    public static StatusBadge of(EstadoMembresia e) {
        StatusBadge b = new StatusBadge(); b.setEstado(e); return b;
    }

    public static StatusBadge of(EstadoEquipo e) {
        StatusBadge b = new StatusBadge(); b.setEstado(e); return b;
    }
}
