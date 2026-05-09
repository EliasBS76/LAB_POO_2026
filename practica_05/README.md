

### 1. Override vs Overload

**Override** — la subclase redefine un método heredado con la misma firma.
Se decide en tiempo de ejecución.

```java
Ñ
public abstract double calcularConsumo();


@Override
public double calcularConsumo() {
    return 8.5 + (numeroPuertas * 0.3);
}
```

**Overload** — misma clase, mismo nombre, distintos parámetros.
Se decide en tiempo de compilación.

```java
// En Auto (OVERLOADS):
public double calcularConsumo() { ... }
public double calcularConsumo(double cargaPasajeros) { ... }
public double calcularConsumo(double cargaPasajeros, boolean aireEncendido) { ... }
```

---

### 2. Por qué `instanceof` antes del cast

Sin verificar primero, hacer `(Auto) v` cuando `v` es en realidad un `Camion`
lanza `ClassCastException` en runtime y el programa revienta.

```java
// Peligroso:
Auto a = (Auto) v;  

// Seguro (Java 17 pattern-matching):
if (v instanceof Auto a) {
    System.out.println(a.getNumPuertas()); 
}
```

`instanceof` garantiza que el cast es válido antes de hacerlo.

---

### 3. ¿Se puede instanciar `Vehiculo` directamente?

No. El compilador lo bloquea porque `Vehiculo` tiene métodos abstractos sin
implementar. Si Java permitiera crearlo, llamar `v.calcularConsumo()` no tendría
ningún cuerpo que ejecutar.

```java
Vehiculo v = new Vehiculo("X", "Y", 2020, 100);
```
Solo se puede instanciar a través de sus subclases concretas, que sí tienen
todas las implementaciones.
