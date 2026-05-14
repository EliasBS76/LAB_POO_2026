public class CuentaCompartida {
    private double saldo;

    public CuentaCompartida(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public synchronized void procesarTransaccion(double monto, boolean esDeposito, String nombreCajero, int idCliente) {
        if (esDeposito) {
            saldo += monto;
            System.out.println("[" + nombreCajero + "] Deposito de $" + monto + " del Cliente " + idCliente + ". Saldo: $" + saldo);
        } else {
            while (saldo < monto) {
                System.out.println("[" + nombreCajero + "] Boveda sin fondos suficientes para retiro de Cliente " + idCliente + ". Esperando...");
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            saldo -= monto;
            System.out.println("[" + nombreCajero + "] Retiro de $" + monto + " del Cliente " + idCliente + ". Saldo: $" + saldo);
        }
        notifyAll();
    }

    public synchronized double getSaldo() {
        return saldo;
    }
}