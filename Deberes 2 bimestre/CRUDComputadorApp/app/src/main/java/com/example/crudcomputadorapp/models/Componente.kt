package com.example.crudcomputadorapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

@Entity(
    tableName = "componentes",
    foreignKeys = [ForeignKey(
        entity = Computador::class,
        parentColumns = ["id"],
        childColumns = ["computador_id"],
        onDelete = ForeignKey.CASCADE // 🔥 IMPORTANTE: Elimina los componentes automáticamente
    )]
)
data class Componente(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "tipo") val tipo: String,
    @ColumnInfo(name = "precio") val precio: Double,
    @ColumnInfo(name = "cantidad") val cantidad: Int,
    @ColumnInfo(name = "es_reemplazable") val esReemplazable: Boolean,
    @ColumnInfo(name = "computador_id") val computadorId: Int // 🔗 Relación con Computador
)
