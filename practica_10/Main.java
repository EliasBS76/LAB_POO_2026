
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ColaClientes fila = new ColaClientes(5);
        CuentaCompartida boveda = new CuentaCompartida(2000.0);

        System.out.println("=== ABRIENDO SUCURSAL BANCARIA ===");
        System.out.println("Fondo inicial en boveda: $" + boveda.getSaldo() + "\n");

        GeneradorClientes puerta = new GeneradorClientes(fila, 15);
        
        ExecutorService poolCajeros = Executors.newFixedThreadPool(3);

        puerta.start();

        poolCajeros.execute(new Cajero("Ventanilla 1", fila, boveda));
        poolCajeros.execute(new Cajero("Ventanilla 2", fila, boveda));
        poolCajeros.execute(new Cajero("Ventanilla 3", fila, boveda));

        try {
            puerta.join();
            Thread.sleep(3000); 
            poolCajeros.shutdownNow();
            poolCajeros.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== CERRANDO SUCURSAL ===");
        System.out.println("Fondo final en boveda: $" + boveda.getSaldo());
    }
}