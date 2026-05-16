package gym;

import gym.service.DataService;
import gym.service.NotificacionService;
import gym.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GymApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DataService.getInstance().cargarDatos();
        NotificacionService.getInstance().verificarVencimientos();

        MainView mainView = new MainView(primaryStage);
        Scene scene = new Scene(mainView.getRoot(), 1200, 720);
        scene.getStylesheets().add(
            getClass().getResource("/gym/styles.css").toExternalForm()
        );

        primaryStage.setTitle("GymManager Pro");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> DataService.getInstance().guardarDatos());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
