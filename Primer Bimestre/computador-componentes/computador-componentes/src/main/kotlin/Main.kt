import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Computador(
    val id: Int,
    var nombre: String,
    var marca: String,
    var fechaCompra: String,
    var precio: Double,
    val componentes: List<Componente> = emptyList()
)

@Serializable
data class Componente(
    val id: Int,
    var tipo: String,
    var descripcion: String,
    var estado: Boolean,
    var garantia: Int
)

const val FILE_NAME = "computadores.json"

// Guarda la lista de computadores en un archivo JSON
fun guardarDatos(computadores: List<Computador>) {
    try {
        val json = Json.encodeToString(computadores)
        File(FILE_NAME).writeText(json)
        println("Datos guardados exitosamente.")
    } catch (e: Exception) {
        println("Error al guardar datos: ${e.message}")
    }
}

// Carga la lista de computadores desde un archivo JSON
fun cargarDatos(): MutableList<Computador> {
    return try {
        if (File(FILE_NAME).exists()) {
            val json = File(FILE_NAME).readText()
            Json.decodeFromString(json)
        } else {
            mutableListOf()
        }
    } catch (e: Exception) {
        println("Error al cargar datos: ${e.message}")
        mutableListOf()
    }
}

fun mostrarMenu() {
    println(
        """
        --- MENÚ ---
        1. Crear Computador
        2. Leer Computadores
        3. Actualizar Computador
        4. Eliminar Computador
        5. Salir
        Selecciona una opción:
    """.trimIndent()
    )
}

fun leerEntradaInt(mensaje: String): Int {
    while (true) {
        try {
            print("$mensaje: ")
            return readln().toInt()
        } catch (e: NumberFormatException) {
            println("Por favor, ingresa un número válido.")
        }
    }
}

fun leerEntradaDouble(mensaje: String): Double {
    while (true) {
        try {
            print("$mensaje: ")
            return readln().toDouble()
        } catch (e: NumberFormatException) {
            println("Por favor, ingresa un número válido.")
        }
    }
}

fun crearComputador(computadores: MutableList<Computador>) {
    val id = leerEntradaInt("Ingresa el ID del computador")
    print("Ingresa el nombre del computador: ")
    val nombre = readln()
    print("Ingresa la marca del computador: ")
    val marca = readln()
    print("Ingresa la fecha de compra (YYYY-MM-DD): ")
    val fechaCompra = readln()
    val precio = leerEntradaDouble("Ingresa el precio del computador")

    val componentes = mutableListOf<Componente>()
    val numComponentes = leerEntradaInt("¿Cuántos componentes tiene este computador?")

    for (i in 1..numComponentes) {
        val compId = leerEntradaInt("Componente $i: Ingresa el ID del componente")
        print("Ingresa el tipo de componente: ")
        val tipo = readln()
        print("Ingresa la descripción del componente: ")
        val descripcion = readln()
        val estado = leerEntradaBoolean("¿El componente está en buen estado? (true/false)")
        val garantia = leerEntradaInt("Ingresa los meses de garantía del componente")
        componentes.add(Componente(compId, tipo, descripcion, estado, garantia))
    }

    computadores.add(Computador(id, nombre, marca, fechaCompra, precio, componentes))
    guardarDatos(computadores)
    println("Computador agregado correctamente.")
}

fun leerEntradaBoolean(mensaje: String): Boolean {
    while (true) {
        print("$mensaje: ")
        when (val input = readln().lowercase()) {
            "true" -> return true
            "false" -> return false
            else -> println("Por favor, ingresa 'true' o 'false'.")
        }
    }
}

fun leerComputadores(computadores: List<Computador>) {
    if (computadores.isEmpty()) {
        println("No hay computadores registrados.")
        return
    }

    computadores.forEach { computador ->
        println("\nID: ${computador.id}")
        println("Nombre: ${computador.nombre}")
        println("Marca: ${computador.marca}")
        println("Fecha de compra: ${computador.fechaCompra}")
        println("Precio: ${computador.precio}")
        println("Componentes:")
        computador.componentes.forEach { componente ->
            println("  - ID: ${componente.id}, Tipo: ${componente.tipo}, Descripción: ${componente.descripcion}, Estado: ${componente.estado}, Garantía: ${componente.garantia} meses")
        }
    }
}

fun actualizarComputador(computadores: MutableList<Computador>) {
    val id = leerEntradaInt("Ingresa el ID del computador a actualizar")
    val computador = computadores.find { it.id == id }

    if (computador != null) {
        print("Ingresa el nuevo nombre del computador: ")
        computador.nombre = readln()
        print("Ingresa la nueva marca del computador: ")
        computador.marca = readln()
        print("Ingresa la nueva fecha de compra (YYYY-MM-DD): ")
        computador.fechaCompra = readln()
        computador.precio = leerEntradaDouble("Ingresa el nuevo precio del computador")

        guardarDatos(computadores)
        println("Computador actualizado correctamente.")
    } else {
        println("Computador no encontrado.")
    }
}

fun eliminarComputador(computadores: MutableList<Computador>) {
    val id = leerEntradaInt("Ingresa el ID del computador a eliminar")
    if (computadores.removeIf { it.id == id }) {
        guardarDatos(computadores)
        println("Computador eliminado correctamente.")
    } else {
        println("Computador no encontrado.")
    }
}

fun main() {
    val computadores = cargarDatos()

    while (true) {
        mostrarMenu()
        when (leerEntradaInt("Selecciona una opción")) {
            1 -> crearComputador(computadores)
            2 -> leerComputadores(computadores)
            3 -> actualizarComputador(computadores)
            4 -> eliminarComputador(computadores)
            5 -> {
                println("Saliendo del programa...")
                break
            }
            else -> println("Opción no válida. Intenta nuevamente.")
        }
    }
}
