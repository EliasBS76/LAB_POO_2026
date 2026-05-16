package gym.dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Diálogo modal personalizado de confirmación.
 * Soporta atajos de teclado: Enter = confirmar, Escape = cancelar.
 */
public class ConfirmDialog extends Stage {

    private boolean confirmed = false;

    public ConfirmDialog(Stage owner, String titulo, String mensaje) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        setResizable(false);

        VBox root = new VBox(18);
        root.setPadding(new Insets(30, 35, 25, 35));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("confirm-dialog");

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("dialog-title");

        Label lblMensaje = new Label(mensaje);
        lblMensaje.getStyleClass().add("dialog-message");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(360);
        lblMensaje.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.getStyleClass().addAll("btn", "btn-danger");
        btnConfirmar.setPrefWidth(130);
        btnConfirmar.setDefaultButton(true);

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().addAll("btn", "btn-secondary");
        btnCancelar.setPrefWidth(130);

        btnConfirmar.setOnAction(e -> { confirmed = true;  close(); });
        btnCancelar.setOnAction(e ->  { confirmed = false; close(); });

        HBox buttons = new HBox(12, btnCancelar, btnConfirmar);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(lblTitulo, lblMensaje, buttons);

        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(
                getClass().getResource("/gym/styles.css").toExternalForm()
            );
        } catch (Exception ignored) {}

        // Keyboard shortcuts
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) { confirmed = false; close(); }
        });

        setScene(scene);
    }

    /** Muestra el diálogo y retorna true si el usuario confirmó. */
    public boolean showAndGetResult() {
        showAndWait();
        return confirmed;
    }
}
