package practica5v;

public class Autobus extends Vehiculo {

    private int capacidadPasajeros;
    private boolean esArticulado;
    private double precioDiesel;

    public Autobus(String marca, String modelo, int anio, double velocidadBase,
                   int capacidadPasajeros, boolean esArticulado, double precioDiesel) {
        super(marca, modelo, anio, velocidadBase);
        this.capacidadPasajeros = capacidadPasajeros;
        this.esArticulado = esArticulado;
        this.precioDiesel = precioDiesel;
    }

    @Override
    public double calcularConsumo() {
        double base = 18.0 + (capacidadPasajeros * 0.08);
        return esArticulado ? base * 1.25 : base;
    }

    @Override
    public double calcularCostoViaje(double km) {
        return (calcularConsumo() / 100) * km * precioDiesel;
    }

    @Override
    public double calcularVelocidadMax() {
        return esArticulado ? velocidadBase * 0.85 : velocidadBase;
    }

    @Override
    public void acelerar(double factor) {
        velocidadBase *= Math.min(factor, 1.15);
    }

    @Override
    public String tipoCombustible() {
        return esArticulado ? "diesel (articulado)" : "diesel";
    }

    public double calcularConsumo(int pasajerosActuales) {
        double ocupacion = (double) pasajerosActuales / capacidadPasajeros;
        return calcularConsumo() * (1 + ocupacion * 0.15);
    }

    public double calcularConsumo(int pasajerosActuales, boolean traficoAlto) {
        double base = calcularConsumo(pasajerosActuales);
        return traficoAlto ? base * 1.3 : base;
    }

    public double calcularConsumo(int pasajerosActuales, boolean traficoAlto, int paradasPorKm) {
        double base = calcularConsumo(pasajerosActuales, traficoAlto);
        return base + (paradasPorKm * 0.4);
    }

    public double costoPromedioPorPasajero(double km, int pasajeros) {
        return calcularCostoViaje(km) / Math.max(pasajeros, 1);
    }

    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public boolean isEsArticulado() { return esArticulado; }
}
