package gym.view;

import gym.model.*;
import gym.service.DataService;
import gym.service.ReporteService;
import gym.service.ThemeService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class ReporteView {

    private final Stage stage;
    private final BorderPane root;
    private final DataService ds = DataService.getInstance();

    public ReporteView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("view-root");
        construir();
    }

    private void construir() {
        Label titulo = new Label("Dashboard & Reportes");
        titulo.getStyleClass().add("view-title");

        Button btnRefresh = boton("Actualizar datos", "btn-secondary");
        btnRefresh.setOnAction(e -> actualizarDashboard());

        HBox header = new HBox(12, titulo, btnRefresh);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 8, 20));
        root.setTop(header);

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(15, 25, 25, 25));
        contenido.getChildren().addAll(panelKPIs(), panelPDF(), panelUltimosMovimientos());

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        root.setCenter(scroll);
    }

    // ── KPIs ─────────────────────────────────────────────────────────────────

    private HBox panelKPIs() {
        List<Cliente> clientes = ds.getClientes();
        long activos = clientes.stream().filter(c -> c.getMembresiaActiva() != null).count();
        long vencidos = clientes.size() - activos;
        double ingreso = clientes.stream()
                .filter(c -> c.getMembresiaActiva() != null)
                .mapToDouble(c -> c.getMembresiaActiva().getPlan().getPrecioFinal()).sum();
        long pagosOK = ds.getPagos().stream()
                .filter(p -> p.getEstado() == EstadoPago.COMPLETADO).count();
        long enGim = ds.clientesEnGimnasio();
        int enMant = (int) ds.getEquipos().stream()
                .filter(e -> e.getEstado() != EstadoEquipo.DISPONIBLE).count();

        HBox row = new HBox(15);
        row.getChildren().addAll(
            kpi("Clientes",          String.valueOf(clientes.size()), "#4fc3f7",  "total"),
            kpi("Activos",           String.valueOf(activos),          "#81c784",  "membresía vigente"),
            kpi("Vencidos",          String.valueOf(vencidos),         "#ef9a9a",  "sin membresía"),
            kpi("Ingreso mensual",   String.format("$%.0f", ingreso),  "#ffcc80",  "proyectado"),
            kpi("Pagos completados", String.valueOf(pagosOK),          "#ce93d8",  "total recaudado"),
            kpi("En el gimnasio",    String.valueOf(enGim),            "#80cbc4",  "en este momento"),
            kpi("En mantenimiento",  String.valueOf(enMant),           "#ff8a65",  "equipos")
        );
        row.getChildren().forEach(node -> HBox.setHgrow(node, Priority.ALWAYS));
        return row;
    }

    private VBox kpi(String titulo, String valor, String color, String subtitulo) {
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #b0bec5;");

        Label lblSub = new Label(subtitulo);
        lblSub.setStyle("-fx-font-size: 10px; -fx-text-fill: #607d8b;");

        VBox card = new VBox(4, lblValor, lblTitulo, lblSub);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #1e2235; -fx-background-radius: 10;" +
                      "-fx-border-color: " + color + "40; -fx-border-radius: 10; -fx-border-width: 1;");
        card.setMinWidth(110);
        return card;
    }

    // ── botón PDF ─────────────────────────────────────────────────────────────

    private VBox panelPDF() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1e2235; -fx-background-radius: 10;" +
                       "-fx-border-color: #2a2f4a; -fx-border-radius: 10; -fx-border-width: 1;");

        Label titulo = new Label("Generar Reporte PDF");
        titulo.getStyleClass().add("form-title");

        Label desc = new Label(
            "El reporte incluye:\n" +
            "  • Estadísticas de clientes por plan\n" +
            "  • Ingresos proyectados mensuales\n" +
            "  • Historial de últimas transacciones\n" +
            "  • Estado completo del inventario"
        );
        desc.setStyle("-fx-text-fill: #90a4ae; -fx-font-size: 13px;");

        Button btnGenerar = boton("Generar PDF", "btn-primary");
        btnGenerar.setStyle("-fx-font-size: 14px; -fx-padding: 10 30 10 30;");
        btnGenerar.setOnAction(e -> generarPDF());

        HBox btnRow = new HBox(btnGenerar);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        panel.getChildren().addAll(titulo, desc, btnRow);
        return panel;
    }

    // ── últimos movimientos ────────────────────────────────────────────────────

    private VBox panelUltimosMovimientos() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1e2235; -fx-background-radius: 10;" +
                       "-fx-border-color: #2a2f4a; -fx-border-radius: 10; -fx-border-width: 1;");

        Label titulo = new Label("Últimos pagos registrados");
        titulo.getStyleClass().add("form-title");

        var pagos = ds.getPagos().stream()
                .sorted((a, b) -> b.getFecha().compareTo(a.getFecha()))
                .limit(6).toList();

        if (pagos.isEmpty()) {
            panel.getChildren().addAll(titulo, new Label("No hay pagos registrados.") {{
                setStyle("-fx-text-fill: #607d8b;");
            }});
            return panel;
        }

        for (Pago p : pagos) {
            HBox fila = new HBox(12);
            fila.setPadding(new Insets(8, 0, 8, 0));
            fila.setStyle("-fx-border-color: transparent transparent #2a2f4a transparent; -fx-border-width: 1;");
            fila.setAlignment(Pos.CENTER_LEFT);

            String icono = p.getEstado() == EstadoPago.COMPLETADO ? "✅" : "❌";
            Label lblIco   = new Label(icono);
            lblIco.setStyle("-fx-font-size: 18px;");

            Label lblNom   = new Label(p.getCliente().getNombre());
            lblNom.setStyle("-fx-text-fill: #cdd9e5; -fx-font-weight: bold; -fx-min-width: 150px;");

            Label lblPlan  = new Label(p.getPlan().getNombre());
            lblPlan.setStyle("-fx-text-fill: #90a4ae; -fx-min-width: 90px;");

            Label lblMonto = new Label(String.format("$%.2f", p.getMonto()));
            lblMonto.setStyle("-fx-text-fill: #81c784; -fx-font-weight: bold; -fx-min-width: 75px;");

            Label lblMetodo = new Label(p.getMetodo());
            lblMetodo.setStyle("-fx-text-fill: #78909c; -fx-min-width: 120px;");

            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Label lblFecha = new Label(
                p.getFecha().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm")));
            lblFecha.setStyle("-fx-text-fill: #546e7a; -fx-font-size: 11px;");

            fila.getChildren().addAll(lblIco, lblNom, lblPlan, lblMonto, lblMetodo, sp, lblFecha);
            panel.getChildren().add(fila);
        }
        panel.getChildren().add(0, titulo);
        return panel;
    }

    // ── generación PDF ────────────────────────────────────────────────────────

    private void generarPDF() {
        Stage dlg = new Stage();
        dlg.initOwner(stage);
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Generando reporte…");
        dlg.setResizable(false);

        ProgressBar pb  = new ProgressBar(0); pb.setPrefWidth(340);
        Label lblMsg    = new Label("Iniciando…"); lblMsg.getStyleClass().add("info-label");
        Label lblIcon   = new Label("📄"); lblIcon.setStyle("-fx-font-size: 36px;");

        VBox box = new VBox(12, lblIcon, pb, lblMsg);
        box.setPadding(new Insets(28)); box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("form-panel");

        Scene sc = new Scene(box, 420, 200);
        ThemeService.getInstance().registerScene(sc);
        dlg.setScene(sc);

        ReporteService task = new ReporteService();
        pb.progressProperty().bind(task.progressProperty());
        lblMsg.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            pb.progressProperty().unbind();
            lblMsg.textProperty().unbind();
            lblIcon.setText("✅");
            String archivo = task.getValue();
            lblMsg.setText("Reporte guardado: " + archivo);

            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                Platform.runLater(() -> {
                    dlg.close();
                    // Abrir PDF con la aplicación por defecto del sistema
                    try {
                        if (Desktop.isDesktopSupported())
                            Desktop.getDesktop().open(new File(archivo));
                    } catch (Exception ignored) {}
                });
            }).start();
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            pb.progressProperty().unbind();
            lblMsg.textProperty().unbind();
            lblIcon.setText("❌");
            lblMsg.setText("Error: " + task.getException().getMessage());
        }));

        new Thread(task, "reporte-thread").start();
        dlg.show();
    }

    private void actualizarDashboard() {
        root.setCenter(null);
        construir();
    }

    private Button boton(String texto, String estilo) {
        Button b = new Button(texto); b.getStyleClass().addAll("btn", estilo); return b;
    }

    public BorderPane getRoot() { return root; }
}
