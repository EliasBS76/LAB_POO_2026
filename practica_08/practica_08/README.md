# Práctica 8 — Colecciones, Streams y Ordenamiento

## Estructura del proyecto

```
practica_08/
├── src/
│   └── main/java/practica8/
│       ├── domain/
│       │   └── Producto.java              ← Comparable por precio
│       ├── comparators/
│       │   └── ProductoComparators.java   ← Comparator ×3
│       ├── collections/
│       │   ├── GestoraProductos.java      ← CRUD + Streams + iteradores
│       │   └── DatosPrueba.java           ← 18 registros de prueba
│       ├── menu/
│       │   └── Menu.java                  ← Menú interactivo
│       └── TestRunner.java
├── README.md
├── REFLEXION.md
├── BITACORA.md
└── pom.xml
```

## Estructuras de colección usadas

| Estructura | Dónde | Por qué |
|---|---|---|
| `HashMap<Integer, Producto>` | Catálogo principal | Búsqueda por ID en O(1) |
| `ArrayList<Producto>` | Lista ordenada | Lectura rápida por índice, compatible con Streams |
| `LinkedList<Producto>` | Historial reciente | Inserción/eliminación O(1) al inicio (FIFO) |
| `HashSet<String>` | Categorías | Unicidad automática, contains en O(1) |

## Elemento de Decisión Propia — Búsqueda compuesta

**Consulta implementada:** productos cuyo precio está en un rango `[min, max]` Y cuyo stock supera un mínimo dado.

```java
gestora.buscarPorPrecioYStock(precioMin, precioMax, stockMinimo)
```

**Por qué es relevante:** el encargado de compras necesita identificar artículos que sean accesibles para el cliente (precio acotado) y que además tengan suficiente inventario para cubrir una promoción. Una sola condición no basta — precio barato con stock cero no sirve, y stock alto con precio fuera de rango tampoco.

## Cómo ejecutar

```bash
# Compilar
find src/main -name "*.java" | xargs javac -d out/main

# Pruebas automáticas
java -cp out/main practica8.TestRunner

# Menú interactivo
java -cp out/main practica8.menu.Menu

# Con Maven
mvn compile exec:java -Dexec.mainClass="practica8.menu.Menu"
```
