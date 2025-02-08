package com.example.crudcomputadorapp.database

import androidx.room.*
import com.example.crudcomputadorapp.models.Componente
import kotlinx.coroutines.flow.Flow

@Dao
interface ComponenteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarComponente(componente: Componente)

    @Query("SELECT * FROM componentes WHERE computador_id = :computadorId")
    fun obtenerComponentes(computadorId: Int): Flow<List<Componente>>
}
