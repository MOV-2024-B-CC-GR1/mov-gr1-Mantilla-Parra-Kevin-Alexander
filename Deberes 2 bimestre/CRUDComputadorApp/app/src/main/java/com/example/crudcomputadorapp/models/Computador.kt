package com.example.crudcomputadorapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "computadores")
data class Computador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "marca") val marca: String,
    @ColumnInfo(name = "precio") val precio: Double,
    @ColumnInfo(name = "fecha_fabricacion") val fechaFabricacion: String,
    @ColumnInfo(name = "es_portatil") val esPortatil: Boolean,
    @ColumnInfo(name = "latitud") val latitud: Double? = null,
    @ColumnInfo(name = "longitud") val longitud: Double? = null
)
