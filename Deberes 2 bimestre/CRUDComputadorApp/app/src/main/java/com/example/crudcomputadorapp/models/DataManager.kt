package com.example.crudcomputadorapp.models

import android.content.Context
import com.example.crudcomputadorapp.database.AppDatabase
import kotlinx.coroutines.flow.Flow

class DataManager(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val computadorDao = db.computadorDao()
    private val componenteDao = db.componenteDao()

    // Obtener todos los computadores
    fun obtenerComputadores(): Flow<List<Computador>> {
        return computadorDao.obtenerComputadores()
    }

    // Insertar un nuevo computador
    suspend fun agregarComputador(computador: Computador) {
        computadorDao.insertarComputador(computador)
    }

    // Actualizar un computador existente
    suspend fun actualizarComputador(computador: Computador) {
        computadorDao.actualizarComputador(computador)
    }

    // Eliminar un computador
    suspend fun eliminarComputador(computador: Computador) {
        computadorDao.eliminarComputador(computador)
    }

    // Obtener componentes de un computador específico
    fun obtenerComponentes(computadorId: Int): Flow<List<Componente>> {
        return componenteDao.obtenerComponentes(computadorId)
    }

    // Insertar un componente a un computador
    suspend fun agregarComponente(componente: Componente) {
        componenteDao.insertarComponente(componente)
    }

    suspend fun actualizarUbicacion(id: Int, latitud: Double, longitud: Double) {
        computadorDao.actualizarUbicacion(id, latitud, longitud)
    }

}
