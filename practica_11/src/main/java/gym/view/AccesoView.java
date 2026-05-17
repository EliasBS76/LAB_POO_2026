package gym.view;

import gym.components.SearchableTable;
import gym.controller.AccesoController;
import gym.exception.GymException;
import gym.model.Cliente;
import gym.model.RegistroAcceso;
import gym.model.TipoAcceso;
import gym.service.DataService;
import gym.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class AccesoView {

    private final Stage stage;
    private final BorderPane root;
    private final AccesoController ctrl = new AccesoController();
    private final ObservableList<RegistroAcceso> datos =
            FXCollections.observableArrayList(DataService.getInstance().getAccesos());

    private ComboBox<Cliente> cbCliente;
    private Label lblEnGimnasio;

    public AccesoView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        construir();
    }

    private void construir() {
        Label titulo = new Label("Registro de Entrada / Salida");
        titulo.getStyleClass().add("view-title");

        actualizarContador();
        HBox header = new HBox(12, titulo, lblEnGimnasio);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 8, 20));
        root.setTop(header);

        SplitPane split = new SplitPane();
        split.setPadding(new Insets(10, 20, 20, 20));
        split.getItems().addAll(panelTabla(), panelAccion());
        split.setDividerPositions(0.68);
        root.setCenter(split);
    }

    // ── tabla de accesos ──────────────────────────────────────────────────────

    private VBox panelTabla() {
        SearchableTable<RegistroAcceso> tabla =
                new SearchableTable<>("Buscar por nombre de cliente…");

        TableColumn<RegistroAcceso, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getCliente().getNombre()));
        colCliente.setPrefWidth(170);

        TableColumn<RegistroAcceso, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getTipo().name()));
        colTipo.setPrefWidth(90);
        colTipo.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                setStyle(item.equals("ENTRADA")
                        ? "-fx-text-fill: #81c784; -fx-font-weight: bold;"
                        : "-fx-text-fill: #ef9a9a; -fx-font-weight: bold;");
            }
        });

        TableColumn<RegistroAcceso, String> colFecha = new TableColumn<>("Fecha y hora");
        colFecha.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getFechaHora()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        colFecha.setPrefWidth(160);

        TableColumn<RegistroAcceso, String> colHoy = new TableColumn<>("Hoy");
        colHoy.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().esDeHoy() ? "✔" : ""));
        colHoy.setPrefWidth(50);

        tabla.getColumns().addAll(colCliente, colTipo, colFecha, colHoy);
        tabla.setItems(datos, (r, q) ->
                r.getCliente().getNombre().toLowerCase().contains(q) ||
                r.getTipo().name().toLowerCase().contains(q)
        );

        Button btnFiltrarHoy = boton("Solo hoy", "btn-secondary");
        Button btnTodos      = boton("Todos",    "btn-primary");

        btnFiltrarHoy.setOnAction(e ->
            tabla.getSearchField().setText("") // primero limpiar, luego filtrar por fecha
        );
        // Filtro por hoy: usamos el search field no es ideal; lo hacemos con datos directos
        btnFiltrarHoy.setOnAction(e -> {
            ObservableList<RegistroAcceso> hoy = FXCollections.observableArrayList(
                    DataService.getInstance().getAccesos().stream()
                            .filter(RegistroAcceso::esDeHoy).toList()
            );
            tabla.setItems(hoy, (r, q) ->
                    r.getCliente().getNombre().toLowerCase().contains(q) ||
                    r.getTipo().name().toLowerCase().contains(q)
            );
        });
        btnTodos.setOnAction(e -> {
            datos.setAll(DataService.getInstance().getAccesos());
            tabla.setItems(datos, (r, q) ->
                    r.getCliente().getNombre().toLowerCase().contains(q) ||
                    r.getTipo().name().toLowerCase().contains(q)
            );
        });

        HBox botones = new HBox(8, btnTodos, btnFiltrarHoy);
        botones.setPadding(new Insets(10, 0, 0, 0));

        VBox panel = new VBox(8, tabla, botones);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    // ── panel de acción ───────────────────────────────────────────────────────

    private VBox panelAccion() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(18, 18, 20, 18));
        panel.getStyleClass().add("form-panel");
        panel.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Registrar Acceso");
        titulo.getStyleClass().add("form-title");

        // Selector de cliente
        ObservableList<Cliente> clientes =
                FXCollections.observableArrayList(DataService.getInstance().getClientes());
        cbCliente = new ComboBox<>(clientes);
        cbCliente.setPromptText("Seleccionar cliente…");
        cbCliente.setMaxWidth(Double.MAX_VALUE);
        cbCliente.getStyleClass().add("combo-field");

        // Info del cliente seleccionado
        Label lblInfo = new Label("—");
        lblInfo.getStyleClass().add("info-label");
        lblInfo.setWrapText(true);
        cbCliente.valueProperty().addListener((obs, old, c) -> {
            if (c == null) { lblInfo.setText("—"); return; }
            var m = c.getMembresiaActiva();
            lblInfo.setText(m != null
                    ? "Membresía: " + m.getPlan().getNombre() + " | Vence: " + m.getFechaFin()
                    : "⚠ Sin membresía activa");
        });

        // Botones grandes de entrada / salida
        Button btnEntrada = new Button("ENTRADA");
        btnEntrada.getStyleClass().addAll("btn", "btn-success");
        btnEntrada.setMaxWidth(Double.MAX_VALUE);
        btnEntrada.setStyle("-fx-font-size: 16px; -fx-padding: 18 0 18 0;");

        Button btnSalida = new Button("SALIDA");
        btnSalida.getStyleClass().addAll("btn", "btn-danger");
        btnSalida.setMaxWidth(Double.MAX_VALUE);
        btnSalida.setStyle("-fx-font-size: 16px; -fx-padding: 18 0 18 0;");

        btnEntrada.setOnAction(e -> registrar(TipoAcceso.ENTRADA, lblInfo));
        btnSalida.setOnAction(e  -> registrar(TipoAcceso.SALIDA,  lblInfo));

        // Atajos teclado: E = entrada, S = salida
        panel.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case E -> btnEntrada.fire();
                case S -> btnSalida.fire();
                default -> {}
            }
        });

        Label lblAtajos = new Label("Atajos: E = Entrada | S = Salida");
        lblAtajos.getStyleClass().add("info-label");
        lblAtajos.setStyle("-fx-font-size: 11px;");

        Separator sep = new Separator();

        // Estadística de quién está dentro
        Label lblDentroTitle = new Label("En el gimnasio ahora:");
        lblDentroTitle.getStyleClass().add("field-label");

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);

        panel.getChildren().addAll(
                titulo,
                new Label("Cliente:") {{ getStyleClass().add("field-label"); }},
                cbCliente, lblInfo,
                btnEntrada, btnSalida, lblAtajos,
                spacer, sep, lblDentroTitle, lblEnGimnasio
        );
        return panel;
    }

    // ── lógica ────────────────────────────────────────────────────────────────

    private void registrar(TipoAcceso tipo, Label lblInfo) {
        try {
            RegistroAcceso reg = ctrl.registrar(cbCliente.getValue(), tipo);
            datos.add(reg);
            actualizarContador();
            AlertUtil.mostrarExito(stage,
                    tipo.name() + " registrada para " + reg.getCliente().getNombre());
        } catch (GymException e) {
            AlertUtil.mostrarError(stage, "Acceso denegado", e.getMessage());
        }
    }

    private void actualizarContador() {
        long cnt = DataService.getInstance().clientesEnGimnasio();
        if (lblEnGimnasio == null) {
            lblEnGimnasio = new Label();
            lblEnGimnasio.getStyleClass().add("info-label");
            lblEnGimnasio.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        }
        lblEnGimnasio.setText("En el gimnasio: " + cnt + " persona(s)");
    }

    private Button boton(String texto, String estilo) {
        Button b = new Button(texto); b.getStyleClass().addAll("btn", estilo); return b;
    }

    public BorderPane getRoot() { return root; }
}
