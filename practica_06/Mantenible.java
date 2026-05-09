
package practica_06;

public interface Mantenible {
    boolean necesitaMantenimiento();
    double calcularCostoMantenimiento();
    void registrarMantenimiento(int kilometraje);
    String estadoGeneral();
}
