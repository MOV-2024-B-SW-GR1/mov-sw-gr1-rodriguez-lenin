package org.example

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val rutaEquipos = "src/main/resources/equipos.txt"
    val rutaJugadores = "src/main/resources/jugadores.txt"

    // Crear archivos si no existen
    File(rutaEquipos).apply { if (!exists()) createNewFile() }
    File(rutaJugadores).apply { if (!exists()) createNewFile() }

    println("Bienvenido al sistema de gestión de Equipos y Jugadores")
    var opcion: Int
    do {
        println(
            """
            |Seleccione una opción:
            |1. Gestionar Equipos
            |2. Gestionar Jugadores
            |3. Salir
            """.trimMargin()
        )
        opcion = readln().toInt()

        when (opcion) {
            1 -> menuEquipos(rutaEquipos)
            2 -> menuJugadores(rutaJugadores)
            3 -> println("Saliendo del sistema...")
            else -> println("Opción no válida. Intente de nuevo.")
        }
    } while (opcion != 3)
}

// Funciones para equipos
data class Equipo(
    val id: Int,
    val nombre: String,
    val ciudad: String,
    val fundacion: Int,
    val activo: Boolean // Boolean
)

fun crearEquipo(equipo: Equipo, ruta: String) {
    val line = "${equipo.id},${equipo.nombre},${equipo.ciudad},${equipo.fundacion},${equipo.activo}"
    val datos = leerArchivo(ruta).toMutableList()
    datos.add(line)
    escribirArchivo(ruta, datos)
}

fun leerEquipos(ruta: String): List<Equipo> {
    return leerArchivo(ruta).map { linea ->
        val campos = linea.split(",")
        Equipo(
            campos[0].toInt(),
            campos[1],
            campos[2],
            campos[3].toInt(),
            campos[4].toBoolean()
        )
    }
}

fun actualizarEquipo(id: Int, nuevoEquipo: Equipo, ruta: String) {
    val equipos = leerEquipos(ruta).map {
        if (it.id == id) nuevoEquipo else it
    }
    val datos = equipos.map { "${it.id},${it.nombre},${it.ciudad},${it.fundacion},${it.activo}" }
    escribirArchivo(ruta, datos)
}

fun eliminarEquipo(id: Int, ruta: String) {
    val equipos = leerEquipos(ruta).filter { it.id != id }
    val datos = equipos.map { "${it.id},${it.nombre},${it.ciudad},${it.fundacion},${it.activo}" }
    escribirArchivo(ruta, datos)
}

fun menuEquipos(ruta: String) {
    println("Gestionar Equipos:")
    println("1. Crear Equipo")
    println("2. Mostrar Equipos")
    println("3. Actualizar Equipo")
    println("4. Eliminar Equipo")
    println("Seleccione una opción:")
    val opcion = readln().toInt()

    when (opcion) {
        1 -> {
            println("Ingrese el ID, Nombre, Ciudad, Fundación (año) y si está activo (true/false):")
            val datos = readln().split(",")
            crearEquipo(
                Equipo(
                    datos[0].toInt(),
                    datos[1],
                    datos[2],
                    datos[3].toInt(),
                    datos[4].toBoolean()
                ),
                ruta
            )
        }
        2 -> leerEquipos(ruta).forEach { println(it) }
        3 -> {
            println("Ingrese el ID del equipo a actualizar:")
            val id = readln().toInt()
            println("Ingrese el nuevo Nombre, Ciudad, Fundación (año) y si está activo (true/false):")
            val datos = readln().split(",")
            actualizarEquipo(
                id,
                Equipo(
                    id,
                    datos[0],
                    datos[1],
                    datos[2].toInt(),
                    datos[3].toBoolean()
                ),
                ruta
            )
        }
        4 -> {
            println("Ingrese el ID del equipo a eliminar:")
            val id = readln().toInt()
            eliminarEquipo(id, ruta)
        }
        else -> println("Opción no válida.")
    }
}

// Funciones para jugadores
data class Jugador(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val idEquipo: Int,
    val salario: Double // Double
)

fun crearJugador(jugador: Jugador, ruta: String) {
    val line = "${jugador.id},${jugador.nombre},${jugador.edad},${jugador.idEquipo},${jugador.salario}"
    val datos = leerArchivo(ruta).toMutableList()
    datos.add(line)
    escribirArchivo(ruta, datos)
}

fun leerJugadores(ruta: String): List<Jugador> {
    return leerArchivo(ruta).map { linea ->
        val campos = linea.split(",")
        Jugador(
            campos[0].toInt(),
            campos[1],
            campos[2].toInt(),
            campos[3].toInt(),
            campos[4].toDouble()
        )
    }
}

fun actualizarJugador(id: Int, nuevoJugador: Jugador, ruta: String) {
    val jugadores = leerJugadores(ruta).map {
        if (it.id == id) nuevoJugador else it
    }
    val datos = jugadores.map { "${it.id},${it.nombre},${it.edad},${it.idEquipo},${it.salario}" }
    escribirArchivo(ruta, datos)
}

fun eliminarJugador(id: Int, ruta: String) {
    val jugadores = leerJugadores(ruta).filter { it.id != id }
    val datos = jugadores.map { "${it.id},${it.nombre},${it.edad},${it.idEquipo},${it.salario}" }
    escribirArchivo(ruta, datos)
}
fun menuJugadores(ruta: String) {
    println("Gestionar Jugadores:")
    println("1. Crear Jugador")
    println("2. Mostrar Jugadores")
    println("3. Actualizar Jugador")
    println("4. Eliminar Jugador")
    println("Seleccione una opción:")
    val opcion = readln().toInt()

    when (opcion) {
        1 -> {
            println("Ingrese el ID, Nombre, Edad, ID de Equipo y Salario:")
            val datos = readln().split(",")
            val idEquipo = datos[3].toInt()

            // Validar si el equipo existe
            val equipos = leerEquipos("src/main/resources/equipos.txt")
            if (equipos.any { it.id == idEquipo }) {
                // Crear jugador si el equipo existe
                crearJugador(
                    Jugador(
                        datos[0].toInt(),
                        datos[1],
                        datos[2].toInt(),
                        idEquipo,
                        datos[4].toDouble()
                    ),
                    ruta
                )
                println("Jugador creado exitosamente.")
            } else {
                // Equipo no existe
                println("El equipo con ID $idEquipo no existe. Regresando al menú principal...")
                return // Regresa al menú principal
            }
        }
        2 -> leerJugadores(ruta).forEach { println(it) }
        3 -> {
            println("Ingrese el ID del jugador a actualizar:")
            val id = readln().toInt()
            println("Ingrese el nuevo Nombre, Edad, ID de Equipo y Salario:")
            val datos = readln().split(",")
            actualizarJugador(
                id,
                Jugador(
                    id,
                    datos[0],
                    datos[1].toInt(),
                    datos[2].toInt(),
                    datos[3].toDouble()
                ),
                ruta
            )
        }
        4 -> {
            println("Ingrese el ID del jugador a eliminar:")
            val id = readln().toInt()
            eliminarJugador(id, ruta)
        }
        else -> println("Opción no válida.")
    }
}


// Funciones comunes para manejo de archivos
fun leerArchivo(ruta: String): List<String> {
    return File(ruta).readLines()
}

fun escribirArchivo(ruta: String, datos: List<String>) {
    File(ruta).writeText(datos.joinToString("\n"))
}
