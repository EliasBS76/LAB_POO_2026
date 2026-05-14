# Reflexión — Práctica 8

## 1. ¿Por qué usaste cada colección? ¿Qué pasaría si usaras ArrayList para todo?

Usé cada una donde tiene sentido:

- **HashMap** para buscar productos por ID. Con un ArrayList tendría que recorrer toda la lista cada vez que busco uno — con HashMap lo encuentro directo en O(1).
- **ArrayList** para listar y usar Streams. Es la más eficiente cuando casi todo es lectura y necesitas índices.
- **LinkedList** para el historial. Insertar y borrar al inicio es O(1), que es justo lo que necesito para un historial tipo FIFO.
- **HashSet** para las categorías. No quiero duplicados y no me importa el orden — el Set lo maneja solo.

Si usara ArrayList para todo funcionaría, pero sería más lento. Buscar por ID sería O(n) en lugar de O(1), y tendría que hacer un `contains()` manual para evitar categorías repetidas.

---

## 2. ¿Qué diferencia hay entre Comparable y Comparator?

`Comparable` se implementa dentro de la propia clase y define **un solo orden natural**. En este caso `Producto` se compara por precio porque es el criterio más común.

`Comparator` es una clase aparte que define un orden **externo y adicional**. Lo usé para ordenar por nombre, por stock o por categoría+precio sin tocar la clase `Producto`.

Uso `Comparable` cuando hay un orden obvio que siempre aplica (precio para productos, fecha para eventos). Uso `Comparator` cuando necesito varios criterios distintos o cuando no puedo modificar la clase.

---

## 3. ¿Qué hace un Stream? ¿Por qué es más legible que un for?

Un Stream es como una tubería: le dices qué datos entran, qué operaciones aplicarles (filtrar, transformar, ordenar) y qué quieres al final (una lista, un número, etc.). No modifica la colección original.

Por ejemplo, esto con for:

```java
List<Producto> resultado = new ArrayList<>();
for (Producto p : lista) {
    if (p.getPrecio() >= 20 && p.getPrecio() <= 100 && p.getStock() > 10) {
        resultado.add(p);
    }
}
Collections.sort(resultado);
```

Con Stream queda así:

```java
lista.stream()
    .filter(p -> p.getPrecio() >= 20 && p.getPrecio() <= 100)
    .filter(p -> p.getStock() > 10)
    .sorted()
    .collect(Collectors.toList());
```

Es más legible porque cada línea describe **qué** quieres, no **cómo** hacerlo. No hay variables temporales ni lógica de acumulación — solo el criterio.
