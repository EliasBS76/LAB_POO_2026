package practica5v;

public abstract class Vehiculo implements IVehiculo {

    protected String marca;
    protected String modelo;
    protected int anio;
    protected double velocidadBase;

    public Vehiculo(String marca, String modelo, int anio, double velocidadBase) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.velocidadBase = velocidadBase;
    }

    public abstract double calcularConsumo();
    public abstract double calcularVelocidadMax();
    public abstract String tipoCombustible();

    public String fichaCompleta() {
        return marca + " " + modelo + " (" + anio + ")"
            + "\n  tipo combustible : " + tipoCombustible()
            + "\n  consumo (L/100km): " + String.format("%.2f", calcularConsumo())
            + "\n  vel. maxima (km/h): " + String.format("%.1f", calcularVelocidadMax())
            + "\n  costo viaje 100km: $" + String.format("%.2f", calcularCostoViaje(100));
    }

    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public double getVelocidadBase() { return velocidadBase; }

    @Override
    public String toString() {
        return marca + " " + modelo + " " + anio;
    }
}
