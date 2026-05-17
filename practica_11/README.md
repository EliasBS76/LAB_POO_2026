# GymManager Pro

Sistema de Gestión de Gimnasio desarrollado con JavaFX siguiendo el patrón MVC.

## Cómo compilar y ejecutar

### Requisitos
- Java 17 o superior
- Maven 3.8+
- Conexión a internet (primera vez, para descargar JavaFX)

### Ejecutar en desarrollo
```bash
cd practica_11
mvn javafx:run
```

### Generar JAR ejecutable
```bash
mvn package
java -jar target/gym-manager-1.0.jar
```

---

## Estructura del proyecto

```
src/main/java/gym/
├── Main.java                    # Punto de entrada (no extiende Application)
├── GymApp.java                  # Aplicación JavaFX
├── model/
│   ├── Cliente.java
│   ├── Membresia.java
│   ├── Plan.java                # Enum: BASICO, ESTANDAR, PREMIUM, VIP
│   ├── ClaseGrupal.java
│   ├── Equipo.java
│   ├── Recompensa.java
│   ├── EstadoMembresia.java     # Enum: ACTIVA, VENCIDA, SUSPENDIDA
│   └── EstadoEquipo.java        # Enum: DISPONIBLE, EN_MANTENIMIENTO, FUERA_DE_SERVICIO
├── controller/
│   ├── ClienteController.java
│   ├── MembresiaController.java
│   ├── ClaseGrupalController.java
│   └── EquipoController.java
├── view/
│   ├── MainView.java
│   ├── ClienteView.java
│   ├── MembresiaView.java
│   ├── ClaseGrupalView.java
│   └── EquipoView.java
├── components/
│   ├── SearchableTable.java     # Componente personalizado 1
│   └── StatusBadge.java         # Componente personalizado 2
├── service/
│   ├── DataService.java
│   ├── NotificacionService.java
│   └── BackupService.java       # Multithreading con Task<String>
├── exception/
│   ├── GymException.java
│   ├── ClienteException.java
│   └── MembresiaException.java
├── dialog/
│   └── ConfirmDialog.java       # Diálogo modal personalizado
└── util/
    ├── AlertUtil.java
    └── Serializer.java

src/main/resources/gym/
└── styles.css                   # Hoja de estilos externa
```

**Total: 30 clases** organizadas en 9 paquetes.

---

## Elemento de Decisión Propia — Filtrado Interactivo

### Descripción
Cada módulo (Clientes, Membresías, Clases, Equipos) incluye un campo de búsqueda que filtra la tabla en tiempo real mientras el usuario escribe.

### Implementación

La funcionalidad está encapsulada en el componente personalizado **`SearchableTable<T>`** (en `gym/components/SearchableTable.java`).

#### Clases JavaFX utilizadas

| Clase | Rol |
|---|---|
| `javafx.collections.transformation.FilteredList<T>` | Envuelve la `ObservableList` original y aplica un `Predicate` dinámico |
| `javafx.collections.transformation.SortedList<T>` | Envuelve la `FilteredList` y sincroniza el comparador con la tabla |
| `javafx.scene.control.TableView<T>` | Tabla visual que muestra la `SortedList` |
| `javafx.scene.control.TextField` | Campo de búsqueda; su `textProperty` activa el filtro |

#### Mecanismo de enlace

```java
// 1. Crear FilteredList desde la ObservableList original
filteredData = new FilteredList<>(items, p -> true);

// 2. Listener en el TextField: re-evalúa el predicado en cada pulsación
searchField.textProperty().addListener((obs, oldVal, newVal) ->
    filteredData.setPredicate(item -> {
        if (newVal == null || newVal.isBlank()) return true;
        return predicate.test(item, newVal.toLowerCase().trim());
    })
);

// 3. SortedList mantiene el orden de columna activo
SortedList<T> sortedData = new SortedList<>(filteredData);
sortedData.comparatorProperty().bind(tableView.comparatorProperty());

// 4. La tabla muestra la SortedList (jamás la lista original)
tableView.setItems(sortedData);
```

El predicado de filtrado es configurable por cada vista. Ejemplo para clientes:

```java
tabla.setItems(datos, (cliente, query) ->
    cliente.getNombre().toLowerCase().contains(query) ||
    cliente.getEmail().toLowerCase().contains(query)  ||
    cliente.getTelefono().contains(query)
);
```

Así la lista original **nunca se modifica**: solo cambia el `Predicate` de la `FilteredList`, y JavaFX actualiza la tabla automáticamente.

---

## Funcionalidades implementadas

| Funcionalidad | Dónde |
|---|---|
| Registro de suscripciones con planes y descuentos | `ClienteView` + `ClienteController` |
| Renovación automática de membresías | `NotificacionService.verificarVencimientos()` |
| Sistema de puntos/recompensas | `Cliente.agregarPuntos()`, `Plan.getPuntosBonus()` |
| Calendario de clases grupales | `ClaseGrupalView` + `ClaseGrupalController` |
| Control de inventario de equipos | `EquipoView` + `EquipoController` |
| Notificaciones por vencimiento | `NotificacionService` (al iniciar la app) |

## Características técnicas

| Requisito | Solución |
|---|---|
| Patrón MVC | Paquetes `model`, `view`, `controller` con responsabilidades separadas |
| ≥ 15 clases en paquetes | 30 clases en 9 paquetes |
| Serialización | `Serializer.java` + `DataService.GymData` (record serializable) |
| Multithreading | `BackupService extends Task<String>` ejecutado en hilo daemon |
| Excepciones personalizadas | `GymException` → `ClienteException`, `MembresiaException` |
| GUI JavaFX responsiva | `SplitPane` redimensionable, `VBox.setVgrow(ALWAYS)` |
| Componentes personalizados | `SearchableTable<T>` (extends VBox), `StatusBadge` (extends Label) |
| Eventos de mouse | Doble clic en tabla activa modo edición |
| Eventos de teclado | Enter guarda el formulario; Escape cierra diálogos |
| Validación en tiempo real | `textProperty().addListener` cambia clase CSS `field-valid`/`field-invalid` |
| Diálogo modal | `ConfirmDialog extends Stage` con `initModality(APPLICATION_MODAL)` |
| CSS externo | `src/main/resources/gym/styles.css` cargado en `GymApp.start()` |
| JAR ejecutable | `maven-shade-plugin` genera fat JAR; `Main.java` es el entry point |
