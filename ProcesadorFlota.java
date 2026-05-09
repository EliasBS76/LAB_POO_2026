package practica5v;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProcesadorFlota {

    private List<Vehiculo> flota = new ArrayList<>();

    public void agregar(Vehiculo v) {
        flota.add(v);
    }

    public void mostrarFlota() {
        System.out.println("\n--- flota completa ---");
        for (Vehiculo v : flota) {
            System.out.println("\n" + v.fichaCompleta());
        }
    }

    public double consumoPromedio() {
        double total = 0;
        for (Vehiculo v : flota) total += v.calcularConsumo();
        return total / flota.size();
    }

    public double costoTotalViaje(double km) {
        double total = 0;
        for (Vehiculo v : flota) total += v.calcularCostoViaje(km);
        return total;
    }

    public Vehiculo masRapido() {
        return flota.stream()
                    .max(Comparator.comparingDouble(Vehiculo::calcularVelocidadMax))
                    .orElse(null);
    }

    public Vehiculo masEficiente() {
        return flota.stream()
                    .min(Comparator.comparingDouble(Vehiculo::calcularConsumo))
                    .orElse(null);
    }

    public void escalarTodos(double factor) {
        for (Vehiculo v : flota) v.acelerar(factor);
    }

    public List<Vehiculo> getFlota() { return flota; }
}
