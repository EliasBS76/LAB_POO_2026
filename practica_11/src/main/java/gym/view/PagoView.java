package gym.view;

import gym.components.SearchableTable;
import gym.controller.PagoController;
import gym.exception.GymException;
import gym.model.Cliente;
import gym.model.EstadoPago;
import gym.model.Pago;
import gym.service.DataService;
import gym.service.PagoService;
import gym.service.ThemeService;
import gym.util.AlertUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class PagoView {

    private final Stage stage;
    private final BorderPane root;
    private final PagoController ctrl = new PagoController();
    private final ObservableList<Pago> datos =
            FXCollections.observableArrayList(DataService.getInstance().getPagos());

    private ComboBox<Cliente>  cbCliente;
    private ComboBox<String>   cbMetodo;
    private Label              lblMonto, lblPlan, lblTotal;

    public PagoView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        construir();
    }

    private void construir() {
        Label titulo = new Label("Procesamiento de Pagos");
        titulo.getStyleClass().add("view-title");
        HBox header = new HBox(titulo);
        header.setPadding(new Insets(15, 20, 8, 20));
        root.setTop(header);

        SplitPane split = new SplitPane();
        split.setPadding(new Insets(10, 20, 20, 20));
        split.getItems().addAll(panelTabla(), panelFormulario());
        split.setDividerPositions(0.65);
        root.setCenter(split);
    }

    // ── tabla de pagos ────────────────────────────────────────────────────────

    private VBox panelTabla() {
        SearchableTable<Pago> tabla = new SearchableTable<>("Buscar por cliente, plan o estado…");

        TableColumn<Pago, String> colCliente = col("Cliente", 150,
                p -> p.getCliente().getNombre());
        TableColumn<Pago, String> colPlan    = col("Plan",    90,
                p -> p.getPlan().getNombre());
        TableColumn<Pago, String> colMonto   = col("Monto",   80,
                p -> String.format("$%.2f", p.getMonto()));
        TableColumn<Pago, String> colMetodo  = col("Método",  110,
                p -> p.getMetodo());
        TableColumn<Pago, String> colFecha   = col("Fecha",   130,
                p -> p.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Columna estado con color
        TableColumn<Pago, String> colEstado = new TableColumn<>("Estado");
        colEstado.setPrefWidth(110);
        colEstado.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getEstado().name()));
        colEstado.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                Pago p = getTableView().getItems().get(getIndex());
                setStyle(switch (p.getEstado()) {
                    case COMPLETADO -> "-fx-text-fill: #81c784; -fx-font-weight: bold;";
                    case RECHAZADO  -> "-fx-text-fill: #ef9a9a; -fx-font-weight: bold;";
                    case PROCESANDO -> "-fx-text-fill: #ffcc80; -fx-font-weight: bold;";
                    default         -> "-fx-text-fill: #b0bec5;";
                });
            }
        });

        tabla.getColumns().addAll(colCliente, colPlan, colMonto, colMetodo, colFecha, colEstado);
        tabla.setItems(datos, (p, q) ->
                p.getCliente().getNombre().toLowerCase().contains(q) ||
                p.getPlan().getNombre().toLowerCase().contains(q)    ||
                p.getEstado().name().toLowerCase().contains(q)       ||
                p.getMetodo().toLowerCase().contains(q)
        );

        lblTotal = new Label("Total recaudado: $0.00");
        lblTotal.getStyleClass().add("info-label");
        lblTotal.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #81c784;");
        actualizarTotal();

        VBox panel = new VBox(8, tabla, lblTotal);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return panel;
    }

    // ── formulario de pago ────────────────────────────────────────────────────

    private VBox panelFormulario() {
        VBox form = new VBox(14);
        form.setPadding(new Insets(15, 18, 20, 18));
        form.getStyleClass().add("form-panel");

        Label fTitle = new Label("Nuevo Pago");
        fTitle.getStyleClass().add("form-title");

        // Selector de cliente
        ObservableList<Cliente> clientes =
                FXCollections.observableArrayList(DataService.getInstance().getClientes());
        cbCliente = new ComboBox<>(clientes);
        cbCliente.setPromptText("Seleccionar cliente…");
        cbCliente.setMaxWidth(Double.MAX_VALUE);
        cbCliente.getStyleClass().add("combo-field");

        lblPlan  = new Label("Plan: —");  lblPlan.getStyleClass().add("info-label");
        lblMonto = new Label("Monto: —"); lblMonto.getStyleClass().add("info-label");
        lblMonto.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");

        cbCliente.valueProperty().addListener((obs, old, c) -> {
            if (c != null && c.getMembresiaActiva() != null) {
                var plan = c.getMembresiaActiva().getPlan();
                lblPlan.setText("Plan: " + plan.getNombre());
                lblMonto.setText(String.format("$%.2f", plan.getPrecioFinal()));
            } else {
                lblPlan.setText(c != null ? "Sin membresía activa" : "Plan: —");
                lblMonto.setText("Monto: —");
            }
        });

        // Método de pago
        cbMetodo = new ComboBox<>(FXCollections.observableArrayList(
                "TARJETA DE CRÉDITO", "TARJETA DE DÉBITO", "EFECTIVO", "TRANSFERENCIA"));
        cbMetodo.setPromptText("Método de pago…");
        cbMetodo.setMaxWidth(Double.MAX_VALUE);
        cbMetodo.getStyleClass().add("combo-field");

        // Botón procesar
        Button btnProcesar = new Button("Procesar Pago");
        btnProcesar.getStyleClass().addAll("btn", "btn-success");
        btnProcesar.setMaxWidth(Double.MAX_VALUE);
        btnProcesar.setStyle("-fx-font-size: 15px; -fx-padding: 12 0 12 0;");
        btnProcesar.setOnAction(e -> procesarPago());

        // Leyenda de tecla
        Label lblTip = new Label("Presiona Enter para procesar");
        lblTip.getStyleClass().add("info-label");
        lblTip.setStyle("-fx-font-size: 11px;");

        form.setOnKeyPressed(ev -> {
            if (ev.getCode() == javafx.scene.input.KeyCode.ENTER) btnProcesar.fire();
        });

        // Separador visual
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2a2f4a;");

        // Estadísticas rápidas
        Label lblStatTitle = new Label("Resumen");
        lblStatTitle.getStyleClass().add("form-title");

        Label lblPagosHoy = new Label();
        lblPagosHoy.getStyleClass().add("info-label");
        actualizarEstadisticasRapidas(lblPagosHoy);

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);

        form.getChildren().addAll(
                fTitle,
                labelField("Cliente:"), cbCliente,
                lblPlan, lblMonto,
                labelField("Método:"), cbMetodo,
                btnProcesar, lblTip,
                spacer, sep, lblStatTitle, lblPagosHoy
        );
        return form;
    }

    // ── lógica ────────────────────────────────────────────────────────────────

    private void procesarPago() {
        try {
            Pago pago = ctrl.crearPago(cbCliente.getValue(),
                    cbMetodo.getValue() != null ? cbMetodo.getValue().split(" ")[0] : null);
            datos.add(pago);
            mostrarDialogoProcesamiento(pago);
        } catch (GymException e) {
            AlertUtil.mostrarError(stage, "Error", e.getMessage());
        }
    }

    private void mostrarDialogoProcesamiento(Pago pago) {
        Stage dlg = new Stage();
        dlg.initOwner(stage);
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Procesando pago…");
        dlg.setResizable(false);

        ProgressBar pb     = new ProgressBar(0); pb.setPrefWidth(340);
        Label lblMsg       = new Label("Iniciando…"); lblMsg.getStyleClass().add("info-label");
        Label lblIcono     = new Label("⏳"); lblIcono.setStyle("-fx-font-size: 32px;");
        Label lblClienteInfo = new Label(
                pago.getCliente().getNombre() + " — " + pago.getPlan().getNombre());
        lblClienteInfo.getStyleClass().add("info-label");

        VBox box = new VBox(12, lblIcono, lblClienteInfo, pb, lblMsg);
        box.setPadding(new Insets(28)); box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("form-panel");

        Scene sc = new Scene(box, 420, 210);
        ThemeService.getInstance().registerScene(sc);
        dlg.setScene(sc);

        PagoService task = new PagoService(pago);
        pb.progressProperty().bind(task.progressProperty());
        lblMsg.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            boolean aprobado = task.getValue();
            Platform.runLater(() -> {
                pb.progressProperty().unbind(); lblMsg.textProperty().unbind();
                lblIcono.setText(aprobado ? "✅" : "❌");
                pb.setStyle(aprobado ? "-fx-accent: #4caf50;" : "-fx-accent: #ef5350;");
                datos.set(datos.indexOf(pago), pago);
                actualizarTotal();
                new Thread(() -> {
                    try { Thread.sleep(1800); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                    Platform.runLater(dlg::close);
                }).start();
            });
        });

        new Thread(task, "pago-thread").start();
        dlg.show();
    }

    private void actualizarTotal() {
        if (lblTotal != null)
            lblTotal.setText(String.format("Total recaudado: $%.2f", ctrl.totalRecaudado()));
    }

    private void actualizarEstadisticasRapidas(Label lbl) {
        long completados = DataService.getInstance().getPagos().stream()
                .filter(p -> p.getEstado() == EstadoPago.COMPLETADO).count();
        long rechazados  = DataService.getInstance().getPagos().stream()
                .filter(p -> p.getEstado() == EstadoPago.RECHAZADO).count();
        lbl.setText("Pagos completados: " + completados + "  |  Rechazados: " + rechazados);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private TableColumn<Pago, String> col(String nombre, double ancho,
            java.util.function.Function<Pago, String> extractor) {
        TableColumn<Pago, String> c = new TableColumn<>(nombre);
        c.setCellValueFactory(cd -> new SimpleStringProperty(extractor.apply(cd.getValue())));
        c.setPrefWidth(ancho);
        return c;
    }

    private Label labelField(String texto) {
        Label l = new Label(texto); l.getStyleClass().add("field-label"); return l;
    }

    public BorderPane getRoot() { return root; }
}
