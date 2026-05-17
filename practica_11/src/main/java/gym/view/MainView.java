package gym.view;

import gym.service.BackupService;
import gym.service.DataService;
import gym.service.NotificacionService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

import java.util.List;

public class MainView {

    private final Stage stage;
    private final BorderPane root;

    public MainView(Stage stage) {
        this.stage = stage;
        this.root  = new BorderPane();
        root.getStyleClass().add("main-root");
        root.setTop(buildHeader());
        root.setCenter(buildTabs());
    }

    // ── cabecera ─────────────────────────────────────────────────────────

    private VBox buildHeader() {
        VBox header = new VBox();
        header.getStyleClass().add("app-header");

        // Barra de menú
        MenuBar menuBar = new MenuBar();

        Menu menuArchivo = new Menu("_Archivo");
        MenuItem miGuardar = new MenuItem("Guardar datos");
        MenuItem miBackup  = new MenuItem("Crear backup…");
        MenuItem miSalir   = new MenuItem("Salir");

        miGuardar.setOnAction(e -> {
            DataService.getInstance().guardarDatos();
            info("Datos guardados correctamente.");
        });
        miBackup.setOnAction(e -> mostrarDialogoBackup());
        miSalir.setOnAction(e  -> { DataService.getInstance().guardarDatos(); stage.close(); });

        menuArchivo.getItems().addAll(miGuardar, new SeparatorMenuItem(), miBackup, new SeparatorMenuItem(), miSalir);

        Menu menuAyuda = new Menu("_Ayuda");
        MenuItem miAcerca = new MenuItem("Acerca de GymManager Pro");
        miAcerca.setOnAction(e -> info(
                "GymManager Pro v1.0\n" +
                "Sistema de Gestión de Gimnasio\n\n" +
                "Desarrollado con JavaFX — Patrón MVC\n" +
                "POO 2026"));
        menuAyuda.getItems().add(miAcerca);
        menuBar.getMenus().addAll(menuArchivo, menuAyuda);

        // Barra de título
        HBox titleBar = new HBox(12);
        titleBar.setPadding(new Insets(14, 22, 14, 22));
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.getStyleClass().add("title-bar");

        Label icon = new Label("🏋️");
        icon.setStyle("-fx-font-size: 26px;");

        VBox titleText = new VBox(2);
        Label appName = new Label("GymManager Pro");
        appName.getStyleClass().add("app-title");
        Label appSub = new Label("Sistema de Gestión de Gimnasio");
        appSub.getStyleClass().add("app-subtitle");
        titleText.getChildren().addAll(appName, appSub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botón de notificaciones
        int notifN = NotificacionService.getInstance().getCantidad();
        Button btnNotif = new Button("Notificaciones" + (notifN > 0 ? "  (" + notifN + ")" : ""));
        btnNotif.getStyleClass().addAll("btn", notifN > 0 ? "btn-warning" : "btn-secondary");
        btnNotif.setOnAction(e -> mostrarNotificaciones());

        titleBar.getChildren().addAll(icon, titleText, spacer, btnNotif);
        header.getChildren().addAll(menuBar, titleBar);
        return header;
    }

    // ── pestañas ─────────────────────────────────────────────────────────

    private TabPane buildTabs() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tabs");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
            new Tab("Dashboard",         new ReporteView(stage).getRoot()),
            new Tab("Clientes",          new ClienteView(stage).getRoot()),
            new Tab("Membresías",        new MembresiaView(stage).getRoot()),
            new Tab("Clases Grupales",   new ClaseGrupalView(stage).getRoot()),
            new Tab("Inventario",        new EquipoView(stage).getRoot()),
            new Tab("Pagos",             new PagoView(stage).getRoot()),
            new Tab("Accesos",           new AccesoView(stage).getRoot())
        );
        return tabPane;
    }

    // ── notificaciones ───────────────────────────────────────────────────

    private void mostrarNotificaciones() {
        List<String> notifs = NotificacionService.getInstance().getNotificaciones();
        if (notifs.isEmpty()) { info("No hay notificaciones pendientes."); return; }

        StringBuilder sb = new StringBuilder();
        for (String n : notifs) sb.append("• ").append(n).append("\n");

        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false); ta.setWrapText(true); ta.setPrefHeight(280);

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Notificaciones del sistema");
        a.setHeaderText(notifs.size() + " notificación(es)");
        a.getDialogPane().setContent(ta);
        a.initOwner(stage);
        a.showAndWait();
    }

    // ── backup (multithreading) ───────────────────────────────────────────

    private void mostrarDialogoBackup() {
        Stage dlgStage = new Stage();
        dlgStage.initOwner(stage);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.setTitle("Backup en progreso");
        dlgStage.setResizable(false);

        ProgressBar pb  = new ProgressBar(0);  pb.setPrefWidth(320);
        Label lblMsg    = new Label("Iniciando…");
        lblMsg.getStyleClass().add("info-label");

        VBox content = new VBox(15, new Label("Creando copia de seguridad…"), pb, lblMsg);
        content.setPadding(new Insets(25)); content.setAlignment(Pos.CENTER);

        Scene scene = new Scene(content, 400, 160);
        try { scene.getStylesheets().add(getClass().getResource("/gym/styles.css").toExternalForm()); }
        catch (Exception ignored) {}
        dlgStage.setScene(scene);

        BackupService task = new BackupService(DataService.getInstance());
        pb.progressProperty().bind(task.progressProperty());
        lblMsg.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            lblMsg.textProperty().unbind();
            lblMsg.setText("Completado: " + task.getValue());
            new Thread(() -> {
                try { Thread.sleep(1800); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                Platform.runLater(dlgStage::close);
            }).start();
        });

        task.setOnFailed(e -> {
            lblMsg.textProperty().unbind();
            lblMsg.setText("Error: " + task.getException().getMessage());
        });

        new Thread(task, "backup-thread").start();
        dlgStage.show();
    }

    private void info(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null); a.initOwner(stage); a.showAndWait();
    }

    public BorderPane getRoot() { return root; }
}
