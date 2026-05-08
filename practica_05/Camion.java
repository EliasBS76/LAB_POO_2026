package practica5v;

public class Camion extends Vehiculo {

    private double toneladas;
    private int ejes;
    private double precioDiesel;

    public Camion(String marca, String modelo, int anio, double velocidadBase,
                  double toneladas, int ejes, double precioDiesel) {
        super(marca, modelo, anio, velocidadBase);
        this.toneladas = toneladas;
        this.ejes = ejes;
        this.precioDiesel = precioDiesel;
    }

    @Override
    public double calcularConsumo() {
        return 25.0 + (toneladas * 1.8) + (ejes * 0.5);
    }

    @Override
    public double calcularCostoViaje(double km) {
        return (calcularConsumo() / 100) * km * precioDiesel;
    }

    @Override
    public double calcularVelocidadMax() {
        return velocidadBase - (toneladas * 2);
    }

    @Override
    public void acelerar(double factor) {
        velocidadBase *= Math.min(factor, 1.1);
    }

    @Override
    public String tipoCombustible() {
        return "diesel";
    }

    public double calcularVelocidadMax(double cargaActual) {
        double reduccion = (cargaActual / toneladas) * 10;
        return calcularVelocidadMax() - reduccion;
    }

    public double calcularVelocidadMax(double cargaActual, boolean subeRampa) {
        double vel = calcularVelocidadMax(cargaActual);
        return subeRampa ? vel * 0.6 : vel;
    }

    public double calcularVelocidadMax(boolean vacio) {
        return vacio ? velocidadBase * 0.95 : calcularVelocidadMax();
    }

    public double pesoTotal(double cargaActual) {
        return toneladas + cargaActual;
    }

    public double getToneladas() { return toneladas; }
    public int getEjes() { return ejes; }
}
