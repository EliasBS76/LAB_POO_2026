package practica_05;

public class Main 
{

    public static void main(String[] args) {

        Vehiculo[] arreglo = {
            new Auto("Toyota", "Corolla", 2022, 160, 4, 50, 22.5),
            new Moto("Kawasaki", "Ninja", 2023, 200, 650, true, 24.0),
            new Camion("Volvo", "FH16", 2021, 110, 20, 6, 28.0),
            new Autobus("Mercedes", "Citaro", 2020, 90, 80, false, 26.0)
        };

        DemoPolimorfismo.correrDemo(arreglo);
        DemoPolimorfismo.demostrarSobrecargas();

        ProcesadorFlota flota = new ProcesadorFlota();
        for (Vehiculo v : arreglo) flota.agregar(v);

        flota.mostrarFlota();

        System.out.println("\n--- estadísticas de la flota ---");
        System.out.printf("  consumo promedio      : %.2f L/100km%n", flota.consumoPromedio());
        System.out.printf("  costo total viaje 500km: $%.2f%n", flota.costoTotalViaje(500));
        System.out.printf("  vehículo más rápido   : %s%n", flota.masRapido());
        System.out.printf("  vehículo más eficiente: %s%n", flota.masEficiente());
    }
}
