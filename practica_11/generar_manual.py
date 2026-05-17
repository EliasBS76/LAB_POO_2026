from fpdf import FPDF
from fpdf.enums import XPos, YPos

DIAG = r"c:\Users\elias\datathon_practica\LAB_POO_2026\practica_11\diagramas"

class PDF(FPDF):
    def header(self):
        if self.page_no() == 1:
            return
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150, 150, 150)
        self.cell(0, 6, "Manual Tecnico - GymManager Pro",
                  new_x=XPos.LMARGIN, new_y=YPos.NEXT, align="L")
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
        self.cell(0, 14, "GymManager Pro", align="C",
                  new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.set_font("Helvetica", "", 16)
        self.set_text_color(180, 180, 180)
        self.cell(0, 10, "Manual Tecnico", align="C",
                  new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.ln(10)
        self.set_draw_color(80, 80, 80)
        self.line(self.l_margin + 30, self.get_y(),
                  self.w - self.r_margin - 30, self.get_y())
        self.ln(10)
        rows = [
            ("Proyecto:",   "PIA - Programacion Orientada a Objetos"),
            ("Tecnologia:", "Java 21 + JavaFX 21 + Maven"),
            ("Autor:",      "Elias Jacob Bernal"),
            ("Fecha:",      "Mayo 2026"),
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
        self.cell(0, 9, text, fill=True, border="B",
                  new_x=XPos.LMARGIN, new_y=YPos.NEXT)
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
        block_h = len(lines) * 5 + 4
        self.rect(self.l_margin, y0,
                  self.w - self.l_margin - self.r_margin, block_h, "FD")
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

    def diagram(self, path, caption="", w=None):
        if w is None:
            w = self.w - self.l_margin - self.r_margin
        self.image(path, x=self.l_margin, w=w)
        if caption:
            self.set_font("Helvetica", "I", 8)
            self.set_text_color(120, 120, 120)
            self.cell(0, 5, caption, align="C",
                      new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.ln(3)


# ── build ──────────────────────────────────────────────────────────────────────
pdf = PDF()
pdf.set_auto_page_break(auto=True, margin=18)
pdf.set_margins(18, 18, 18)

# ── Portada ────────────────────────────────────────────────────────────────────
pdf.title_page()

# ── Pag 2: Descripcion general ────────────────────────────────────────────────
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
    "- iText / OpenPDF para generacion de reportes PDF"
)

# ── Pag 3: Arquitectura MVC ───────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("2. Arquitectura del proyecto (patron MVC)")
pdf.body(
    "El sistema sigue el patron MVC (Model-View-Controller). "
    "Cada capa tiene una responsabilidad especifica y no se mezclan entre si: "
    "la Vista construye la UI, el Controller valida y coordina, "
    "y el Model junto con los servicios gestionan los datos."
)
pdf.diagram(f"{DIAG}/diagrama_mvc.png",
            "Figura 1 - Arquitectura MVC de GymManager Pro")

# ── Pag 4: Diagrama de paquetes ───────────────────────────────────────────────
pdf.add_page()
pdf.section_title("3. Diagrama de paquetes")
pdf.body(
    "El proyecto se organiza en 8 paquetes con responsabilidades claras. "
    "Las flechas indican dependencias: view usa controller, controller usa model y service."
)
pdf.diagram(f"{DIAG}/diagrama_paquetes.png",
            "Figura 2 - Diagrama de paquetes de GymManager Pro")

# ── Pag 5: Modelo de dominio ──────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("4. Modelo de dominio - Diagrama de clases")
pdf.body(
    "Las entidades del dominio implementan Serializable para poder persistirse en disco. "
    "Cada clase administra su propio contador de IDs estatico para garantizar unicidad."
)
pdf.diagram(f"{DIAG}/diagrama_clases.png",
            "Figura 2 - Diagrama de clases del modelo de dominio")

# ── Pag 6: Enumeraciones ──────────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("4. Modelo de dominio (continuacion) - Enumeraciones")
pdf.body(
    "El sistema usa enumeraciones para representar estados y categorias fijas del dominio, "
    "evitando el uso de Strings magicos y aprovechando el tipado fuerte de Java."
)
pdf.table(
    ["Enum", "Valores"],
    [
        ["Plan",            "BASICO ($25/10pts), ESTANDAR ($45/20pts), PREMIUM ($75/50pts), VIP ($120/100pts)"],
        ["EstadoMembresia", "ACTIVA, VENCIDA, SUSPENDIDA"],
        ["EstadoEquipo",    "DISPONIBLE, EN_MANTENIMIENTO, FUERA_DE_SERVICIO"],
        ["EstadoPago",      "PENDIENTE, COMPLETADO, RECHAZADO"],
        ["TipoAcceso",      "ENTRADA, SALIDA"],
    ],
    [42, 138],
)
pdf.sub_title("Plan (enum) con logica de negocio")
pdf.body(
    "Plan es un enum especial que contiene atributos (precio, descuento, puntosBonus) "
    "y un metodo getPrecioFinal() que aplica el descuento correspondiente a cada nivel."
)
pdf.code_block([
    "BASICO  (\"Basico\",   25.0,  0.00,  10)",
    "ESTANDAR(\"Estandar\", 45.0,  0.05,  20)   // 5% descuento",
    "PREMIUM (\"Premium\",  75.0,  0.10,  50)   // 10% descuento",
    "VIP     (\"VIP\",     120.0,  0.20, 100)   // 20% descuento",
    "",
    "double getPrecioFinal() { return precio * (1 - descuento); }",
])

# ── Pag 7: Servicios ──────────────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("5. Capa de servicios")
pdf.body(
    "Los servicios son clases Singleton que proveen funcionalidad transversal. "
    "Ninguna vista o controlador crea instancias directamente; siempre usan getInstance()."
)
pdf.diagram(f"{DIAG}/diagrama_servicios.png",
            "Figura 3 - Servicios Singleton del sistema")

# ── Pag 8: Excepciones ────────────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("6. Jerarquia de excepciones")
pdf.body(
    "Se definio una jerarquia propia para representar errores del dominio del gimnasio. "
    "Todas son checked exceptions: el compilador obliga a capturarlas o declararlas "
    "en cada punto donde se lanzan, lo que hace explicita la logica de errores."
)
pdf.diagram(f"{DIAG}/diagrama_excepciones.png",
            "Figura 4 - Jerarquia de excepciones del dominio")
pdf.sub_title("Uso tipico")
pdf.code_block([
    "// En AccesoController - lanzar",
    "if (cliente.getMembresiaActiva() == null)",
    "    throw new GymException(\"No tiene membresia activa.\");",
    "",
    "// En AccesoView - capturar",
    "try {",
    "    controller.registrar(cliente, tipo);",
    "} catch (GymException e) {",
    "    AlertUtil.error(\"Error de acceso\", e.getMessage());",
    "}",
])

# ── Pag 9: Patrones ───────────────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("7. Patrones de diseno aplicados")
pdf.sub_title("Singleton")
pdf.body(
    "DataService, NotificacionService y ThemeService usan Singleton. "
    "Garantizan una unica instancia durante toda la ejecucion, evitando "
    "inconsistencias al tener multiples copias de los datos en memoria."
)
pdf.code_block([
    "public static DataService getInstance() {",
    "    if (instance == null) instance = new DataService();",
    "    return instance;",
    "}",
])
pdf.sub_title("MVC (Model-View-Controller)")
pdf.body("La vista nunca accede al modelo directamente, siempre pasa por el controlador:")
pdf.code_block([
    "ClienteView  ->  ClienteController  ->  DataService  ->  Cliente",
])
pdf.sub_title("Componente generico SearchableTable<T>")
pdf.body(
    "Encapsula el patron FilteredList + SortedList + TextField para que "
    "cada vista solo declare sus columnas y su predicado de busqueda, sin repetir codigo."
)
pdf.code_block([
    "searchableTable.setItems(ds.getClientes(), (c, texto) ->",
    "    c.getNombre().toLowerCase().contains(texto) ||",
    "    c.getEmail().toLowerCase().contains(texto)",
    ");",
])
pdf.sub_title("Task<T> para concurrencia en JavaFX")
pdf.body(
    "BackupService extiende Task<String> en lugar de Runnable. "
    "Esto permite reportar progreso con updateProgress(), mostrar mensajes con "
    "updateMessage(), y recibir callbacks en el hilo de JavaFX con setOnSucceeded()."
)
pdf.code_block([
    "public class BackupService extends Task<String> {",
    "    @Override",
    "    protected String call() throws Exception {",
    "        updateProgress(0, 4);",
    "        // trabajo en hilo secundario sin bloquear la UI",
    "        return filename;",
    "    }",
    "}",
])

# ── Pag 10: Persistencia ──────────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("8. Persistencia de datos")
pdf.body(
    "El sistema serializa y deserializa todo el estado en un archivo gym_data.dat "
    "usando serializacion nativa de Java. El record GymData agrupa todas las listas "
    "en un unico objeto serializable."
)
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
pdf.sub_title("Flujo de carga y guardado")
pdf.code_block([
    "Al iniciar:",
    "  GymApp.start()",
    "    DataService.cargarDatos()",
    "      Serializer.deserializar('gym_data.dat')",
    "        OK  -> restaura clientes, clases, equipos, pagos, accesos",
    "        FAIL -> carga datos de ejemplo automaticamente",
    "",
    "Al cerrar la ventana:",
    "  stage.setOnCloseRequest(...)",
    "    DataService.guardarDatos()",
    "      Serializer.serializar(gymData, 'gym_data.dat')",
])

# ── Pag 11: Secuencia acceso ──────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("9. Diagrama de secuencia - Registro de acceso")
pdf.body(
    "Muestra el flujo completo cuando un usuario registra la entrada de un cliente. "
    "La validacion es secuencial: primero membresia, luego estado de acceso del dia."
)
pdf.diagram(f"{DIAG}/diagrama_secuencia_acceso.png",
            "Figura 5 - Secuencia de registro de acceso con validaciones")

# ── Pag 12: Secuencia backup ──────────────────────────────────────────────────
pdf.add_page()
pdf.section_title("10. Diagrama de secuencia - Backup en segundo plano")
pdf.body(
    "BackupService se ejecuta en un hilo daemon (muere cuando se cierra la app). "
    "El progreso se reporta en tiempo real y el callback de exito se ejecuta "
    "automaticamente en el hilo de JavaFX sin necesidad de Platform.runLater()."
)
pdf.diagram(f"{DIAG}/diagrama_secuencia_backup.png",
            "Figura 6 - Secuencia del proceso de backup con multithreading")

# ── Pag 13: Requisitos y ejecucion ────────────────────────────────────────────
pdf.add_page()
pdf.section_title("11. Componentes de interfaz reutilizables")
pdf.sub_title("SearchableTable<T>")
pdf.body(
    "Componente que extiende VBox y combina un TextField de busqueda con un TableView. "
    "Internamente usa FilteredList<T> para filtrar en tiempo real sin modificar la "
    "lista original, y SortedList<T> para permitir ordenamiento por columna."
)
pdf.sub_title("ConfirmDialog")
pdf.body(
    "Ventana modal con initModality(APPLICATION_MODAL) que bloquea la ventana padre. "
    "Soporta atajos: Enter confirma, Escape cancela. Hereda el tema activo via ThemeService."
)
pdf.sub_title("StatusBadge")
pdf.body(
    "Etiqueta con color segun el estado del objeto (ACTIVA, VENCIDA, EN_MANTENIMIENTO). "
    "El color se aplica mediante clases CSS definidas en styles.css y styles-light.css."
)

pdf.section_title("12. Requisitos y ejecucion")
pdf.table(
    ["Componente", "Version minima"],
    [
        ["Java JDK",         "17 (compilado en 17, compatible con 21+)"],
        ["Maven Wrapper",    "incluido en el repo (mvnw / mvnw.cmd)"],
        ["JavaFX SDK",       "21 (descargado automaticamente por Maven)"],
        ["Sistema operativo","Windows (natives win incluidas en pom.xml)"],
    ],
    [50, 130],
)
pdf.sub_title("Comandos para clonar y ejecutar")
pdf.code_block([
    "git clone https://github.com/EliasBS76/LAB_POO_2026.git",
    "cd LAB_POO_2026/practica_11",
    "",
    "# Windows",
    "mvnw.cmd javafx:run",
    "",
    "# Mac / Linux",
    "./mvnw javafx:run",
    "",
    "# Generar JAR ejecutable",
    "mvnw.cmd package",
    "java -jar target/gym-manager-1.0.jar",
])
pdf.body(
    "El Maven Wrapper descarga Maven 3.9.6 automaticamente. "
    "Maven descarga JavaFX 21 y OpenPDF de internet en la primera ejecucion. "
    "Si gym_data.dat no existe, el sistema carga datos de ejemplo al arrancar."
)

# ── Output ─────────────────────────────────────────────────────────────────────
out = r"c:\Users\elias\datathon_practica\LAB_POO_2026\practica_11\manual_tecnico.pdf"
pdf.output(out)
print(f"PDF generado: {out}")
