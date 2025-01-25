package com.example.crudcomputadorapp.models

data class Computador(
    var id: Int, // Asegúrate de que sea "Int" y no "Long" o "Int?"
    var nombre: String,
    var marca: String,
    var precio: Double,
    var fechaFabricacion: String,
    var esPortatil: Boolean,
    var componentes: MutableList<Componente> = mutableListOf()
)
