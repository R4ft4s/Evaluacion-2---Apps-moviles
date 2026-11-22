package com.example.gestion_opycorp.datos.repository

import com.example.gestion_opycorp.datos.local.GestionSeguimientoDao
import com.example.gestion_opycorp.datos.local.GestionSeguimientoEntity
import com.example.gestion_opycorp.datos.local.OportunidadDao
import com.example.gestion_opycorp.datos.local.OportunidadEntity
import com.example.gestion_opycorp.datos.remote.EventoExternoModel
import com.example.gestion_opycorp.datos.remote.OportunidadApiService
import com.example.gestion_opycorp.datos.remote.RetrofitClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OportunidadRepository(
    private val dao: OportunidadDao,
    private val seguimientoDao: GestionSeguimientoDao? = null,
    private val apiService: OportunidadApiService = RetrofitClient.apiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getOportunidades(): Flow<List<OportunidadEntity>> {
        return dao.getOportunidades()
    }

    fun getOportunidadById(id: Int): Flow<OportunidadEntity?> {
        return dao.getOportunidadById(id)
    }

    suspend fun getOportunidadByIdDirect(id: Int): OportunidadEntity? {
        return dao.getOportunidadByIdDirect(id)
    }

    suspend fun saveOportunidad(oportunidad: OportunidadEntity, usuario: String = "Sistema"): Long {
        return withContext(dispatcher) {
            val isNew = oportunidad.id == 0
            val savedId = if (isNew) {
                dao.insertOportunidad(oportunidad)
            } else {
                dao.updateOportunidad(oportunidad)
                oportunidad.id.toLong()
            }

            // Record history / follow-up log
            seguimientoDao?.insertSeguimiento(
                GestionSeguimientoEntity(
                    oportunidadId = savedId.toInt(),
                    fecha = "2026-03-08",
                    usuario = usuario,
                    tipoAccion = if (isNew) "Creación" else "Actualización",
                    detalle = "Oportunidad '${oportunidad.nombre}' guardada con estado '${oportunidad.estado}'. Próxima acción: ${oportunidad.proximaAccion.ifBlank { "N/A" }}"
                )
            )

            // Try Sync to Remote API REST
            try {
                if (isNew) {
                    apiService.createOportunidad(oportunidad)
                } else {
                    apiService.updateOportunidad(savedId.toInt(), oportunidad)
                }
            } catch (_: Exception) {
                // Offline fallback - database saved locally
            }

            savedId
        }
    }

    suspend fun deleteOportunidad(oportunidad: OportunidadEntity) {
        withContext(dispatcher) {
            dao.deleteOportunidad(oportunidad)
            try {
                apiService.deleteOportunidad(oportunidad.id)
            } catch (_: Exception) {}
        }
    }

    suspend fun deleteOportunidadById(id: Int) {
        withContext(dispatcher) {
            dao.deleteOportunidadById(id)
            try {
                apiService.deleteOportunidad(id)
            } catch (_: Exception) {}
        }
    }

    // Gestion de Seguimientos / Historial
    fun getSeguimientosForOportunidad(oportunidadId: Int): Flow<List<GestionSeguimientoEntity>>? {
        return seguimientoDao?.getSeguimientosForOportunidad(oportunidadId)
    }

    suspend fun agregarSeguimiento(seguimiento: GestionSeguimientoEntity) {
        withContext(dispatcher) {
            seguimientoDao?.insertSeguimiento(seguimiento)
        }
    }

    // Retrofit API REST Consumo: Catálogo de Eventos Externos
    suspend fun fetchEventosExternos(): List<EventoExternoModel> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getEventosExternos()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                } else {
                    RetrofitClient.getMockEventosExternos()
                }
            } catch (_: Exception) {
                // Offline or local server not running fallback
                RetrofitClient.getMockEventosExternos()
            }
        }
    }

    // Convert API External Event to Local Opportunity (1-Click Import)
    suspend fun importarEventoExternoComoOportunidad(evento: EventoExternoModel, usuario: String): Long {
        val nuevaOportunidad = OportunidadEntity(
            nombre = evento.nombre,
            tipo = evento.tipo,
            descripcion = evento.descripcion,
            fuente = "API REST Externa (${evento.organizacion})",
            prioridad = "Media",
            potencial = "Alto",
            estado = "Evento identificado",
            ubicacion = "${evento.comuna}, Santiago",
            fechaDeteccion = evento.fecha,
            organizacion = evento.organizacion,
            publicoEsperado = evento.publicoEsperado,
            responsable = usuario,
            proximaAccion = "Contactar encargada de la organización"
        )
        return saveOportunidad(nuevaOportunidad, usuario)
    }
}
