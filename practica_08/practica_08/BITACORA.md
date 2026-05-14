# Bitácora — Práctica 8

## ¿Cómo fue el proceso?

Primero definí el dominio que iba a usar: una tienda con productos. Me pareció un buen ejemplo porque permite justificar bien cada colección.

---

### Commit 1 — Dominio y comparadores

Lo primero fue crear la clase `Producto` con todos sus atributos. Le implementé `Comparable` usando el precio porque es el orden más natural cuando alguien busca productos. También hice los `Comparator` en una clase aparte para tener los criterios de ordenamiento listos antes de usarlos en la gestora.

Archivos:
- `domain/Producto.java`
- `comparators/ProductoComparators.java`

---

### Commit 2 — Gestora con las cuatro colecciones

Aquí estuve pensando cuál colección usar para cada cosa. Al final quedó así:

- `HashMap` para buscar por ID rápido
- `ArrayList` para listar y usar Streams
- `LinkedList` como historial de los últimos agregados
- `HashSet` para guardar las categorías sin repetidos

También implementé todo el CRUD, los métodos de Stream, el iterador para borrar productos sin stock y la búsqueda compuesta por precio y stock.

Archivos:
- `collections/GestoraProductos.java`
- `collections/DatosPrueba.java`

---

### Commit 3 — Menú, pruebas y documentación

Hice el menú interactivo con todas las opciones. También hice el `TestRunner` para probar todo sin necesitar JUnit. Corrí las pruebas y salieron 21/21.

Al final escribí el README, la reflexión y esta bitácora.

Archivos:
- `menu/Menu.java`
- `TestRunner.java`
- `pom.xml`
- `README.md`
- `REFLEXION.md`
- `BITACORA.md`

---

**Resultado final:** 21 pruebas, 0 fallos.
