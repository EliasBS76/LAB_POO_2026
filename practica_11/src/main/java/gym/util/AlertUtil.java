package gym.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AlertUtil {

    public static void mostrarError(Stage owner, String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        a.setTitle(titulo);
        a.setHeaderText(null);
        if (owner != null) a.initOwner(owner);
        a.showAndWait();
    }

    public static void mostrarExito(Stage owner, String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        a.setTitle("Operación exitosa");
        a.setHeaderText(null);
        if (owner != null) a.initOwner(owner);
        a.showAndWait();
    }

    public static void mostrarAviso(Stage owner, String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK);
        a.setTitle(titulo);
        a.setHeaderText(null);
        if (owner != null) a.initOwner(owner);
        a.showAndWait();
    }
}
