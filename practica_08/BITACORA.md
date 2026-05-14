# Bitácora — Práctica 8

## ¿Cómo fue el proceso?

Primero definí el dominio que iba a usar: una tienda con productos. Me pareció un buen ejemplo porque permite justificar bien cada colección.

---

### Commit 1 — Dominio y comparadores

Lo primero fue crear la clase `Producto` con todos sus atributos. Le implementé `Comparable` usando el precio porque es el orden más natural cuando alguien busca productos. También hice los `Comparator` en una clase aparte para tener los criterios de ordenamiento listos antes de usarlos en la gestora.



### Commit 2 — Gestora con las cuatro colecciones

- `HashMap` para buscar por ID rápido
- `ArrayList` para listar y usar Streams
- `LinkedList` como historial de los últimos agregados
- `HashSet` para guardar las categorías sin repetidos







