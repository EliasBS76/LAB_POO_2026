1. ¿Qué es la serialización y cuándo es útil en comparación con guardar
   texto plano?
2. ¿Por qué usamos =BufferedReader= en lugar de leer byte a byte? ¿Qué
   mejora en rendimiento ofrece?
3. ¿Qué riesgos tiene no cerrar un archivo después de usarlo? ¿Cómo los
   mitigaste?


   1 R:La serialización es el proceso de convertir el estado de un objeto en memoria (junto con todas sus propiedades) en una secuencia de bytes para poder guardarlo en disco o enviarlo por red.

   2 R: Leer información del disco duro físico es una de las operaciones más lentas en la programación. Si lees byte a byte, el programa tiene que pedirle permiso al sistema operativo y acceder al disco por cada letra, lo que paraliza el rendimiento. BufferedReader mejora esto leyendo un bloque enorme de datos de una sola vez y guardándolo temporalmente en la memoria RAM (el buffer).

   3 R: No cerrar un flujo de datos (Stream) conlleva tres riesgos principales: