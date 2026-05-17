package gym.view;

import gym.components.SearchableTable;
import gym.controller.ClaseGrupalController;
import gym.dialog.ConfirmDialog;
import gym.exception.GymException;
import gym.model.ClaseGrupal;
import gym.model.Cliente;
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

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ClaseGrupalView {

    private final Stage stage;
    private final BorderPane root;
    private final ClaseGrupalController ctrl = new ClaseGrupalController();
    private final ObservableList<ClaseGrupal> datos =
            FXCollections.observableArrayList(DataService.getInstance().getClases());

    private SearchableTable<ClaseGrupal> tabla;
    private TextField txtNombre, txtInstructor, txtHora, txtCapacidad;
    private ComboBox<DayOfWeek> cbDia;
    private ClaseGrupal seleccionada;
    private boolean modoEdicion = false;

    public ClaseGrupalView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        construir();
    }

    private void construir() {
        Label titulo = new Label("Calendario de Clases Grupales");
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
        tabla = new SearchableTable<>("Buscar por nombre, instructor o día…");

        TableColumn<ClaseGrupal, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getId()).asObject());
        colId.setPrefWidth(40);

        TableColumn<ClaseGrupal, String> colNombre = new TableColumn<>("Clase");
        colNombre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getNombre()));
        colNombre.setPrefWidth(130);

        TableColumn<ClaseGrupal, String> colInstructor = new TableColumn<>("Instructor");
        colInstructor.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getInstructor()));
        colInstructor.setPrefWidth(130);

        TableColumn<ClaseGrupal, String> colDia = new TableColumn<>("Día");
        colDia.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getDia().toString()));
        colDia.setPrefWidth(90);

        TableColumn<ClaseGrupal, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getHora().toString()));
        colHora.setPrefWidth(70);

        TableColumn<ClaseGrupal, String> colCupos = new TableColumn<>("Inscritos / Cap.");
        colCupos.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getInscritos() + " / " + cd.getValue().getCapacidadMaxima()));
        colCupos.setPrefWidth(110);

        tabla.getColumns().addAll(colId, colNombre, colInstructor, colDia, colHora, colCupos);
        tabla.setItems(datos, (c, q) ->
                c.getNombre().toLowerCase().contains(q) ||
                c.getInstructor().toLowerCase().contains(q) ||
                c.getDia().toString().toLowerCase().contains(q)
        );

        tabla.getTableView().getSelectionModel().selectedItemProperty()
             .addListener((obs, old, nuevo) -> { if (nuevo != null) cargarForm(nuevo); });

        Button btnNuevo    = boton("Nueva clase", "btn-primary");
        Button btnEliminar = boton("Eliminar",    "btn-danger");
        Button btnInscribir = boton("Inscribir cliente…", "btn-success");

        btnNuevo.setOnAction(e    -> prepararNuevo());
        btnEliminar.setOnAction(e -> eliminar());
        btnInscribir.setOnAction(e -> inscribirCliente());

        HBox botones = new HBox(8, btnNuevo, btnEliminar, btnInscribir);
        botones.setPadding(new Insets(10, 0, 0, 0));

        VBox panel = new VBox(8, tabla, botones);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    private VBox panelFormulario() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(10, 15, 15, 15));
        form.getStyleClass().add("form-panel");

        Label fTitle = new Label("Datos de la Clase");
        fTitle.getStyleClass().add("form-title");

        txtNombre     = campo("Nombre de la clase");
        txtInstructor = campo("Instructor");
        txtHora       = campo("Hora (HH:MM)");
        txtCapacidad  = campo("Capacidad máxima");

        cbDia = new ComboBox<>(FXCollections.observableArrayList(DayOfWeek.values()));
        cbDia.setPromptText("Seleccionar día…");
        cbDia.setMaxWidth(Double.MAX_VALUE);
        cbDia.getStyleClass().add("combo-field");

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
                grupo("Nombre:",     txtNombre),
                grupo("Instructor:", txtInstructor),
                new Label("Día:"), cbDia,
                grupo("Hora:",       txtHora),
                grupo("Capacidad:",  txtCapacidad),
                spacer, btnBox);
        return form;
    }

    private void guardar() {
        try {
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            int cap = Integer.parseInt(txtCapacidad.getText().trim());
            DayOfWeek dia = cbDia.getValue();
            if (dia == null) throw new GymException("Selecciona un día.");

            if (modoEdicion && seleccionada != null) {
                ctrl.actualizar(seleccionada, txtNombre.getText(), txtInstructor.getText(), dia, hora, cap);
                datos.set(datos.indexOf(seleccionada), seleccionada);
            } else {
                ClaseGrupal nueva = ctrl.crear(txtNombre.getText(), txtInstructor.getText(), dia, hora, cap);
                datos.add(nueva);
            }
            limpiar();
            AlertUtil.mostrarExito(stage, "Clase guardada correctamente.");
        } catch (DateTimeParseException e) {
            AlertUtil.mostrarError(stage, "Formato incorrecto", "La hora debe tener formato HH:MM (ej. 09:30).");
        } catch (NumberFormatException e) {
            AlertUtil.mostrarError(stage, "Formato incorrecto", "La capacidad debe ser un número entero.");
        } catch (GymException e) {
            AlertUtil.mostrarError(stage, "Validación", e.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionada == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona una clase."); return; }
        ConfirmDialog dlg = new ConfirmDialog(stage, "Eliminar clase",
                "¿Eliminar la clase \"" + seleccionada.getNombre() + "\"?");
        if (dlg.showAndGetResult()) {
            ctrl.eliminar(seleccionada);
            datos.remove(seleccionada);
            limpiar();
        }
    }

    private void inscribirCliente() {
        if (seleccionada == null) { AlertUtil.mostrarError(stage, "Sin selección", "Selecciona una clase."); return; }
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(DataService.getInstance().getClientes());
        ChoiceDialog<Cliente> dlg = new ChoiceDialog<>(clientes.isEmpty() ? null : clientes.get(0), clientes);
        dlg.setTitle("Inscribir cliente");
        dlg.setHeaderText("Clase: " + seleccionada.getNombre() + " — Lugares disponibles: " + seleccionada.getLugaresDisponibles());
        dlg.setContentText("Seleccionar cliente:");
        dlg.showAndWait().ifPresent(cliente -> {
            try {
                String msg = ctrl.inscribirCliente(seleccionada, cliente);
                datos.set(datos.indexOf(seleccionada), seleccionada);
                AlertUtil.mostrarExito(stage, msg);
            } catch (GymException e) {
                AlertUtil.mostrarError(stage, "Error", e.getMessage());
            }
        });
    }

    private void cargarForm(ClaseGrupal c) {
        seleccionada = c; modoEdicion = true;
        txtNombre.setText(c.getNombre());
        txtInstructor.setText(c.getInstructor());
        cbDia.setValue(c.getDia());
        txtHora.setText(c.getHora().toString());
        txtCapacidad.setText(String.valueOf(c.getCapacidadMaxima()));
    }

    private void prepararNuevo() {
        limpiar();
        tabla.getTableView().getSelectionModel().clearSelection();
        txtNombre.requestFocus();
    }

    private void limpiar() {
        seleccionada = null; modoEdicion = false;
        txtNombre.clear(); txtInstructor.clear();
        txtHora.clear(); txtCapacidad.clear();
        cbDia.setValue(null);
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
