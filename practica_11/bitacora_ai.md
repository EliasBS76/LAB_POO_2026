# Bitácora de Asistencia con IA — Práctica 11: GymManager Pro

---

Pregunta: ¿Cómo estructuro un proyecto JavaFX con Maven siguiendo el patrón MVC?

Respuesta: Se debe crear el proyecto con Maven y organizar los paquetes en `model`, `view`, `controller`, `service`, `exception`, `components` y `util`. El punto de entrada debe ser una clase `Main` separada que llame a `Application.launch()` para que el fat JAR funcione correctamente con el `maven-shade-plugin`.

---

Pregunta: ¿Cómo implemento excepciones personalizadas en Java?

Respuesta: Se crea una clase base `GymException extends RuntimeException` y luego clases específicas como `ClienteException` y `MembresiaException` que la extiendan. Así se tiene una jerarquía propia del dominio.

---

Pregunta: ¿Cómo hago que los datos persistan entre ejecuciones de la aplicación?

Respuesta: Se usa serialización Java. Se crea un record `GymData` que implemente `Serializable` y una clase `Serializer` con métodos `guardar()` y `cargar()` que escriban y lean ese objeto de un archivo `.dat` en el sistema de archivos.

---

Pregunta: ¿Cómo implemento multithreading en JavaFX para no bloquear la interfaz?

Respuesta: Se extiende `Task<String>` de JavaFX y se sobreescribe `call()`. La tarea se ejecuta en un hilo daemon con `Thread t = new Thread(task); t.setDaemon(true); t.start()`. Nunca se modifica la UI desde ese hilo, solo desde el hilo de JavaFX usando `Platform.runLater()`.

---

Pregunta: ¿Cómo creo un componente de tabla con búsqueda en tiempo real reutilizable?

Respuesta: Se crea `SearchableTable<T> extends VBox`. Internamente usa `FilteredList<T>` que envuelve la `ObservableList` original, y un `TextField` con `textProperty().addListener` que actualiza el `Predicate` en cada pulsación. Una `SortedList` encima de la `FilteredList` sincroniza el orden con la tabla. La lista original nunca se modifica.

---

Pregunta: ¿Cómo implemento un diálogo modal de confirmación personalizado?

Respuesta: Se crea `ConfirmDialog extends Stage` y en el constructor se llama `initModality(Modality.APPLICATION_MODAL)`. Esto bloquea la ventana padre hasta que el diálogo se cierre.

---

Pregunta: ¿Cómo valido campos de formulario en tiempo real con JavaFX y CSS?

Respuesta: Se agrega un listener con `textProperty().addListener` y según si el valor es válido o no, se agrega o quita una clase CSS al nodo con `getStyleClass().add("field-invalid")` o `getStyleClass().remove("field-invalid")`. Los estilos se definen en el archivo CSS externo.

---

Pregunta: ¿Cómo implemento modo día/noche en JavaFX?

Respuesta: Se crean dos archivos CSS, `styles.css` (oscuro) y `styles-light.css` (claro). Un `ThemeService` guarda la preferencia y al cambiar el tema se reemplaza la hoja de estilo en la escena con `scene.getStylesheets().clear()` seguido de `scene.getStylesheets().add(...)`.

---

Pregunta: ¿Cómo genero un reporte en PDF desde Java?

Respuesta: Se usa la librería iText declarada como dependencia en el `pom.xml`. Se crea un `Document`, se le agregan párrafos con los datos del sistema y se cierra. La vista `ReporteView` llama al servicio y muestra una alerta de confirmación cuando el archivo se genera.

---

Pregunta: ¿Cómo simulo el registro de accesos al gimnasio?

Respuesta: Se crea el modelo `RegistroAcceso` con un enum `TipoAcceso` (ENTRADA, SALIDA) y un timestamp. El `AccesoController` verifica que el cliente tenga membresía activa antes de registrar la entrada, lanzando `MembresiaException` si no la tiene


