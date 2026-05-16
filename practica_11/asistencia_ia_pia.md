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

## 4. Revisión de código: errores encontrados y cómo se corrigieron

Esta sección documenta los problemas concretos detectados en el código durante el desarrollo y las sugerencias que se aplicaron para resolverlos. El alumno escribió el código; la IA señaló qué estaba mal y por qué, y el alumno tomó la decisión de cómo corregirlo.

---

### Error 1 — `AccesoController`: lógica de acceso incompleta

**Código original (antes de la corrección):**
```java
public RegistroAcceso registrar(Cliente cliente, TipoAcceso tipo) throws GymException {
    if (cliente == null)
        throw new GymException("Selecciona un cliente.");

    if (tipo == TipoAcceso.ENTRADA && cliente.getMembresiaActiva() == null)
        throw new GymException("\"" + cliente.getNombre() + "\" no tiene membresía activa. Acceso denegado.");

    RegistroAcceso reg = new RegistroAcceso(cliente, tipo);
    ds.agregarAcceso(reg);
    return reg;
}
```

**Problema detectado:** La validación solo revisaba si el cliente tenía membresía al entrar, pero ignoraba dos casos incorrectos:
- Un cliente podía registrar una **salida sin haber entrado** ese día.
- Un cliente podía registrar **dos entradas seguidas** sin salida intermedia (quedaba "duplicado" dentro del sistema).

**Sugerencia de la IA:** Filtrar los accesos del día actual para ese cliente, ordenarlos por hora y revisar cuál fue el **último movimiento** antes de permitir el siguiente. Si el último fue `ENTRADA`, no se puede volver a entrar; si no hay movimientos del día, no se puede registrar salida.

**Código corregido (aplicado por el alumno):**
```java
List<RegistroAcceso> hoy = ds.getAccesos().stream()
        .filter(a -> a.getCliente().getId() == cliente.getId() && a.esDeHoy())
        .sorted((a, b) -> a.getFechaHora().compareTo(b.getFechaHora()))
        .toList();

if (tipo == TipoAcceso.ENTRADA) {
    if (cliente.getMembresiaActiva() == null)
        throw new GymException("...");
    if (!hoy.isEmpty() && hoy.get(hoy.size() - 1).getTipo() == TipoAcceso.ENTRADA)
        throw new GymException("... ya tiene una entrada activa sin salida registrada.");
} else {
    if (hoy.isEmpty())
        throw new GymException("... no tiene registro de entrada hoy.");
    if (hoy.get(hoy.size() - 1).getTipo() == TipoAcceso.SALIDA)
        throw new GymException("... ya registró salida anteriormente.");
}
```

---

### Error 2 — `ConfirmDialog`: CSS hardcodeado que rompía el modo día/noche

**Código original (antes de la corrección):**
```java
Scene scene = new Scene(root);
try {
    scene.getStylesheets().add(
        getClass().getResource("/gym/styles.css").toExternalForm()
    );
} catch (Exception ignored) {}
```

**Problemas detectados:**
1. El diálogo siempre cargaba `styles.css` (modo oscuro) aunque el usuario hubiera activado el modo claro. Al abrir cualquier diálogo de confirmación, el tema volvía al oscuro.
2. El bloque `catch (Exception ignored)` silenciaba cualquier error de carga de CSS sin avisarle al desarrollador — mala práctica que oculta bugs reales.

**Sugerencia de la IA:** Registrar la escena del diálogo en `ThemeService` igual que se hace en `GymApp`, para que reciba automáticamente el tema activo y cambie junto con el resto de la aplicación.

**Código corregido (aplicado por el alumno):**
```java
Scene scene = new Scene(root);
ThemeService.getInstance().registerScene(scene);
```

---

### Error 3 — `BackupService`: usar `Runnable` en lugar de `Task<T>`

**Problema planteado:** La primera idea era implementar `BackupService` como un `Runnable` lanzado con `new Thread(...)`, ejecutando el backup en segundo plano.

**Sugerencia de la IA:** `Runnable` no tiene forma nativa de reportar progreso ni de notificar a la UI cuando termina o falla. Para JavaFX existe `Task<T>`, que provee:
- `updateProgress()` para actualizar una barra de progreso
- `updateMessage()` para mostrar mensajes de estado
- `setOnSucceeded()` / `setOnFailed()` que corren automáticamente en el hilo de JavaFX sin necesidad de `Platform.runLater()`

**Decisión del alumno:** Reescribir `BackupService` extendiendo `Task<String>`, lo que resultó en una clase más limpia con retroalimentación visual real al usuario.

```java
public class BackupService extends Task<String> {
    @Override
    protected String call() throws Exception {
        updateMessage("Iniciando backup…");
        updateProgress(0, 4);
        // ... trabajo real ...
        return filename;
    }
}
```

---

### Error 4 — `SearchableTable<T>`: código de filtrado duplicado en cada vista

**Problema detectado:** Cada vista (clientes, membresías, equipos…) repetía el mismo bloque de código para crear un `TextField`, una `FilteredList`, una `SortedList` y enlazarla al `TableView`. Esto viola el principio DRY y hace que cualquier corrección tenga que replicarse en varios archivos.

**Sugerencia de la IA:** Extraer ese patrón a un componente genérico `SearchableTable<T>` que extienda `VBox` y reciba un `BiPredicate<T, String>` para el filtrado. Así cada vista solo declara las columnas y pasa el predicado específico de su dominio.

**Resultado:** Un único componente reutilizable. Cualquier mejora al filtrado (por ejemplo, ignorar acentos) se hace en un solo lugar.

---

### Error 5 — `Main` separada de `GymApp`

**Problema planteado:** El punto de entrada original era directamente la clase `GymApp extends Application`. Al empaquetar con `maven-shade-plugin`, el fat JAR fallaba al lanzarse porque el classloader no podía inicializar JavaFX antes de que `main()` se ejecutara.

**Sugerencia de la IA:** Separar el punto de entrada en una clase `Main` que **no extiende** `Application` y simplemente delega a `GymApp.main(args)`. Esto permite que el shade plugin genere un JAR ejecutable correctamente.

```java
public class Main {
    public static void main(String[] args) {
        GymApp.main(args);
    }
}
```

---

### Resumen de errores por categoría

| # | Archivo | Tipo de error | Corrección aplicada |
|---|---|---|---|
| 1 | `AccesoController.java` | Lógica de negocio incompleta | Validación de secuencia ENTRADA→SALIDA con Stream |
| 2 | `ConfirmDialog.java` | CSS hardcodeado + `catch` silencioso | Integración con `ThemeService` |
| 3 | `BackupService.java` | Diseño: `Runnable` en lugar de `Task<T>` | Heredar de `Task<String>` para integración con JavaFX |
| 4 | Múltiples vistas | Código duplicado | Componente genérico `SearchableTable<T>` |
| 5 | `GymApp.java` | Punto de entrada causa fallo en fat JAR | Clase `Main` separada |

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
