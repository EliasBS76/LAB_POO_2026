package gym.view;

import gym.components.SearchableTable;
import gym.components.StatusBadge;
import gym.controller.EquipoController;
import gym.dialog.ConfirmDialog;
import gym.exception.GymException;
import gym.model.Equipo;
import gym.model.EstadoEquipo;
import gym.service.DataService;
import gym.util.AlertUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EquipoView {

    private final Stage stage;
    private final BorderPane root;
    private final EquipoController ctrl = new EquipoController();
    private final ObservableList<Equipo> datos =
            FXCollections.observableArrayList(DataService.getInstance().getEquipos());

    private SearchableTable<Equipo> tabla;
    private TextField txtNombre, txtTipo, txtCantidad;
    private ComboBox<EstadoEquipo> cbEstado;
    private Equipo seleccionado;
    private boolean modoEdicion = false;

    public EquipoView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        construir();
    }

    private void construir() {
        Label titulo = new Label("Inventario de Equipos");
        titulo.getStyleClass().add("view-title");
        HBox header = new HBox(titulo);
        header.setPadding(new Insets(15, 20, 8, 20));
        root.setTop(header);

        SplitPane split = new SplitPane();
        split.setPadding(new Insets(10, 20, 20, 20));
        split.getItems().addAll(panelTabla(), panelFormulario());
        split.setDividerPositions(0.60);
        root.setCenter(split);
    }

    private VBox panelTabla() {
        tabla = new SearchableTable<>("Buscar por nombre, tipo o estado…");

        TableColumn<Equipo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getId()).asObject());
        colId.setPrefWidth(40);

        TableColumn<Equipo, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getNombre()));
        colNombre.setPrefWidth(160);

        TableColumn<Equipo, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTipo()));
        colTipo.setPrefWidth(100);

        TableColumn<Equipo, Integer> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getCantidad()).asObject());
        colCant.setPrefWidth(80);

        TableColumn<Equipo, String> colMant = new TableColumn<>("Último Mantenimiento");
        colMant.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getUltimoMantenimiento().toString()));
        colMant.setPrefWidth(160);

        // Columna de estado con StatusBadge personalizado
        TableColumn<Equipo, Void> colEstado = new TableColumn<>("Estado");
        colEstado.setPrefWidth(130);
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

        tabla.getColumns().addAll(colId, colNombre, colTipo, colCant, colMant, colEstado);
        tabla.setItems(datos, (eq, q) ->
                eq.getNombre().toLowerCase().contains(q) ||
                eq.getTipo().toLowerCase().contains(q)   ||
                eq.getEstado().name().toLowerCase().contains(q)
        );

        tabla.getTableView().getSelectionModel().selectedItemProperty()
             .addListener((obs, old, nuevo) -> { if (nuevo != null) cargarForm(nuevo); });

        Button btnNuevo       = boton("Nuevo equipo",  "btn-primary");
        Button btnEliminar    = boton("Eliminar",      "btn-danger");
        Button btnMantenimiento = boton("Registrar mantenimiento", "btn-warning");
        Button btnDisponible  = boton("Marcar disponible",         "btn-success");

        btnNuevo.setOnAction(e         -> prepararNuevo());
        btnEliminar.setOnAction(e      -> eliminar());
        btnMantenimiento.setOnAction(e -> registrarMantenimiento());
        btnDisponible.setOnAction(e    -> marcarDisponible());

        HBox botones = new HBox(8, btnNuevo, btnEliminar, btnMantenimiento, btnDisponible);
        botones.setPadding(new Insets(10, 0, 0, 0));

        VBox panel = new VBox(8, tabla, botones);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    private VBox panelFormulario() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(10, 15, 15, 15));
        form.getStyleClass().add("form-panel");

        Label fTitle = new Label("Datos del Equipo");
        fTitle.getStyleClass().add("form-title");

        txtNombre   = campo("Nombre del equipo");
        txtTipo     = campo("Tipo (Cardio, Fuerza…)");
        txtCantidad = campo("Cantidad");

        cbEstado = new ComboBox<>(FXCollections.observableArrayList(EstadoEquipo.values()));
        cbEstado.setPromptText("Estado…");
        cbEstado.setMaxWidth(Double.MAX_VALUE);
        cbEstado.getStyleClass().add("combo-field");

        Button btnGuardar = boton("Guardar", "btn-primary");
        Button btnLimpiar = boton("Limpiar", "btn-secondary");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        btnGuardar.setOnAction(e -> guardar());
        btnLimpiar.setOnAction(e -> limpiar());

        HBox btnBox = new HBox(8, btnLimpiar, btnGuardar);
        HBox.setHgrow(btnGuardar, Priority.ALWAYS);
        HBox.setHgrow(btnLimpiar, Priority.ALWAYS);
        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);

        form.getChildren().addAll(fTitle,
                grupo("Nombre:",   txtNombre),
                grupo("Tipo:",     txtTipo),
                grupo("Cantidad:", txtCantidad),
                new Label("Estado:"), cbEstado,
                spacer, btnBox);
        return form;
    }

    private void guardar() {
        try {
            int cant = Integer.parseInt(txtCantidad.getText().trim());
            EstadoEquipo estado = cbEstado.getValue() != null ? cbEstado.getValue() : EstadoEquipo.DISPONIBLE;

            if (modoEdicion && seleccionado != null) {
                ctrl.actualizar(seleccionado, txtNombre.getText(), txtTipo.getText(), cant, estado);
                datos.set(datos.indexOf(seleccionado), seleccionado);
            } else {
                Equipo nuevo = ctrl.crear(txtNombre.getText(), txtTipo.getText(), cant, estado);
                datos.add(nuevo);
            }
            limpiar();
            AlertUtil.mostrarExito(stage, "Equipo guardado correctamente.");
        } catch (NumberFormatException e) {
            AlertUtil.mostrarError(stage, "Formato incorrecto", "La cantidad debe ser un número entero.");
        } catch (GymException e) {
            AlertUtil.mostrarError(stage, "Validación", e.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona un equipo."); return; }
        ConfirmDialog dlg = new ConfirmDialog(stage, "Eliminar equipo",
                "¿Eliminar \"" + seleccionado.getNombre() + "\"?");
        if (dlg.showAndGetResult()) {
            ctrl.eliminar(seleccionado);
            datos.remove(seleccionado);
            limpiar();
        }
    }

    private void registrarMantenimiento() {
        if (seleccionado == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona un equipo."); return; }
        ctrl.registrarMantenimiento(seleccionado);
        datos.set(datos.indexOf(seleccionado), seleccionado);
        AlertUtil.mostrarExito(stage, "Mantenimiento registrado para " + seleccionado.getNombre() + ".");
    }

    private void marcarDisponible() {
        if (seleccionado == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona un equipo."); return; }
        ctrl.marcarDisponible(seleccionado);
        datos.set(datos.indexOf(seleccionado), seleccionado);
        AlertUtil.mostrarExito(stage, seleccionado.getNombre() + " marcado como DISPONIBLE.");
    }

    private void cargarForm(Equipo e) {
        seleccionado = e; modoEdicion = true;
        txtNombre.setText(e.getNombre());
        txtTipo.setText(e.getTipo());
        txtCantidad.setText(String.valueOf(e.getCantidad()));
        cbEstado.setValue(e.getEstado());
    }

    private void prepararNuevo() {
        limpiar();
        tabla.getTableView().getSelectionModel().clearSelection();
        txtNombre.requestFocus();
    }

    private void limpiar() {
        seleccionado = null; modoEdicion = false;
        txtNombre.clear(); txtTipo.clear(); txtCantidad.clear();
        cbEstado.setValue(null);
    }

    private TextField campo(String prompt) {
        TextField tf = new TextField(); tf.setPromptText(prompt); tf.getStyleClass().add("form-field"); return tf;
    }

    private VBox grupo(String label, TextField field) {
        Label lbl = new Label(label); lbl.getStyleClass().add("field-label"); return new VBox(3, lbl, field);
    }

    private Button boton(String texto, String estilo) {
        Button b = new Button(texto); b.getStyleClass().addAll("btn", estilo); return b;
    }

    public BorderPane getRoot() { return root; }
}
