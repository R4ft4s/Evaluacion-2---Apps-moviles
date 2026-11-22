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
    entities = [OportunidadEntity::class, GestionSeguimientoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oportunidadDao(): OportunidadDao
    abstract fun gestionSeguimientoDao(): GestionSeguimientoDao

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
                    .fallbackToDestructiveMigration(dropAllTables = true)
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
                        populateDatabase(database.oportunidadDao(), database.gestionSeguimientoDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: OportunidadDao, seguimientoDao: GestionSeguimientoDao) {
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
                    ubicacion = "Providencia, Santiago",
                    fechaDeteccion = "2026-02-15",
                    organizacion = "Municipalidad de Providencia",
                    contactoNombre = "Claudia Morales",
                    contactoTelefono = "+56 9 8765 4321",
                    publicoEsperado = "800 personas",
                    responsable = "Ejecutivo Comercial",
                    condicionesEspacio = "Stand 3x3 mts con electricidad",
                    condicionesCosto = "$150.000 CLP",
                    condicionesRequisitos = "Tener credenciales de salud al día",
                    proximaAccion = "Enviar propuesta formal de convenio"
                ),
                OportunidadEntity(
                    nombre = "Jornada de Beneficios CorpBanmédica",
                    tipo = "Jornada de beneficios",
                    descripcion = "Actividad interna para la difusión de convenios de salud y prestaciones corporativas.",
                    fuente = "Contacto Directo / Referencia",
                    prioridad = "Alta",
                    potencial = "Alto",
                    estado = "Propuesta enviada",
                    observaciones = "Pendiente confirmación de fecha para stand interactivo.",
                    ubicacion = "Las Condes, Santiago",
                    fechaDeteccion = "2026-02-20",
                    organizacion = "CorpBanmédica Chile",
                    contactoNombre = "Roberto Fuentes",
                    contactoTelefono = "+56 9 7654 3210",
                    publicoEsperado = "1200 empleados",
                    responsable = "Ejecutivo Comercial",
                    condicionesEspacio = "Toldo exterior y mesón promocional",
                    condicionesCosto = "Sin costo organizador",
                    condicionesRequisitos = "Firma de acuerdo de confidencialidad",
                    proximaAccion = "Reunión de confirmación técnica"
                ),
                OportunidadEntity(
                    nombre = "Expo Bienestar Municipal Puente Alto",
                    tipo = "Actividad municipal",
                    descripcion = "Evento masivo para funcionarios municipales y familias en complejo deportivo.",
                    fuente = "Diario Oficial y Redes Municipales",
                    prioridad = "Media",
                    potencial = "Medio",
                    estado = "Participación confirmada",
                    observaciones = "Puesto asignado sector B. Coordinar equipamiento gráfico.",
                    ubicacion = "Puente Alto, Santiago",
                    fechaDeteccion = "2026-03-01",
                    organizacion = "DIDECO Puente Alto",
                    contactoNombre = "Loreto Peña",
                    contactoTelefono = "+56 9 5432 1098",
                    publicoEsperado = "2500 asistentes",
                    responsable = "Gestión de Activaciones",
                    condicionesEspacio = "Módulo techado 4x4",
                    condicionesCosto = "$50.000 CLP",
                    condicionesRequisitos = "Banner y pendones corporativos Atenas",
                    resultadoActivacion = "Alta recepción de colaboradores corporativos",
                    impactoEvaluacion = "95% satisfacción de asistentes",
                    proximaAccion = "Registrar reporte final de activación"
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
                    ubicacion = "Maipú, Santiago",
                    fechaDeteccion = "2026-03-05",
                    organizacion = "Automotriz Central Maipú",
                    contactoNombre = "Gonzalo Silva",
                    contactoTelefono = "+56 9 1234 5678",
                    publicoEsperado = "400 personas",
                    responsable = "Supervisor Comercial",
                    proximaAccion = "Llamada inicial a encargado de operaciones"
                )
            )

            for (item in initialOpportunities) {
                val id = dao.insertOportunidad(item)
                seguimientoDao.insertSeguimiento(
                    GestionSeguimientoEntity(
                        oportunidadId = id.toInt(),
                        fecha = item.fechaDeteccion,
                        usuario = item.responsable,
                        tipoAccion = "Registro Inicial",
                        detalle = "Oportunidad creada en el sistema con estado inicial: ${item.estado}",
                        resultado = "Oportunidad activa"
                    )
                )
            }
        }
    }
}
