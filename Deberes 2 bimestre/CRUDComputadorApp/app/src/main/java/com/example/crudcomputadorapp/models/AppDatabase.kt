package com.example.crudcomputadorapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crudcomputadorapp.models.Computador
import com.example.crudcomputadorapp.models.Componente
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Computador::class, Componente::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun computadorDao(): ComputadorDao
    abstract fun componenteDao(): ComponenteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "computador_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 🔥 Definir migración de versión 1 a 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE computadores ADD COLUMN latitud REAL DEFAULT NULL")
                database.execSQL("ALTER TABLE computadores ADD COLUMN longitud REAL DEFAULT NULL")
            }
        }
    }
}

