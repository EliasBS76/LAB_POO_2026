package practica_05;

public class Moto extends Vehiculo {

    private int cilindrada;
    private boolean esSport;
    private double precioCombustible;

    public Moto(String marca, String modelo, int anio, double velocidadBase,
                int cilindrada, boolean esSport, double precioCombustible) {
        super(marca, modelo, anio, velocidadBase);
        this.cilindrada = cilindrada;
        this.esSport = esSport;
        this.precioCombustible = precioCombustible;
    }

    @Override
    public double calcularConsumo() {
        double base = cilindrada / 100.0 * 0.9;
        return esSport ? base * 1.3 : base;
    }

    @Override
    public double calcularCostoViaje(double km) {
        return (calcularConsumo() / 100) * km * precioCombustible;
    }

    @Override
    public double calcularVelocidadMax() {
        return esSport ? velocidadBase * 1.5 : velocidadBase * 1.1;
    }

    @Override
    public void acelerar(double factor) {
        velocidadBase *= factor;
    }

    @Override
    public String tipoCombustible() {
        return "gasolina premium";
    }

    public double calcularCostoViaje(double km, double peajes) {
        return calcularCostoViaje(km) + peajes;
    }

    public double calcularCostoViaje(double km, double peajes, double estacionamiento) {
        return calcularCostoViaje(km, peajes) + estacionamiento;
    }

    public double calcularCostoViaje(int km) {
        return calcularCostoViaje((double) km) * 1.05;
    }

    public boolean puedeRebasar(Vehiculo otro) {
        return calcularVelocidadMax() > otro.calcularVelocidadMax();
    }

    public int getCilindrada() { return cilindrada; }
    public boolean isEsSport() { return esSport; }
}
