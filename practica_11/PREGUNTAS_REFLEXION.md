# Preguntas de Reflexión — JavaFX

## 1. ¿Qué es el hilo de la UI (JavaFX Application Thread) y por qué no debes hacer operaciones pesadas en él?

Es el hilo que se encarga de dibujar y actualizar todo lo que el usuario ve en pantalla: botones, textos, animaciones, etc.

Si pones una operación lenta ahí (como leer un archivo grande o consultar una base de datos), la interfaz se "congela" y el usuario no puede hacer nada hasta que esa operación termine.
---

## 2. ¿Qué es un `EventHandler`? ¿Cómo conecta la acción del usuario con la lógica de tu programa?

Un `EventHandler` es el código que se ejecuta cuando el usuario hace algo, por ejemplo hacer clic en un botón o presionar una tecla.

Funciona como un "escucha": le dices a un botón "cuando te hagan clic, ejecuta este código", y JavaFX se encarga de llamarlo en el momento justo. Así la interfaz sabe qué hacer ante cada acción sin tener que estar preguntando constantemente si pasó algo.

---

## 3. ¿Qué diferencia hay entre un `Stage`, una `Scene` y un `Node` en JavaFX?

El Stage es la ventana, la Scene es el lienzo o pantalla que decides mostrar en ese momento, y los Node son los botones y textos individuales que llenan ese lienzo.
