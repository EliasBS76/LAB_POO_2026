package practica_04;
import java.util.ArrayList;
import java.util.List;

public class GestorHardware 
{

    private List<sensor> listaDispositivos;

    public GestorHardware() {
        this.listaDispositivos = new ArrayList<>();
    }

    public void agregarComponente(sensor s) {
        listaDispositivos.add(s);
    }

    public void monitorearSistema() {
        System.out.println("=== INICIANDO MONITOREO DE HARDWARE ===");
        for (sensor s : listaDispositivos) {
            
            s.actualizarLectura();
            s.Alerta();
            System.out.println("-----------------------");
        }
    }
}