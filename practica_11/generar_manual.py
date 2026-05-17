from fpdf import FPDF
from fpdf.enums import XPos, YPos

class PDF(FPDF):
    def header(self):
        if self.page_no() == 1:
            return
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150, 150, 150)
        self.cell(0, 6, "Manual Tecnico - GymManager Pro", new_x=XPos.LMARGIN, new_y=YPos.NEXT, align="L")
        self.set_draw_color(200, 200, 200)
        self.line(self.l_margin, self.get_y(), self.w - self.r_margin, self.get_y())
        self.ln(2)

    def footer(self):
        if self.page_no() == 1:
            return
        self.set_y(-12)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150, 150, 150)
        self.cell(0, 6, f"Pagina {self.page_no() - 1}", align="C")

    def title_page(self):
        self.add_page()
        self.set_fill_color(30, 30, 30)
        self.rect(0, 0, self.w, self.h, "F")

        self.set_y(60)
        self.set_font("Helvetica", "B", 32)
        self.set_text_color(255, 255, 255)
        self.cell(0, 14, "GymManager Pro", align="C", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

        self.set_font("Helvetica", "", 16)
        self.set_text_color(180, 180, 180)
        self.cell(0, 10, "Manual Tecnico", align="C", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

        self.ln(10)
        self.set_draw_color(80, 80, 80)
        self.line(self.l_margin + 30, self.get_y(), self.w - self.r_margin - 30, self.get_y())
        self.ln(10)

        self.set_font("Helvetica", "", 12)
        self.set_text_color(200, 200, 200)
        rows = [
            ("Proyecto:", "PIA - Programacion Orientada a Objetos"),
            ("Tecnologia:", "Java 21 + JavaFX 21 + Maven"),
            ("Autor:", "Elias Jacob Bernal"),
            ("Fecha:", "Mayo 2026"),
        ]
        for label, value in rows:
            self.set_font("Helvetica", "B", 11)
            self.set_text_color(160, 160, 160)
            self.cell(45, 8, label, new_x=XPos.RIGHT, new_y=YPos.TOP)
            self.set_font("Helvetica", "", 11)
            self.set_text_color(220, 220, 220)
            self.cell(0, 8, value, new_x=XPos.LMARGIN, new_y=YPos.NEXT)

    def section_title(self, text):
        self.ln(4)
        self.set_fill_color(240, 240, 240)
        self.set_draw_color(200, 200, 200)
        self.set_font("Helvetica", "B", 13)
        self.set_text_color(30, 30, 30)
        self.cell(0, 9, text, fill=True, border="B", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.ln(3)

    def sub_title(self, text):
        self.ln(2)
        self.set_font("Helvetica", "B", 11)
        self.set_text_color(50, 50, 50)
        self.cell(0, 7, text, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.ln(1)

    def body(self, text):
        self.set_font("Helvetica", "", 10)
        self.set_text_color(50, 50, 50)
        self.multi_cell(0, 6, text)
        self.ln(1)

    def code_block(self, lines):
        self.set_fill_color(245, 245, 245)
        self.set_draw_color(210, 210, 210)
        self.set_font("Courier", "", 8)
        self.set_text_color(40, 40, 40)
        x0 = self.get_x()
        y0 = self.get_y()
        # draw block background then text
        block_h = len(lines) * 5 + 4
        self.rect(self.l_margin, y0, self.w - self.l_margin - self.r_margin, block_h, "FD")
        self.set_xy(x0 + 3, y0 + 2)
        for line in lines:
            self.cell(0, 5, line, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.ln(3)

    def table(self, headers, rows, col_widths):
        self.set_font("Helvetica", "B", 9)
        self.set_fill_color(60, 60, 60)
        self.set_text_color(255, 255, 255)
        for i, h in enumerate(headers):
            self.cell(col_widths[i], 7, h, border=1, fill=True, align="C")
        self.ln()
        self.set_font("Helvetica", "", 9)
        fill = False
        for row in rows:
            self.set_fill_color(248, 248, 248) if fill else self.set_fill_color(255, 255, 255)
            self.set_text_color(40, 40, 40)
            for i, cell in enumerate(row):
                self.cell(col_widths[i], 6, cell, border=1, fill=True)
            self.ln()
            fill = not fill
        self.ln(3)

# -- build ----------------------------------------------------------------------
pdf = PDF()
pdf.set_auto_page_break(auto=True, margin=18)
pdf.set_margins(18, 18, 18)

# -- Portada --------------------------------------------------------------------
pdf.title_page()

# -- Pagina 2: Descripcion general ---------------------------------------------
pdf.add_page()
pdf.section_title("1. Descripcion general del sistema")
pdf.body(
    "GymManager Pro es una aplicacion de escritorio para la administracion de un gimnasio. "
    "Permite gestionar clientes, membresias, clases grupales, equipos, pagos y accesos de "
    "entrada/salida, todo desde una interfaz grafica con modo claro y oscuro."
)
pdf.ln(2)
pdf.table(
    ["Modulo", "Descripcion"],
    [
        ["Clientes",        "Alta, baja, modificacion y busqueda de miembros"],
        ["Membresias",      "Asignacion de planes, renovacion automatica"],
        ["Clases grupales", "Horarios, instructores, inscripcion de clientes"],
        ["Equipos",         "Inventario con estado de mantenimiento"],
        ["Pagos",           "Registro de cobros con diferentes metodos"],
        ["Accesos",         "Control de entrada y salida en tiempo real"],
        ["Reportes",        "Generacion de PDF con resumen del sistema"],
    ],
    [45, 135],
)
pdf.sub_title("Tecnologias utilizadas")
pdf.body(
    "- Java 21 con records, streams y var\n"
    "- JavaFX 21 para la interfaz grafica\n"
    "- Maven como sistema de construccion\n"
    "- Serializacion Java para persistencia local\n"
    "- iText para generacion de PDF"
)

# -- Pagina 3: Arquitectura MVC -------------------------------------------------
pdf.add_page()
pdf.section_title("2. Arquitectura del proyecto (patron MVC)")
pdf.body(
    "El sistema sigue el patron MVC (Model-View-Controller), donde cada capa tiene "
    "una responsabilidad especifica y no se mezclan entre si."
)
pdf.code_block([
    "+--------------------------------------------------+",
    "|                     VIEW                         |",
    "|  ClienteView, MembresiaView, EquipoView, ...     |",
    "|  Construyen la UI. No contienen logica.          |",
    "+--------------------+-----------------------------+",
    "                     | eventos de usuario           ",
    "                     v                              ",
    "+--------------------------------------------------+",
    "|                  CONTROLLER                      |",
    "|  ClienteController, AccesoController, ...        |",
    "|  Validan datos, coordinan modelo y servicio.     |",
    "+------------+-------------------------------------+",
    "             | consulta / modifica                  ",
    "             v                                      ",
    "+--------------------------------------------------+",
    "|             MODEL  +  SERVICE                    |",
    "|  Cliente, Membresia, Pago, Equipo...             |",
    "|  DataService, ThemeService, NotificacionService  |",
    "+--------------------------------------------------+",
])
pdf.body(
    "Esta separacion garantiza que:\n"
    "- Los cambios en la UI no afectan la logica de negocio.\n"
    "- La logica puede probarse sin necesidad de la interfaz grafica.\n"
    "- Cada clase tiene una unica razon de cambio (SRP)."
)

# -- Pagina 4: Diagrama de paquetes --------------------------------------------
pdf.add_page()
pdf.section_title("3. Diagrama de paquetes")
pdf.code_block([
    "gym/",
    "  Main.java            <- punto de entrada (no extiende Application)",
    "  GymApp.java          <- Application de JavaFX",
    "",
    "  model/               <- Entidades del dominio",
    "    Cliente.java",
    "    Membresia.java",
    "    Plan.java           (enum: BASICO, ESTANDAR, PREMIUM, VIP)",
    "    ClaseGrupal.java",
    "    Equipo.java",
    "    Pago.java",
    "    RegistroAcceso.java",
    "    Recompensa.java",
    "    EstadoMembresia.java  (enum)",
    "    EstadoEquipo.java     (enum)",
    "    EstadoPago.java       (enum)",
    "    TipoAcceso.java       (enum)",
    "",
    "  controller/          <- Logica de negocio",
    "    ClienteController.java",
    "    MembresiaController.java",
    "    ClaseGrupalController.java",
    "    EquipoController.java",
    "    PagoController.java",
    "    AccesoController.java",
    "",
    "  view/                <- Interfaces JavaFX",
    "    MainView.java  ClienteView.java  MembresiaView.java",
    "    EquipoView.java  PagoView.java   AccesoView.java",
    "    ReporteView.java",
    "",
    "  service/             <- Servicios transversales",
    "    DataService.java         NotificacionService.java",
    "    BackupService.java       ThemeService.java",
    "    PagoService.java         ReporteService.java",
    "",
    "  exception/  components/  dialog/  util/",
])

# -- Pagina 5: Modelo de dominio ------------------------------------------------
pdf.add_page()
pdf.section_title("4. Modelo de dominio - Diagrama de clases")
pdf.code_block([
    "+-----------------------------------+",
    "|            Cliente                |",
    "+-----------------------------------+",
    "| - id: int                         |",
    "| - nombre: String                  |",
    "| - email: String                   |",
    "| - telefono: String                |",
    "| - puntos: int                     |",
    "| - membresias: List<Membresia>     |",
    "+-----------------------------------+",
    "| + getMembresiaActiva(): Membresia |",
    "| + agregarPuntos(int): void        |",
    "+------------------+----------------+",
    "                   | 1 tiene *        ",
    "                   v                  ",
    "+-----------------------------------+",
    "|           Membresia               |",
    "+-----------------------------------+",
    "| - id: int                         |",
    "| - plan: Plan                      |",
    "| - fechaInicio: LocalDate          |",
    "| - fechaFin: LocalDate             |",
    "| - renovacionAutomatica: boolean   |",
    "| - estado: EstadoMembresia         |",
    "+-----------------------------------+",
    "| + renovar(): void                 |",
    "| + estaVencida(): boolean          |",
    "| + diasRestantes(): long           |",
    "+------------------+----------------+",
    "                   | usa              ",
    "                   v                  ",
    "+-----------------------------------+",
    "|         Plan  (enum)              |",
    "+-----------------------------------+",
    "| BASICO    -> $25.00  /  10 pts   |",
    "| ESTANDAR  -> $45.00  /  20 pts   |",
    "| PREMIUM   -> $75.00  /  50 pts   |",
    "| VIP       -> $120.00 / 100 pts   |",
    "+-----------------------------------+",
    "| + getPrecioFinal(): double        |",
    "+-----------------------------------+",
])

# -- Pagina 6: Mas clases del modelo -------------------------------------------
pdf.add_page()
pdf.section_title("4. Modelo de dominio (continuacion)")
pdf.code_block([
    "+-----------------------------+    +---------------------------+",
    "|           Pago              |    |      RegistroAcceso       |",
    "+-----------------------------+    +---------------------------+",
    "| - id: int                   |    | - id: int                 |",
    "| - cliente: Cliente          |    | - cliente: Cliente        |",
    "| - plan: Plan                |    | - tipo: TipoAcceso        |",
    "| - monto: double             |    | - fechaHora: LocalDateTime|",
    "| - metodo: String            |    +---------------------------+",
    "| - estado: EstadoPago        |    | + esDeHoy(): boolean      |",
    "+-----------------------------+    +---------------------------+",
    "",
    "+-----------------------------+    +---------------------------+",
    "|        ClaseGrupal          |    |         Equipo            |",
    "+-----------------------------+    +---------------------------+",
    "| - id: int                   |    | - id: int                 |",
    "| - nombre: String            |    | - nombre: String          |",
    "| - instructor: String        |    | - tipo: String            |",
    "| - dia: DayOfWeek            |    | - cantidad: int           |",
    "| - hora: LocalTime           |    | - estado: EstadoEquipo    |",
    "| - capacidadMaxima: int      |    | - ultimoMantenimiento     |",
    "| - clienteIdsInscritos: List |    |   : LocalDate             |",
    "+-----------------------------+    +---------------------------+",
    "| + inscribir(int): boolean   |",
    "| + desinscribir(int): boolean|",
    "| + getLugaresDisponibles()   |",
    "+-----------------------------+",
])
pdf.ln(3)
pdf.sub_title("Enumeraciones del dominio")
pdf.table(
    ["Enum", "Valores"],
    [
        ["EstadoMembresia", "ACTIVA, VENCIDA, SUSPENDIDA"],
        ["EstadoEquipo",    "DISPONIBLE, EN_MANTENIMIENTO, FUERA_DE_SERVICIO"],
        ["EstadoPago",      "PENDIENTE, COMPLETADO, RECHAZADO"],
        ["TipoAcceso",      "ENTRADA, SALIDA"],
        ["Plan",            "BASICO, ESTANDAR, PREMIUM, VIP"],
    ],
    [50, 130],
)

# -- Pagina 7: Servicios --------------------------------------------------------
pdf.add_page()
pdf.section_title("5. Capa de servicios")
pdf.body("Los servicios son singletons que proveen funcionalidad transversal al resto del sistema.")
pdf.code_block([
    "+------------------------------------------+",
    "|            DataService (Singleton)        |",
    "+------------------------------------------+",
    "| - clientes: List<Cliente>                |",
    "| - clases: List<ClaseGrupal>              |",
    "| - equipos: List<Equipo>                  |",
    "| - pagos: List<Pago>                      |",
    "| - accesos: List<RegistroAcceso>          |",
    "+------------------------------------------+",
    "| + cargarDatos(): void                    |",
    "| + guardarDatos(): void                   |",
    "| + clientesEnGimnasio(): long             |",
    "| + getInstance(): DataService             |",
    "+------------------------------------------+",
    "",
    "+------------------------------------------+",
    "|     NotificacionService (Singleton)       |",
    "+------------------------------------------+",
    "| - notificaciones: List<String>           |",
    "+------------------------------------------+",
    "| + verificarVencimientos(): void           |",
    "|   -> Renueva membresias <= 7 dias        |",
    "|   -> Genera aviso si no es automatica    |",
    "+------------------------------------------+",
    "",
    "+------------------------------------------+",
    "|        ThemeService (Singleton)           |",
    "+------------------------------------------+",
    "| - dark: boolean                          |",
    "| - scenes: List<Scene>                    |",
    "+------------------------------------------+",
    "| + registerScene(Scene): void             |",
    "| + toggle(): void                         |",
    "|   -> Aplica styles.css o styles-light.css|",
    "+------------------------------------------+",
    "",
    "+------------------------------------------+",
    "|    BackupService extends Task<String>     |",
    "+------------------------------------------+",
    "| + call(): String                         |",
    "|   -> Serializa GymData con timestamp     |",
    "|   -> Reporta progreso updateProgress()   |",
    "+------------------------------------------+",
])

# -- Pagina 8: Excepciones ------------------------------------------------------
pdf.add_page()
pdf.section_title("6. Jerarquia de excepciones")
pdf.body(
    "Se definio una jerarquia propia para representar errores del dominio del gimnasio "
    "sin depender de excepciones genericas de Java. Todas son excepciones chequeadas "
    "(checked), por lo que el compilador obliga a manejarlas en cada punto donde se lanzan."
)
pdf.code_block([
    "Exception",
    "  |-- GymException",
    "        |--- ClienteException",
    "        |-- MembresiaException",
])
pdf.sub_title("Uso tipico en AccesoController")
pdf.code_block([
    "// Lanzar excepcion si no hay membresia",
    "if (cliente.getMembresiaActiva() == null)",
    "    throw new GymException(\"No tiene membresia activa.\");",
    "",
    "// Capturar en la vista",
    "try {",
    "    controller.registrar(cliente, tipo);",
    "} catch (GymException e) {",
    "    AlertUtil.error(\"Error de acceso\", e.getMessage());",
    "}",
])
pdf.body(
    "Ventajas de esta jerarquia:\n"
    "- Se puede capturar GymException para manejar cualquier error del dominio.\n"
    "- Se puede capturar ClienteException o MembresiaException para casos especificos.\n"
    "- Los errores son descriptivos y orientados al usuario final."
)

# -- Pagina 9: Patrones de diseno -----------------------------------------------
pdf.add_page()
pdf.section_title("7. Patrones de diseno aplicados")
pdf.sub_title("Singleton")
pdf.body(
    "DataService, NotificacionService y ThemeService usan Singleton para garantizar "
    "que solo exista una instancia durante toda la ejecucion de la aplicacion."
)
pdf.code_block([
    "public static DataService getInstance() {",
    "    if (instance == null) instance = new DataService();",
    "    return instance;",
    "}",
])
pdf.sub_title("MVC (Model-View-Controller)")
pdf.body(
    "Separacion estricta: la vista no accede directamente al modelo, siempre pasa "
    "por el controlador."
)
pdf.code_block([
    "ClienteView  ->  ClienteController  ->  DataService  ->  Cliente",
])
pdf.sub_title("Componente generico (Reutilizacion)")
pdf.body(
    "SearchableTable<T> encapsula el patron FilteredList + SortedList + TextField "
    "para que cada vista solo declare sus columnas y su predicado de busqueda."
)
pdf.code_block([
    "searchableTable.setItems(ds.getClientes(), (c, texto) ->",
    "    c.getNombre().toLowerCase().contains(texto) ||",
    "    c.getEmail().toLowerCase().contains(texto)",
    ");",
])
pdf.sub_title("Task (Concurrencia JavaFX)")
pdf.body(
    "BackupService extiende Task<String> para ejecutar el backup en un hilo secundario "
    "sin congelar la interfaz, reportando progreso en tiempo real via updateProgress()."
)
pdf.code_block([
    "public class BackupService extends Task<String> {",
    "    @Override",
    "    protected String call() throws Exception {",
    "        updateProgress(0, 4);",
    "        // trabajo pesado sin bloquear la UI",
    "        return filename;",
    "    }",
    "}",
])

# -- Pagina 10: Persistencia ----------------------------------------------------
pdf.add_page()
pdf.section_title("8. Persistencia de datos")
pdf.body(
    "El sistema serializa y deserializa todo el estado de la aplicacion en un archivo "
    "gym_data.dat usando la serializacion nativa de Java."
)
pdf.code_block([
    "Al iniciar la aplicacion:",
    "",
    "  GymApp.start()",
    "    -> DataService.cargarDatos()",
    "         -> Serializer.deserializar(\"gym_data.dat\")",
    "              Si existe: carga GymData (record)",
    "                         restaura clientes, clases, equipos,",
    "                         pagos, accesos y recompensas",
    "              Si falla:  carga datos de ejemplo",
    "",
    "Al cerrar la ventana:",
    "",
    "  stage.setOnCloseRequest(...)",
    "    -> DataService.guardarDatos()",
    "         -> Serializer.serializar(gymData, \"gym_data.dat\")",
])
pdf.sub_title("Record GymData")
pdf.body("Agrupa todas las listas en un unico objeto serializable:")
pdf.code_block([
    "record GymData(",
    "    List<Cliente>         clientes,",
    "    List<ClaseGrupal>     clases,",
    "    List<Equipo>          equipos,",
    "    List<Recompensa>      recompensas,",
    "    List<Pago>            pagos,",
    "    List<RegistroAcceso>  accesos",
    ") implements Serializable {}",
])

# -- Pagina 11: Diagramas de secuencia -----------------------------------------
pdf.add_page()
pdf.section_title("9. Diagrama de secuencia - Registro de acceso")
pdf.body("Flujo cuando un usuario registra la entrada de un cliente al gimnasio.")
pdf.code_block([
    "AccesoView          AccesoController        DataService",
    "    |                     |                      |",
    "    | registrar(cliente,  |                      |",
    "    | ENTRADA)            |                      |",
    "    |-------------------->|                      |",
    "    |                     | getAccesos()         |",
    "    |                     |--------------------->|",
    "    |                     |<---------------------|",
    "    |                     |                      |",
    "    |                     | filtrar accesos hoy  |",
    "    |                     | ordenar por hora     |",
    "    |                     |                      |",
    "    |                     | [validar membresia]  |",
    "    |                     | si nula -> lanza     |",
    "    |                     | GymException         |",
    "    |                     |                      |",
    "    |                     | [validar duplicado]  |",
    "    |                     | si ultimo==ENTRADA   |",
    "    |                     | -> lanza GymException|",
    "    |                     |                      |",
    "    |                     | new RegistroAcceso() |",
    "    |                     | agregarAcceso(reg)   |",
    "    |                     |--------------------->|",
    "    |                     |                      |",
    "    | mostrar exito       |                      |",
    "    |<--------------------|                      |",
])

pdf.section_title("10. Diagrama de secuencia - Backup en segundo plano")
pdf.code_block([
    "MainView           BackupService          Serializer",
    "    |                    |                     |",
    "    | new BackupService  |                     |",
    "    |------------------->|                     |",
    "    |                    |                     |",
    "    | new Thread(task)   |                     |",
    "    | t.setDaemon(true)  |                     |",
    "    | t.start()          |                     |",
    "    |------------------->|                     |",
    "    |                    |                     |",
    "    | [hilo secundario]  |                     |",
    "    |                    | updateProgress(1/4) |",
    "    |                    | updateProgress(4/4) |",
    "    |                    |                     |",
    "    |                    | serializar(gymData) |",
    "    |                    |-------------------->|",
    "    |                    |<--------------------|",
    "    |                    |                     |",
    "    | [hilo JavaFX]      |                     |",
    "    | setOnSucceeded()   |                     |",
    "    | AlertUtil.info()   |                     |",
    "    |<-------------------|                     |",
])

# -- Pagina 12: Componentes UI --------------------------------------------------
pdf.add_page()
pdf.section_title("11. Componentes de interfaz reutilizables")
pdf.sub_title("SearchableTable<T>")
pdf.body(
    "Componente generico que combina un TextField de busqueda con un TableView. "
    "El filtrado ocurre en tiempo real sin modificar la lista original."
)
pdf.code_block([
    "+--------------------------------------+",
    "|  SearchableTable<T>  extends VBox    |",
    "+--------------------------------------+",
    "|  [ Buscar...                    ]   |  <- TextField",
    "| +----------------------------------+ |",
    "| | Col1      | Col2      | Col3    | |  <- TableView",
    "| |-----------|-----------|----------| |",
    "| | dato      | dato      | dato    | |",
    "| | dato      | dato      | dato    | |",
    "| +----------------------------------+ |",
    "+--------------------------------------+",
    "",
    "Internamente usa:",
    "  FilteredList<T>   -> filtra sin tocar la lista original",
    "  SortedList<T>     -> permite ordenar por columna",
    "  BiPredicate<T,S>  -> logica de busqueda de cada modulo",
])
pdf.sub_title("ConfirmDialog")
pdf.body(
    "Ventana modal personalizada con initModality(APPLICATION_MODAL) que bloquea "
    "la ventana padre hasta que el usuario responde. Soporta atajos: "
    "Enter = confirmar, Escape = cancelar. Hereda el tema activo via ThemeService."
)
pdf.sub_title("StatusBadge")
pdf.body(
    "Etiqueta visual con color de fondo segun el estado del objeto "
    "(ACTIVA, VENCIDA, EN_MANTENIMIENTO, etc.), usando clases CSS definidas en "
    "styles.css y styles-light.css."
)

# -- Pagina 13: Flujo y requisitos ---------------------------------------------
pdf.add_page()
pdf.section_title("12. Flujo de la aplicacion")
pdf.code_block([
    "        +-------------+",
    "        |    Main     |",
    "        |   .main()   |",
    "        +------+------+",
    "               |",
    "               v",
    "        +-------------+",
    "        |   GymApp    |",
    "        |  .start()   |",
    "        +------+------+",
    "               |",
    "    +----------+----------+",
    "    |           |          |",
    "    v           v          v",
    "DataService  Notificacion  ThemeService",
    ".cargar()    Service       .register()",
    "             .verificar()",
    "               |",
    "               v",
    "        +-------------+",
    "        |  MainView   |  <- barra de navegacion",
    "        +------+------+",
    "               | tab seleccionada",
    "    +----------+----------+----------+",
    "    |           |          |          |",
    "    v           v          v          v",
    "ClienteView  Membresia  Equipo    AccesoView",
    "               View      View",
    "    |",
    "    | accion del usuario",
    "    v",
    "ClienteController",
    "    |",
    "    v",
    "DataService.agregarCliente()",
    "    |",
    "    v",
    "[Al cerrar] DataService.guardarDatos()",
])
pdf.section_title("13. Requisitos y ejecucion")
pdf.table(
    ["Componente", "Version minima"],
    [
        ["Java JDK",        "21"],
        ["JavaFX SDK",       "21"],
        ["Maven",           "3.8+"],
        ["Sistema operativo","Windows / macOS / Linux"],
    ],
    [70, 110],
)
pdf.sub_title("Comandos")
pdf.code_block([
    "# Compilar",
    "mvn clean compile",
    "",
    "# Ejecutar",
    "mvn javafx:run",
    "",
    "# Generar JAR ejecutable",
    "mvn clean package",
    "java -jar target/gymmanager-pro.jar",
])
pdf.body(
    "El archivo gym_data.dat se crea automaticamente la primera vez que se cierra "
    "la aplicacion. Si no existe o esta danado, el sistema carga datos de ejemplo "
    "para que la aplicacion no quede vacia en el primer arranque."
)

# -- Output ---------------------------------------------------------------------
pdf.output(r"c:\Users\elias\datathon_practica\LAB_POO_2026\practica_11\manual_tecnico.pdf")
print("PDF generado correctamente.")
