package practica_06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorFlota {

    private List<Vehiculo> flota = new ArrayList<>();

    public void agregar(Vehiculo v) {
        flota.add(v);
    }

    public void mostrarResumen() {
        System.out.println("\n--- flota registrada ---");
        for (Vehiculo v : flota) {
            System.out.println("  " + v.resumen());
        }
    }

    public void mostrarEstadoMantenimiento() {
        System.out.println("\n--- estado de mantenimiento ---");
        for (Vehiculo v : flota) {
            if (v instanceof Mantenible m) {
                System.out.printf("  %-25s estado: %-40s costo estimado: $%.2f%n",
                    v.toString(), m.estadoGeneral(), m.calcularCostoMantenimiento());
            }
        }
    }

    public void mostrarUbicaciones() {
        System.out.println("\n--- vehiculos rastreables ---");
        for (Vehiculo v : flota) {
            if (v instanceof Rastreable r) {
                System.out.printf("  %-25s ubicacion: %s%n",
                    v.toString(), r.obtenerUbicacion());
            }
        }
    }

    public void mostrarEstadoBaterias() {
        System.out.println("\n--- vehiculos electricos ---");
        for (Vehiculo v : flota) {
            if (v instanceof Electrico e) {
                System.out.printf("  %-25s %s  autonomia restante: %.1f km%n",
                    v.toString(), e.alertaBateria(), e.calcularAutonomiaRestante());
            }
        }
    }

    public List<Vehiculo> conMantenimientoPendiente() {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : flota) {
            if (v instanceof Mantenible m && m.necesitaMantenimiento()) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Vehiculo> fueraDeZona(double latCentro, double lonCentro, double radioKm) {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : flota) {
            if (v instanceof Rastreable r && !r.estaEnZonaSegura(latCentro, lonCentro, radioKm)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public Vehiculo masValioso() {
        return flota.stream()
                    .max(Comparator.comparingDouble(Vehiculo::calcularValorComercial))
                    .orElse(null);
    }

    public double valorTotalFlota() {
        double total = 0;
        for (Vehiculo v : flota) total += v.calcularValorComercial();
        return total;
    }

    public double costoMantenimientoTotal() {
        double total = 0;
        for (Vehiculo v : flota) {
            if (v instanceof Mantenible m) total += m.calcularCostoMantenimiento();
        }
        return total;
    }

    public List<Vehiculo> getFlota() { return flota; }
}