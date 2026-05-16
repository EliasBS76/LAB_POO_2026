package gym.service;

import gym.util.Serializer;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tarea ejecutada en hilo secundario para generar un backup serializado sin
 * bloquear la interfaz gráfica.
 */
public class BackupService extends Task<String> {

    private final DataService dataService;

    public BackupService(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    protected String call() throws Exception {
        updateMessage("Iniciando backup…");
        updateProgress(0, 4);

        Thread.sleep(400);
        updateProgress(1, 4);
        updateMessage("Serializando clientes…");

        Thread.sleep(400);
        updateProgress(2, 4);
        updateMessage("Serializando clases y equipos…");

        Thread.sleep(400);
        updateProgress(3, 4);
        updateMessage("Escribiendo archivo…");

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "backup_" + timestamp + ".dat";

        DataService.GymData snapshot = new DataService.GymData(
                dataService.getClientes(),
                dataService.getClases(),
                dataService.getEquipos(),
                dataService.getRecompensas(),
                dataService.getPagos(),
                dataService.getAccesos()
        );
        Serializer.serializar(snapshot, filename);

        updateProgress(4, 4);
        updateMessage("Backup completado: " + filename);
        return filename;
    }
}
