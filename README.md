# Juego Matrix

**Autor:** David Santiago Palacios  
**Curso:** ARSW  
**Proyecto:** Simulación de un juego estilo Matrix

---

## Descripción

Este proyecto implementa un juego de consola inspirado en Matrix, donde el jugador controla a **Nio**, un personaje que debe escapar de los **agentes** para llegar a un teléfono y salir del sistema. El juego se desarrolla en una matriz de 8x8 casillas, donde:

- `N` representa a Nio
- `A` representa un Agente
- `T` representa el Teléfono (meta)
- `.` representa una casilla vacía

El juego utiliza **hilos en Java** para simular el movimiento concurrente de Nio y los agentes, donde cada entidad se mueve automáticamente una casilla por turno.

---

## Cómo jugar / Ejecutar

1. Clonar o descargar el proyecto.
2. Compilar las clases.
3. Ejecutar el juego con el main que está en Game.
4. Observar cómo Nio intenta escapar hacia el teléfono mientras los agentes lo persiguen.
5. Cada turno se imprime en consola mostrando la posición de Nio, los agentes y el teléfono.

---

## Mecánica del juego

- El juego se desarrolla por **turnos**:
    - **Turno de Nio:** Nio se mueve una casilla hacia el teléfono.
    - **Turno de Agentes:** Cada agente se mueve una casilla hacia Nio.
- Después de cada turno, la matriz completa se imprime en consola.
- El juego termina cuando:
    - Nio alcanza el teléfono (**victoria de Nio**)
    - Algún agente alcanza la posición de Nio (**derrota de Nio**)

---

## Estrategia implementada

1. **Movimiento de Nio:**  
   Nio calcula la dirección hacia el teléfono (`x` y `y`) y se mueve **una casilla por turno**.
2. **Movimiento de agentes:**  
   Cada agente calcula la dirección hacia Nio (`x` y `y`) y se mueve **una casilla por turno**.
3. **Sincronización con hilos:**  
   Se utiliza un objeto `lock` para controlar los turnos y evitar movimientos múltiples por turno.  
   Nio y los agentes esperan su turno mediante `wait()` y se despiertan mediante `notifyAll()` controlado por la clase `Game`.
4. **Impresión del tablero:**  
   Después de cada turno, la matriz se imprime mostrando todas las posiciones actuales de los jugadores y el teléfono.

---

## Ejemplo de tablero inicial

----- TURNO -----  
. . . . . . . .  
. . . . . . . .  
. . . . . . . .  
. . . N . . . .  
. . . T . . . .  
. . . . . . . .  
. . . . . . . .  
. . . . . . . .

- `N` en `(3,3)` es Nio
- `T` en `(4,4)` es el teléfono
- Los agentes aún no se han movido

---

## Ejemplo de un turno

Nio se mueve y los agentes también:

Nio se movió a `(4,4)`  
Agente se movió a `(3,4)`

----- TURNO -----  
. . . . . . . .  
. . . . . . . .  
. . . . . . . .  
. . . A . . . .  
. . . N . . . .  
. . . . . . . .  
. . . . . . . .  
. . . . . . . .  
----- FIN DE TURNO -----

- Se puede ver que Nio y los agentes se mueven **una sola vez por turno**
- La matriz refleja la posición actual de cada entidad

---

## Objetivos del proyecto

- Practicar el manejo de **hilos en Java** y sincronización de turnos.
- Implementar una **simulación simple de IA** para el movimiento de agentes y jugador.
- Crear una **interfaz de consola clara y visualmente comprensible** para seguimiento de turnos.

---
