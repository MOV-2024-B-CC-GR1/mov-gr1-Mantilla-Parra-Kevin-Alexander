package com.example.crudcomputadorapp.models

import androidx.room.Embedded
import androidx.room.Relation

data class ComputadorConComponentes(
    @Embedded val computador: Computador,
    @Relation(
        parentColumn = "id",
        entityColumn = "computador_id"
    )
    val componentes: List<Componente>
)

