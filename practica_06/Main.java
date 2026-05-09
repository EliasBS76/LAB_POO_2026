package practica_06;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Auto auto1 = new Auto("Toyota", "Corolla", 2020, "ABC-123", 4);
        Auto auto2 = new Auto("Honda", "Civic", 2018, "DEF-456", 4);
        Moto moto1 = new Moto("Kawasaki", "Ninja", 2022, "GHI-789", 650, true);
        Moto moto2 = new Moto("Honda", "CB500", 2019, "JKL-012", 500, false);
        Camion camion1 = new Camion("Volvo", "FH16", 2021, "MNO-345", 20, 6);
        AutoElectrico electrico1 = new AutoElectrico("Tesla", "Model 3", 2023, "PQR-678", 75, 500);
        AutoElectrico electrico2 = new AutoElectrico("BYD", "Seal", 2024, "STU-901", 82, 550);

        auto1.agregarKilometros(12500);
        auto2.agregarKilometros(9800);
        moto1.agregarKilometros(7200);
        moto2.agregarKilometros(3000);
        camion1.agregarKilometros(18000);
        electrico1.agregarKilometros(5000);
        electrico2.agregarKilometros(1200);

        auto1.actualizarUbicacion(25.6866, -100.3161);
        auto2.actualizarUbicacion(25.7200, -100.2800);
        camion1.actualizarUbicacion(25.4010, -100.9800);
        electrico1.actualizarUbicacion(25.6500, -100.4000);
        electrico2.actualizarUbicacion(25.7100, -100.3500);

        electrico1.cargarBateria(-75);
        electrico2.cargarBateria(-85);

        GestorFlota gestor = new GestorFlota();
        gestor.agregar(auto1);
        gestor.agregar(auto2);
        gestor.agregar(moto1);
        gestor.agregar(moto2);
        gestor.agregar(camion1);
        gestor.agregar(electrico1);
        gestor.agregar(electrico2);

        gestor.mostrarResumen();
        gestor.mostrarEstadoMantenimiento();
        gestor.mostrarUbicaciones();
        gestor.mostrarEstadoBaterias();

        System.out.println("\n--- vehiculos con mantenimiento pendiente ---");
        List<Vehiculo> pendientes = gestor.conMantenimientoPendiente();
        if (pendientes.isEmpty()) {
            System.out.println("  ninguno por ahora");
        } else {
            for (Vehiculo v : pendientes) System.out.println("  " + v);
        }

        double latBase = 25.6866;
        double lonBase = -100.3161;
        double radioKm = 50;
        System.out.println("\n--- vehiculos fuera de zona (radio " + radioKm + " km) ---");
        List<Vehiculo> fuera = gestor.fueraDeZona(latBase, lonBase, radioKm);
        if (fuera.isEmpty()) {
            System.out.println("  todos dentro del radio");
        } else {
            for (Vehiculo v : fuera) System.out.println("  " + v);
        }

        System.out.println("\n--- financiero ---");
        System.out.printf("  valor total flota        : $%.2f%n", gestor.valorTotalFlota());
        System.out.printf("  mantenimiento total      : $%.2f%n", gestor.costoMantenimientoTotal());
        System.out.printf("  vehiculo mas valioso     : %s ($%.2f)%n",
            gestor.masValioso(), gestor.masValioso().calcularValorComercial());

        System.out.println("\n--- uso directo de interfaces ---");
        IMantenible mMoto = moto1;
        System.out.println("  moto1 como IMantenible   : " + mMoto.estadoGeneral());

        IRastreable rAuto = auto1;
        System.out.printf("  auto1 como IRastreable   : distancia a centro = %.2f km%n",
            rAuto.calcularDistanciaA(latBase, lonBase));

        IElectrico eAuto = electrico1;
        System.out.println("  electrico1 como IElectrico: " + eAuto.alertaBateria());
        System.out.printf("  autonomia restante       : %.1f km%n", eAuto.calcularAutonomiaRestante());
    }
}