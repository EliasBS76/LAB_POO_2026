package practica_06;

public interface Electrico {
    double getPorcentajeBateria();
    double calcularAutonomiaRestante();
    void cargarBateria(double porcentaje);
    boolean necesitaCarga();

    default String alertaBateria() {
        if (getPorcentajeBateria() <= 10)  return "CRITICO: bateria al " + String.format("%.0f", getPorcentajeBateria()) + "%";
        if (getPorcentajeBateria() <= 25)  return "BAJO: bateria al " + String.format("%.0f", getPorcentajeBateria()) + "%";
        return "OK: bateria al " + String.format("%.0f", getPorcentajeBateria()) + "%";
    }
}