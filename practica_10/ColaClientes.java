import java.util.LinkedList;
import java.util.Queue;

public class ColaClientes {
    private final Queue<Cliente> cola = new LinkedList<>();
    private final int capacidadMax;

    public ColaClientes(int capacidadMax) {
        this.capacidadMax = capacidadMax;
    }

    public synchronized void agregarCliente(Cliente cliente) {
        while (cola.size() == capacidadMax) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        cola.add(cliente);
        System.out.println("[Fila] Cliente " + cliente.getId() + " entro a la fila.");
        notifyAll();
    }

    public synchronized Cliente llamarSiguiente() {
        while (cola.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Cliente c = cola.poll();
        notifyAll();
        return c;
    }
}