package com.example.crudcomputadorapp.models

object DataManager {
    val computadores: MutableList<Computador> = mutableListOf()

    fun agregarComputador(computador: Computador) {
        computadores.add(computador)
    }

    fun obtenerComputadores(): List<Computador> {
        return computadores
    }

    fun eliminarComputador(id: Int) {
        computadores.removeIf { it.id == id }
    }
}
