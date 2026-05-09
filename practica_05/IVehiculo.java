package practica5v;

public interface IVehiculo {
    double calcularConsumo();
    double calcularCostoViaje(double km);
    double calcularVelocidadMax();
    void acelerar(double factor);
}
