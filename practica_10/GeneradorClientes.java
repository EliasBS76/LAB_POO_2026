public class GeneradorClientes extends Thread {
    private final ColaClientes fila;
    private final int totalClientes;

    public GeneradorClientes(ColaClientes fila, int totalClientes) {
        this.fila = fila;
        this.totalClientes = totalClientes;
    }

    @Override
    public void run() {
        for (int i = 1; i <= totalClientes; i++) {
            boolean esDeposito = Math.random() > 0.4;
            double monto = Math.round((Math.random() * 500 + 50) * 100.0) / 100.0;
            Cliente c = new Cliente(i, monto, esDeposito);
            
            fila.agregarCliente(c);
            
            try {
                Thread.sleep((long) (Math.random() * 800));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[Puerta] Ya no hay mas clientes por hoy.");
    }
}