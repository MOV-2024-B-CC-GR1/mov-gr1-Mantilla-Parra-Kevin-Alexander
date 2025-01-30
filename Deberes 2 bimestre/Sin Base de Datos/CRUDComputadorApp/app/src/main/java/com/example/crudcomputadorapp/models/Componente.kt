package com.example.crudcomputadorapp.models

data class Componente(
    var id: Int, // Asegúrate de que sea "Int" y no "Long" o "Int?"
    var nombre: String,
    var tipo: String,
    var precio: Double,
    var cantidad: Int,
    var esReemplazable: Boolean
)
