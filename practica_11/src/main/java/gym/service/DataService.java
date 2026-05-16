package gym.service;

import gym.model.*;
import gym.util.Serializer;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private static DataService instance;
    private static final String DATA_FILE = "gym_data.dat";

    private List<Cliente>       clientes    = new ArrayList<>();
    private List<ClaseGrupal>   clases      = new ArrayList<>();
    private List<Equipo>        equipos     = new ArrayList<>();
    private List<Recompensa>    recompensas = new ArrayList<>();
    private List<Pago>          pagos       = new ArrayList<>();
    private List<RegistroAcceso> accesos    = new ArrayList<>();

    private DataService() {}

    public static DataService getInstance() {
        if (instance == null) instance = new DataService();
        return instance;
    }

    

    public void cargarDatos() {
        try {
            GymData data = (GymData) Serializer.deserializar(DATA_FILE);
            clientes    = new ArrayList<>(data.clientes());
            clases      = new ArrayList<>(data.clases());
            equipos     = new ArrayList<>(data.equipos());
            recompensas = new ArrayList<>(data.recompensas());
            pagos       = new ArrayList<>(data.pagos());
            accesos     = new ArrayList<>(data.accesos());
            actualizarContadores();
        } catch (Exception e) {
            cargarDatosEjemplo();
        }
    }

    public void guardarDatos() {
        try {
            Serializer.serializar(
                new GymData(clientes, clases, equipos, recompensas, pagos, accesos),
                DATA_FILE
            );
        } catch (Exception e) {
            System.err.println("Error guardando datos: " + e.getMessage());
        }
    }

    private void actualizarContadores() {
        clientes.stream().mapToInt(Cliente::getId).max()
                .ifPresent(max -> Cliente.setNextId(max + 1));

        int maxMem = clientes.stream().flatMap(c -> c.getMembresias().stream())
                .mapToInt(Membresia::getId).max().orElse(0);
        if (maxMem > 0) Membresia.setNextId(maxMem + 1);

        clases.stream().mapToInt(ClaseGrupal::getId).max()
              .ifPresent(max -> ClaseGrupal.setNextId(max + 1));

        equipos.stream().mapToInt(Equipo::getId).max()
               .ifPresent(max -> Equipo.setNextId(max + 1));

        pagos.stream().mapToInt(Pago::getId).max()
             .ifPresent(max -> Pago.setNextId(max + 1));

        accesos.stream().mapToInt(RegistroAcceso::getId).max()
               .ifPresent(max -> RegistroAcceso.setNextId(max + 1));
    }

    private void cargarDatosEjemplo() {
       
        Cliente c1 = new Cliente("Juan García",   "juan@email.com",   "555-1001");
        Cliente c2 = new Cliente("María López",   "maria@email.com",  "555-1002");
        Cliente c3 = new Cliente("Carlos Ruiz",   "carlos@email.com", "555-1003");
        Cliente c4 = new Cliente("Ana Martínez",  "ana@email.com",    "555-1004");

        Membresia m1 = new Membresia(c1, Plan.PREMIUM, true);
        c1.getMembresias().add(m1); c1.agregarPuntos(150);

        Membresia m2 = new Membresia(c2, Plan.BASICO, false);
        c2.getMembresias().add(m2); c2.agregarPuntos(30);

        Membresia m3 = new Membresia(c3, Plan.VIP, true);
        c3.getMembresias().add(m3); c3.agregarPuntos(320);

        Membresia m4 = new Membresia(c4, Plan.ESTANDAR, true);
        c4.getMembresias().add(m4); c4.agregarPuntos(75);

        clientes.addAll(List.of(c1, c2, c3, c4));

        
        clases.add(new ClaseGrupal("Yoga",     "Laura Torres",  DayOfWeek.MONDAY,    LocalTime.of(9,  0), 12));
        clases.add(new ClaseGrupal("Spinning", "Pedro Sánchez", DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), 20));
        clases.add(new ClaseGrupal("Pilates",  "Sofía Ramos",   DayOfWeek.FRIDAY,    LocalTime.of(10, 0), 15));
        clases.add(new ClaseGrupal("Zumba",    "Diego Morales", DayOfWeek.TUESDAY,   LocalTime.of(19, 0), 25));

        
        Equipo e1 = new Equipo("Bicicletas estáticas", "Cardio",  10);
        Equipo e2 = new Equipo("Mancuernas (set)",     "Fuerza",  20);
        Equipo e3 = new Equipo("Caminadoras",          "Cardio",   5);
        e3.setEstado(EstadoEquipo.EN_MANTENIMIENTO);
        Equipo e4 = new Equipo("Barras olímpicas",     "Fuerza",   8);
        equipos.addAll(List.of(e1, e2, e3, e4));

        
        recompensas.add(new Recompensa("Clase grupal gratis",   100));
        recompensas.add(new Recompensa("Mes adicional gratis",  500));
        recompensas.add(new Recompensa("Botella personalizada", 200));
        recompensas.add(new Recompensa("Toalla del gimnasio",    80));

        
        Pago p1 = new Pago(c1, Plan.PREMIUM, Plan.PREMIUM.getPrecioFinal(), "TARJETA");
        p1.setEstado(EstadoPago.COMPLETADO);
        Pago p2 = new Pago(c3, Plan.VIP, Plan.VIP.getPrecioFinal(), "TRANSFERENCIA");
        p2.setEstado(EstadoPago.COMPLETADO);
        pagos.addAll(List.of(p1, p2));

        // Accesos de ejemplo
        accesos.add(new RegistroAcceso(c1, TipoAcceso.ENTRADA));
        accesos.add(new RegistroAcceso(c2, TipoAcceso.ENTRADA));
        accesos.add(new RegistroAcceso(c2, TipoAcceso.SALIDA));
    }

    

    public List<Cliente>        getClientes()    { return clientes; }
    public List<ClaseGrupal>    getClases()      { return clases; }
    public List<Equipo>         getEquipos()     { return equipos; }
    public List<Recompensa>     getRecompensas() { return recompensas; }
    public List<Pago>           getPagos()       { return pagos; }
    public List<RegistroAcceso> getAccesos()     { return accesos; }

    public void agregarCliente(Cliente c)        { clientes.add(c); }
    public void eliminarCliente(Cliente c)       { clientes.remove(c); }

    public void agregarClase(ClaseGrupal c)      { clases.add(c); }
    public void eliminarClase(ClaseGrupal c)     { clases.remove(c); }

    public void agregarEquipo(Equipo e)          { equipos.add(e); }
    public void eliminarEquipo(Equipo e)         { equipos.remove(e); }

    public void agregarPago(Pago p)              { pagos.add(p); }

    public void agregarAcceso(RegistroAcceso r)  { accesos.add(r); }

    public long clientesEnGimnasio() {
        
        return clientes.stream().filter(c -> {
            List<RegistroAcceso> suyos = accesos.stream()
                    .filter(a -> a.getCliente().getId() == c.getId() && a.esDeHoy())
                    .toList();
            return !suyos.isEmpty() &&
                   suyos.get(suyos.size() - 1).getTipo() == TipoAcceso.ENTRADA;
        }).count();
    }

  

    public record GymData(
        List<Cliente>       clientes,
        List<ClaseGrupal>   clases,
        List<Equipo>        equipos,
        List<Recompensa>    recompensas,
        List<Pago>          pagos,
        List<RegistroAcceso> accesos
    ) implements java.io.Serializable {
        private static final long serialVersionUID = 2L;
    }
}
