package gym.view;

import gym.components.SearchableTable;
import gym.components.StatusBadge;
import gym.controller.MembresiaController;
import gym.dialog.ConfirmDialog;
import gym.exception.MembresiaException;
import gym.model.Cliente;
import gym.model.EstadoMembresia;
import gym.model.Membresia;
import gym.model.Plan;
import gym.service.DataService;
import gym.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MembresiaView {

    private final Stage stage;
    private final BorderPane root;
    private final MembresiaController ctrl = new MembresiaController();
    private final ObservableList<Membresia> datos = FXCollections.observableArrayList();

    private SearchableTable<Membresia> tabla;
    private Membresia seleccionada;

    public MembresiaView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        recargarDatos();
        construir();
    }

    private void recargarDatos() {
        datos.setAll(ctrl.todasLasMembresias());
    }

    private void construir() {
        Label titulo = new Label("Membresías");
        titulo.getStyleClass().add("view-title");
        HBox header = new HBox(titulo);
        header.setPadding(new Insets(15, 20, 8, 20));
        root.setTop(header);
        root.setCenter(panelTabla());
    }

    private VBox panelTabla() {
        tabla = new SearchableTable<>("Buscar por cliente o plan…");

        TableColumn<Membresia, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(45);

        TableColumn<Membresia, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getCliente().getNombre()));
        colCliente.setPrefWidth(160);

        TableColumn<Membresia, String> colPlan = new TableColumn<>("Plan");
        colPlan.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getPlan().getNombre()));
        colPlan.setPrefWidth(90);

        TableColumn<Membresia, String> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getFechaInicio().toString()));
        colInicio.setPrefWidth(100);

        TableColumn<Membresia, String> colFin = new TableColumn<>("Vencimiento");
        colFin.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getFechaFin().toString()));
        colFin.setPrefWidth(100);

        TableColumn<Membresia, String> colRenov = new TableColumn<>("Auto-renovación");
        colRenov.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().isRenovacionAutomatica() ? "Sí" : "No"));
        colRenov.setPrefWidth(110);

        // Columna de estado con StatusBadge
        TableColumn<Membresia, Void> colEstado = new TableColumn<>("Estado");
        colEstado.setPrefWidth(110);
        colEstado.setCellFactory(col -> new TableCell<>() {
            private final StatusBadge badge = new StatusBadge();
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    badge.setEstado(getTableRow().getItem().getEstado());
                    setGraphic(badge);
                }
            }
        });

        tabla.getColumns().addAll(colId, colCliente, colPlan, colInicio, colFin, colRenov, colEstado);
        tabla.setItems(datos, (m, q) ->
                m.getCliente().getNombre().toLowerCase().contains(q) ||
                m.getPlan().getNombre().toLowerCase().contains(q) ||
                m.getEstado().name().toLowerCase().contains(q)
        );

        tabla.getTableView().getSelectionModel().selectedItemProperty()
             .addListener((obs, old, nuevo) -> seleccionada = nuevo);

        // Botones de acción
        Button btnRenovar    = boton("Renovar",          "btn-success");
        Button btnSuspender  = boton("Suspender",        "btn-warning");
        Button btnToggleRen  = boton("Toggle Auto-Ren.", "btn-secondary");
        Button btnRecargar   = boton("Recargar",         "btn-primary");

        btnRenovar.setOnAction(e   -> renovar());
        btnSuspender.setOnAction(e -> suspender());
        btnToggleRen.setOnAction(e -> toggleRenovacion());
        btnRecargar.setOnAction(e  -> { recargarDatos(); tabla.limpiarBusqueda(); });

        HBox botones = new HBox(8, btnRenovar, btnSuspender, btnToggleRen, btnRecargar);
        botones.setPadding(new Insets(10, 20, 20, 20));

        VBox panel = new VBox(8, tabla, botones);
        panel.setPadding(new Insets(10, 20, 0, 20));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    private void renovar() {
        if (seleccionada == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona una membresía."); return; }
        ctrl.renovar(seleccionada);
        recargarDatos();
        AlertUtil.mostrarExito(stage, "Membresía renovada. Próximo vencimiento: " + seleccionada.getFechaFin());
    }

    private void suspender() {
        if (seleccionada == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona una membresía."); return; }
        ConfirmDialog dlg = new ConfirmDialog(stage, "Suspender membresía",
                "¿Suspender la membresía de " + seleccionada.getCliente().getNombre() + "?");
        if (dlg.showAndGetResult()) {
            try {
                ctrl.suspender(seleccionada);
                recargarDatos();
            } catch (MembresiaException e) {
                AlertUtil.mostrarError(stage, "Error", e.getMessage());
            }
        }
    }

    private void toggleRenovacion() {
        if (seleccionada == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona una membresía."); return; }
        ctrl.toggleRenovacion(seleccionada);
        recargarDatos();
        AlertUtil.mostrarExito(stage, "Renovación automática: " + (seleccionada.isRenovacionAutomatica() ? "activada" : "desactivada"));
    }

    private Button boton(String texto, String estilo) {
        Button b = new Button(texto); b.getStyleClass().addAll("btn", estilo); return b;
    }

    public BorderPane getRoot() { return root; }
}
