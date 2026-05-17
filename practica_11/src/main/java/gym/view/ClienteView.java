package gym.view;

import gym.components.SearchableTable;
import gym.controller.ClienteController;
import gym.dialog.ConfirmDialog;
import gym.exception.ClienteException;
import gym.exception.MembresiaException;
import gym.model.Cliente;
import gym.model.Membresia;
import gym.model.Plan;
import gym.service.DataService;
import gym.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ClienteView {

    private final Stage stage;
    private final BorderPane root;
    private final ClienteController ctrl = new ClienteController();
    private final ObservableList<Cliente> datos =
            FXCollections.observableArrayList(DataService.getInstance().getClientes());

    private SearchableTable<Cliente> tabla;
    private TextField txtNombre, txtEmail, txtTelefono;
    private ComboBox<Plan> cbPlan;
    private CheckBox chkRenovacion;
    private Label lblPuntos, lblEstado;
    private Cliente seleccionado;
    private boolean modoEdicion = false;

    public ClienteView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        construir();
    }

    private void construir() {
        Label titulo = new Label("Gestión de Clientes");
        titulo.getStyleClass().add("view-title");
        HBox header = new HBox(titulo);
        header.setPadding(new Insets(15, 20, 8, 20));
        root.setTop(header);

        SplitPane split = new SplitPane();
        split.setPadding(new Insets(10, 20, 20, 20));
        split.getItems().addAll(panelTabla(), panelFormulario());
        split.setDividerPositions(0.62);
        root.setCenter(split);
    }

    // ── tabla ─────────────────────────────────────────────────────────────

    private VBox panelTabla() {
        tabla = new SearchableTable<>("Buscar por nombre, email o teléfono…");

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(45);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(160);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(190);

        TableColumn<Cliente, String> colTel = new TableColumn<>("Teléfono");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTel.setPrefWidth(100);

        TableColumn<Cliente, Integer> colPuntos = new TableColumn<>("Puntos");
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colPuntos.setPrefWidth(65);

        TableColumn<Cliente, String> colPlan = new TableColumn<>("Plan");
        colPlan.setCellValueFactory(cd -> {
            Membresia m = cd.getValue().getMembresiaActiva();
            return new javafx.beans.property.SimpleStringProperty(
                    m != null ? m.getPlan().getNombre() : "Sin membresía");
        });
        colPlan.setPrefWidth(90);

        tabla.getColumns().addAll(colId, colNombre, colEmail, colTel, colPuntos, colPlan);
        tabla.setItems(datos, (c, q) ->
                c.getNombre().toLowerCase().contains(q) ||
                c.getEmail().toLowerCase().contains(q)  ||
                c.getTelefono().contains(q)
        );

        tabla.getTableView().getSelectionModel().selectedItemProperty()
             .addListener((obs, old, nuevo) -> { if (nuevo != null) cargarForm(nuevo); });

        tabla.getTableView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && seleccionado != null) modoEdicion = true;
        });

        Button btnNuevo    = boton("Nuevo",             "btn-primary");
        Button btnEliminar = boton("Eliminar",          "btn-danger");
        Button btnRenovar  = boton("Renovar membresía", "btn-success");

        btnNuevo.setOnAction(e    -> prepararNuevo());
        btnEliminar.setOnAction(e -> eliminar());
        btnRenovar.setOnAction(e  -> renovar());

        HBox botones = new HBox(8, btnNuevo, btnEliminar, btnRenovar);
        botones.setPadding(new Insets(10, 0, 0, 0));

        VBox panel = new VBox(8, tabla, botones);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    // ── formulario ────────────────────────────────────────────────────────

    private VBox panelFormulario() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(10, 15, 15, 15));
        form.getStyleClass().add("form-panel");

        Label fTitle = new Label("Datos del Cliente");
        fTitle.getStyleClass().add("form-title");

        txtNombre   = campo("Nombre completo");
        txtEmail    = campo("Correo electrónico");
        txtTelefono = campo("Teléfono");

        validacionRealTime(txtNombre,   s -> s.length() >= 3,                   "Mínimo 3 caracteres");
        validacionRealTime(txtEmail,    s -> s.matches("[^@]+@[^@]+\\.[^@]+"),  "Formato: usuario@dominio.com");
        validacionRealTime(txtTelefono, s -> s.matches("[0-9\\-+() ]{7,15}"),   "Solo dígitos, guiones o paréntesis");

        cbPlan = new ComboBox<>(FXCollections.observableArrayList(Plan.values()));
        cbPlan.setPromptText("Seleccionar plan…");
        cbPlan.setMaxWidth(Double.MAX_VALUE);
        cbPlan.getStyleClass().add("combo-field");

        cbPlan.valueProperty().addListener((obs, old, p) -> {
            if (p != null) lblEstado.setText(
                    String.format("Precio: $%.2f (%.0f%% desc.)", p.getPrecioFinal(), p.getDescuento() * 100));
        });

        chkRenovacion = new CheckBox("Renovación automática");

        lblPuntos = new Label("Puntos: 0");  lblPuntos.getStyleClass().add("info-label");
        lblEstado = new Label("Estado: —");  lblEstado.getStyleClass().add("info-label");

        Button btnGuardar = boton("Guardar", "btn-primary");
        Button btnLimpiar = boton("Limpiar", "btn-secondary");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        txtTelefono.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) btnGuardar.fire(); });
        btnGuardar.setOnAction(e -> guardar());
        btnLimpiar.setOnAction(e -> limpiar());

        HBox btnBox = new HBox(8, btnLimpiar, btnGuardar);
        HBox.setHgrow(btnGuardar, Priority.ALWAYS);
        HBox.setHgrow(btnLimpiar, Priority.ALWAYS);
        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);

        form.getChildren().addAll(fTitle,
                grupo("Nombre:",   txtNombre),
                grupo("Email:",    txtEmail),
                grupo("Teléfono:", txtTelefono),
                new Label("Plan:"), cbPlan,
                chkRenovacion, lblPuntos, lblEstado,
                spacer, btnBox);
        return form;
    }

    // ── acciones ─────────────────────────────────────────────────────────

    private void guardar() {
        try {
            if (modoEdicion && seleccionado != null) {
                ctrl.actualizar(seleccionado, txtNombre.getText(), txtEmail.getText(),
                        txtTelefono.getText(), cbPlan.getValue(), chkRenovacion.isSelected());
                datos.set(datos.indexOf(seleccionado), seleccionado);
            } else {
                Cliente nuevo = ctrl.crear(txtNombre.getText(), txtEmail.getText(),
                        txtTelefono.getText(), cbPlan.getValue(), chkRenovacion.isSelected());
                datos.add(nuevo);
            }
            limpiar();
            AlertUtil.mostrarExito(stage, "Cliente guardado correctamente.");
        } catch (ClienteException e) {
            AlertUtil.mostrarError(stage, "Validación", e.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            AlertUtil.mostrarError(stage, "Sin selección", "Selecciona un cliente en la tabla."); return;
        }
        ConfirmDialog dlg = new ConfirmDialog(stage, "Eliminar cliente",
                "¿Eliminar a \"" + seleccionado.getNombre() + "\"?\nEsta acción no puede deshacerse.");
        if (dlg.showAndGetResult()) {
            ctrl.eliminar(seleccionado);
            datos.remove(seleccionado);
            limpiar();
        }
    }

    private void renovar() {
        if (seleccionado == null) {
            AlertUtil.mostrarError(stage, "Sin selección", "Selecciona un cliente."); return;
        }
        try {
            ctrl.renovarMembresia(seleccionado);
            cargarForm(seleccionado);
            AlertUtil.mostrarExito(stage, "Membresía renovada. Puntos acumulados: " + seleccionado.getPuntos());
        } catch (MembresiaException e) {
            AlertUtil.mostrarError(stage, "Error", e.getMessage());
        }
    }

    private void cargarForm(Cliente c) {
        seleccionado = c; modoEdicion = true;
        txtNombre.setText(c.getNombre());
        txtEmail.setText(c.getEmail());
        txtTelefono.setText(c.getTelefono());
        lblPuntos.setText("Puntos: " + c.getPuntos());
        Membresia m = c.getMembresiaActiva();
        if (m != null) {
            cbPlan.setValue(m.getPlan());
            chkRenovacion.setSelected(m.isRenovacionAutomatica());
            lblEstado.setText("Estado: " + m.getEstado() + "  |  Vence: " + m.getFechaFin());
        } else {
            cbPlan.setValue(null); chkRenovacion.setSelected(false);
            lblEstado.setText("Sin membresía activa");
        }
    }

    private void prepararNuevo() {
        limpiar();
        tabla.getTableView().getSelectionModel().clearSelection();
        txtNombre.requestFocus();
    }

    private void limpiar() {
        seleccionado = null; modoEdicion = false;
        txtNombre.clear(); txtEmail.clear(); txtTelefono.clear();
        cbPlan.setValue(null); chkRenovacion.setSelected(false);
        lblPuntos.setText("Puntos: 0"); lblEstado.setText("Estado: —");
        for (TextField tf : List.of(txtNombre, txtEmail, txtTelefono))
            tf.getStyleClass().removeAll("field-valid", "field-invalid");
    }

    // ── helpers ───────────────────────────────────────────────────────────

    private TextField campo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("form-field");
        return tf;
    }

    private VBox grupo(String label, TextField field) {
        Label lbl = new Label(label); lbl.getStyleClass().add("field-label");
        return new VBox(3, lbl, field);
    }

    private Button boton(String texto, String estilo) {
        Button b = new Button(texto);
        b.getStyleClass().addAll("btn", estilo);
        return b;
    }

    private void validacionRealTime(TextField field, java.util.function.Predicate<String> test, String tip) {
        field.textProperty().addListener((obs, old, val) -> {
            field.getStyleClass().removeAll("field-valid", "field-invalid");
            if (!val.isBlank())
                field.getStyleClass().add(test.test(val) ? "field-valid" : "field-invalid");
        });
        field.setTooltip(new Tooltip(tip));
    }

    public BorderPane getRoot() { return root; }
}
