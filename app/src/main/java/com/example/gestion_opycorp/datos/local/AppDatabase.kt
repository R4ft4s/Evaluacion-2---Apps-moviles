package com.example.gestion_opycorp.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [OportunidadEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oportunidadDao(): OportunidadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestion_opycorp_db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.oportunidadDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: OportunidadDao) {
            val initialOpportunities = listOf(
                OportunidadEntity(
                    nombre = "Feria de Salud Dental Providencia",
                    tipo = "Feria empresarial",
                    descripcion = "Feria anual de bienestar para colaboradores de clínicas y empresas de Providencia.",
                    fuente = "Sitio Web Municipal / Redes Sociales",
                    prioridad = "Alta",
                    potencial = "Alto",
                    estado = "Contacto realizado",
                    observaciones = "Se envió presentación inicial a encargada de RRHH.",
                    ubicacion = "Av. Providencia 1200, Santiago",
                    fechaDeteccion = "2026-02-15"
                ),
                OportunidadEntity(
                    nombre = "Jornada de Beneficios CorpBanmédica",
                    tipo = "Jornada de beneficios",
                    descripcion = "Actividad interna para la difusion de convenios de salud y prestaciones corporativas.",
                    fuente = "Contacto Directo / Referencia",
                    prioridad = "Alta",
                    potencial = "Alto",
                    estado = "Propuesta enviada",
                    observaciones = "Pendiente confirmación de fecha para stand interactivo.",
                    ubicacion = "Av. Apoquindo 3000, Las Condes",
                    fechaDeteccion = "2026-02-20"
                ),
                OportunidadEntity(
                    nombre = "Expo Bienestar Municipal Puente Alto",
                    tipo = "Actividad municipal",
                    descripcion = "Evento masivo para funcionarios municipales y familias en complejo deportivo.",
                    fuente = "Diario Oficial y Redes Municipales",
                    prioridad = "Media",
                    potencial = "Medio",
                    estado = "Evento identificado",
                    observaciones = "Identificado puesto disponible para atención dental preventiva.",
                    ubicacion = "Concha y Toro 1820, Puente Alto",
                    fechaDeteccion = "2026-03-01"
                ),
                OportunidadEntity(
                    nombre = "Activación Comercial Servicios Automotrices Maipú",
                    tipo = "Activación comercial",
                    descripcion = "Punto de atención para flota de vehículos corporativos y colaboradores.",
                    fuente = "Búsqueda Comercial Directa",
                    prioridad = "Baja",
                    potencial = "Medio",
                    estado = "Contacto pendiente",
                    observaciones = "Revisar factibilidad de instalar módulo móvil.",
                    ubicacion = "Av. Pajaritos 2400, Maipú",
                    fechaDeteccion = "2026-03-05"
                )
            )

            for (item in initialOpportunities) {
                dao.insertOportunidad(item)
            }
        }
    }
}
