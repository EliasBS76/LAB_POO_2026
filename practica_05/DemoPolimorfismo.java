package practica_05;

public class DemoPolimorfismo {

    public static void correrDemo(Vehiculo[] arreglo) {
        System.out.println("\n--- array polimórfico: resumen rápido ---");
        for (Vehiculo v : arreglo) {
            System.out.printf("  %-25s consumo=%.2f L/100km  vel_max=%.1f km/h%n",
                v.toString(), v.calcularConsumo(), v.calcularVelocidadMax());
        }

        System.out.println("\n--- instanceof + casting ---");
        for (Vehiculo v : arreglo) {
            if (v instanceof Auto a) {
                System.out.println("  Auto detectado: " + a.getMarca()
                    + " | autonomia=" + String.format("%.0f", a.autonomia()) + " km"
                    + " | puertas=" + a.getNumeroPuertas());

            } else if (v instanceof Moto m) {
                System.out.println("  Moto detectada: " + m.getMarca()
                    + " | cilindrada=" + m.getCilindrada() + "cc"
                    + " | sport=" + m.isEsSport());

            } else if (v instanceof Camion c) {
                System.out.println("  Camión detectado: " + c.getMarca()
                    + " | toneladas=" + c.getToneladas()
                    + " | vel con carga=" + String.format("%.1f", c.calcularVelocidadMax(c.getToneladas() * 0.8)) + " km/h");

            } else if (v instanceof Autobus b) {
                System.out.println("  Autobús detectado: " + b.getMarca()
                    + " | capacidad=" + b.getCapacidadPasajeros() + " pax"
                    + " | articulado=" + b.isEsArticulado());
            }
        }
    }

    public static void demostrarSobrecargas() {
        System.out.println("\n--- sobrecargas Auto.calcularConsumo() ---");
        Auto a = new Auto("Toyota", "Corolla", 2022, 160, 4, 50, 22.5);
        System.out.printf("  sin args              : %.2f%n", a.calcularConsumo());
        System.out.printf("  con 4 pasajeros       : %.2f%n", a.calcularConsumo(4));
        System.out.printf("  4 pax + AC            : %.2f%n", a.calcularConsumo(4, true));
        System.out.printf("  4 pax + AC + rampa 8° : %.2f%n", a.calcularConsumo(4, true, 8));

        System.out.println("\n--- sobrecargas Moto.calcularCostoViaje() ---");
        Moto m = new Moto("Kawasaki", "Ninja", 2023, 200, 650, true, 24.0);
        System.out.printf("  solo km (double 200)  : $%.2f%n", m.calcularCostoViaje(200.0));
        System.out.printf("  km (int 200)          : $%.2f%n", m.calcularCostoViaje(200));
        System.out.printf("  200km + $50 peajes    : $%.2f%n", m.calcularCostoViaje(200.0, 50));
        System.out.printf("  200km + peajes + estac: $%.2f%n", m.calcularCostoViaje(200.0, 50, 30));

        System.out.println("\n--- sobrecargas Camion.calcularVelocidadMax() ---");
        Camion c = new Camion("Volvo", "FH16", 2021, 110, 20, 6, 28.0);
        System.out.printf("  sin args              : %.1f%n", c.calcularVelocidadMax());
        System.out.printf("  vacio=true            : %.1f%n", c.calcularVelocidadMax(true));
        System.out.printf("  con carga 15t         : %.1f%n", c.calcularVelocidadMax(15));
        System.out.printf("  15t subiendo rampa    : %.1f%n", c.calcularVelocidadMax(15, true));

        System.out.println("\n--- sobrecargas Autobus.calcularConsumo() ---");
        Autobus b = new Autobus("Mercedes", "Citaro", 2020, 90, 80, false, 26.0);
        System.out.printf("  sin args              : %.2f%n", b.calcularConsumo());
        System.out.printf("  40 pasajeros          : %.2f%n", b.calcularConsumo(40));
        System.out.printf("  40 pax + tráfico alto : %.2f%n", b.calcularConsumo(40, true));
        System.out.printf("  40 pax + tráfico + 3 paradas/km: %.2f%n", b.calcularConsumo(40, true, 3));
    }
}
