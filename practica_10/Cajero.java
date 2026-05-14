public class Cajero implements Runnable {
    private final String nombre;
    private final ColaClientes fila;
    private final CuentaCompartida boveda;

    public Cajero(String nombre, ColaClientes fila, CuentaCompartida boveda) {
        this.nombre = nombre;
        this.fila = fila;
        this.boveda = boveda;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Cliente c = fila.llamarSiguiente();
            if (c == null) break;

            System.out.println("[" + nombre + "] Atendiendo al Cliente " + c.getId());
            
            try {
                Thread.sleep((long) (Math.random() * 1500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            boveda.procesarTransaccion(c.getMonto(), c.isEsDeposito(), nombre, c.getId());
        }
    }
}