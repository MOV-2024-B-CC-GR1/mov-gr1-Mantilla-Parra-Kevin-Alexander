package com.example.crudcomputadorapp.database

import androidx.room.*
import com.example.crudcomputadorapp.models.Computador
import com.example.crudcomputadorapp.models.ComputadorConComponentes
import kotlinx.coroutines.flow.Flow

@Dao
interface ComputadorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarComputador(computador: Computador): Long

    @Update
    suspend fun actualizarComputador(computador: Computador)

    @Delete
    suspend fun eliminarComputador(computador: Computador)

    @Query("SELECT * FROM computadores")
    fun obtenerComputadores(): Flow<List<Computador>>

    @Transaction
    @Query("SELECT * FROM computadores WHERE id = :id")
    fun obtenerComputadorConComponentes(id: Int): Flow<ComputadorConComponentes>

    @Transaction
    @Query("SELECT * FROM computadores")
    fun obtenerTodosConComponentes(): Flow<List<ComputadorConComponentes>> // NUEVO

    @Query("UPDATE computadores SET latitud = :lat, longitud = :lng WHERE id = :id")
    suspend fun actualizarUbicacion(id: Int, lat: Double, lng: Double)

}
