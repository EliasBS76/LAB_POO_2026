package practica_05;

public class Auto extends Vehiculo {

    private int numeroPuertas;
    private double litrosTanque;
    private double precioCombustible;

    public Auto(String marca, String modelo, int anio, double velocidadBase,
                int numeroPuertas, double litrosTanque, double precioCombustible) {
        super(marca, modelo, anio, velocidadBase);
        this.numeroPuertas = numeroPuertas;
        this.litrosTanque = litrosTanque;
        this.precioCombustible = precioCombustible;
    }

    @Override
    public double calcularConsumo() {
        return 8.5 + (numeroPuertas * 0.3);
    }

    @Override
    public double calcularCostoViaje(double km) {
        return (calcularConsumo() / 100) * km * precioCombustible;
    }

    @Override
    public double calcularVelocidadMax() {
        return velocidadBase * 1.2;
    }

    @Override
    public void acelerar(double factor) {
        velocidadBase *= factor;
    }

    @Override
    public String tipoCombustible() {
        return "gasolina";
    }

    public double calcularConsumo(double cargaPasajeros) {
        return calcularConsumo() + (cargaPasajeros * 0.05);
    }

    public double calcularConsumo(double cargaPasajeros, boolean aireEncendido) {
        double base = calcularConsumo(cargaPasajeros);
        return aireEncendido ? base + 1.2 : base;
    }

    public double calcularConsumo(double cargaPasajeros, boolean aireEncendido, double gradiente) {
        double base = calcularConsumo(cargaPasajeros, aireEncendido);
        return base + (gradiente * 0.1);
    }

    public double autonomia() {
        return (litrosTanque / calcularConsumo()) * 100;
    }

    public int getNumeroPuertas() { return numeroPuertas; }
    public double getLitrosTanque() { return litrosTanque; }
}
