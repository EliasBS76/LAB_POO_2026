# Asistencia de IA en el Desarrollo del PIA — GymManager Pro

**Proyecto:** GymManager Pro  
**Práctica:** 11 — Programación Orientada a Objetos  
**Herramienta utilizada:** Claude Code (Claude Sonnet 4.6 — Anthropic)

---

## 1. Estructura y organización del proyecto

Una de las primeras preguntas fue cómo organizar un proyecto JavaFX con Maven siguiendo el patrón MVC. La IA propuso dividir el código en 9 paquetes con responsabilidades claras:

| Paquete | Responsabilidad |
|---|---|
| `model` | Entidades del dominio (Cliente, Membresía, Plan, Equipo…) |
| `view` | Interfaces gráficas en JavaFX |
| `controller` | Lógica de negocio y coordinación entre vista y modelo |
| `service` | Servicios transversales (persistencia, notificaciones, backup) |
| `exception` | Jerarquía de excepciones personalizadas |
| `components` | Componentes reutilizables de UI |
| `dialog` | Ventanas modales personalizadas |
| `util` | Utilidades generales |
| `resources` | Archivos CSS y recursos visuales |

También se explicó por qué el punto de entrada debe ser una clase `Main` separada que no extienda `Application`, para que el fat JAR funcione correctamente con el `maven-shade-plugin`.

---

## 2. Conceptos explicados

### Patrón MVC
Se explicó la separación de responsabilidades: el **Model** contiene los datos y reglas del dominio, el **View** construye la interfaz gráfica sin lógica de negocio, y el **Controller** conecta ambos respondiendo a los eventos del usuario.

### Excepciones personalizadas
Se diseñó una jerarquía propia del dominio:
```
RuntimeException
  └── GymException
        ├── ClienteException
        └── MembresiaException
```
Esto permite capturar errores específicos del gimnasio sin depender de excepciones genéricas de Java.

### Serialización
Se explicó cómo persistir datos entre ejecuciones usando serialización Java: un `record GymData implements Serializable` agrupa todo el estado de la aplicación, y la clase `Serializer` lo escribe y lee desde un archivo `.dat`.

### FilteredList y SortedList
Se explicó el mecanismo de filtrado en tiempo real sin modificar la lista original:
```java
filteredData = new FilteredList<>(items, p -> true);
SortedList<T> sortedData = new SortedList<>(filteredData);
sortedData.comparatorProperty().bind(tableView.comparatorProperty());
tableView.setItems(sortedData);
```
La lista original nunca se toca; solo cambia el `Predicate` de la `FilteredList`.

### Modo día/noche con ThemeService
Se explicó el patrón Singleton aplicado a `ThemeService` para gestionar el tema CSS en toda la aplicación. Al cambiar el tema se reemplaza la hoja de estilos en la escena con `scene.getStylesheets().clear()` y `scene.getStylesheets().add(...)`.

### Validación en tiempo real con CSS
Se mostró cómo usar `textProperty().addListener` para agregar o quitar clases CSS (`field-valid` / `field-invalid`) según si el campo pasa validación, sin necesidad de botones de verificar.

---

## 3. Uso de hilos (Multithreading)

Se explicó por qué JavaFX congela la interfaz si se hace trabajo pesado en el hilo principal, y cómo solucionarlo con `Task<T>`:

```java
Task<String> backupTask = new Task<>() {
    @Override
    protected String call() throws Exception {
        // trabajo pesado aquí, fuera del hilo de JavaFX
        return "Backup completado";
    }
};

backupTask.setOnSucceeded(e ->
    AlertUtil.info("Backup", backupTask.getValue())  // de vuelta en el hilo de JavaFX
);

Thread t = new Thread(backupTask);
t.setDaemon(true);  // muere con la app, no la bloquea al cerrar
t.start();
```

Conceptos cubiertos:
- **Hilo daemon:** no impide que la JVM cierre cuando el usuario cierra la ventana.
- **`Platform.runLater()`:** única forma segura de actualizar la UI desde un hilo secundario.
- **`Task.setOnSucceeded/setOnFailed`:** callbacks que se ejecutan automáticamente en el hilo de JavaFX.

---

## 4. Revisión de código y sugerencias de mejora

Durante el desarrollo se revisaron varias versiones del código y se propusieron mejoras concretas:

- **`BackupService`:** se sugirió que extendiera `Task<String>` en lugar de implementar `Runnable`, para tener callbacks de éxito/error integrados con JavaFX.
- **`SearchableTable<T>`:** se propuso extraerlo como componente genérico reutilizable en lugar de duplicar el código de filtrado en cada vista.
- **`AccesoController`:** se corrigió que validara la existencia de una entrada antes de permitir registrar una salida, lanzando `MembresiaException` si el cliente no tenía membresía activa.
- **`ConfirmDialog`:** se indicó que debía usar `initModality(Modality.APPLICATION_MODAL)` para bloquear la ventana padre hasta que el usuario respondiera.
- **CSS:** se organizaron los estilos en dos archivos separados (`styles.css` para modo oscuro, `styles-light.css` para modo claro) en lugar de manejar el tema con condiciones en Java.

---

## 5. Formulación de commits

Se ayudó a estructurar el historial de git con commits atómicos y descriptivos, agrupando los cambios por capa o funcionalidad:

| Commit | Contenido |
|---|---|
| `Inicializar practica_11: proyecto Maven + JavaFX 21` | Esqueleto del proyecto, `pom.xml`, estructura de paquetes |
| `Agregar modelos del dominio y excepciones personalizadas` | Clases `model/` y `exception/` |
| `Agregar capa de servicios: persistencia, notificaciones y backup` | `DataService`, `NotificacionService`, `BackupService` con `Task` |
| `Agregar componentes personalizados, diálogos y controladores MVC` | `SearchableTable`, `StatusBadge`, `ConfirmDialog`, controladores |
| `Implementar vistas CRUD con filtrado interactivo y estilos CSS` | Vistas JavaFX, `styles.css`, integración completa |
| `Agregar simulación de pagos, registro de accesos y reportes PDF` | `Pago`, `RegistroAcceso`, `ReporteService` con iText |
| `Agregar modo día/noche y validación de accesos` | `ThemeService` singleton, `styles-light.css`, botón de alternancia |

---

## 6. Generación de reportes PDF

Se explicó el uso de la librería **iText** declarada en el `pom.xml`. La clase `ReporteService` crea un `Document`, le agrega párrafos con los datos del sistema y lo cierra. La vista `ReporteView` llama al servicio y muestra una alerta de confirmación al terminar.

---

## 7. Resumen del rol de la IA

La IA actuó como un **desarrollador senior en sesiones de pair programming**, acompañando cada etapa del proyecto de forma activa. No se limitó a responder preguntas puntuales, sino que participó como un compañero técnico experimentado: propuso la arquitectura inicial, revisó el código conforme avanzaba el desarrollo, detectó errores lógicos antes de que se convirtieran en bugs, sugirió refactors para mejorar la calidad del código y guió la formulación de commits con una estructura profesional. En todo momento el estudiante tomó las decisiones finales; la IA aportó el criterio técnico y la experiencia para que esas decisiones fueran informadas.
