package com.example.gestion_opycorp.datos.repository

import com.example.gestion_opycorp.datos.local.OportunidadDao
import com.example.gestion_opycorp.datos.local.OportunidadEntity
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(private val dao: OportunidadDao) {

    fun getOportunidades(): Flow<List<OportunidadEntity>> {
        return dao.getOportunidades()
    }

    fun getOportunidadById(id: Int): Flow<OportunidadEntity?> {
        return dao.getOportunidadById(id)
    }

    suspend fun getOportunidadByIdDirect(id: Int): OportunidadEntity? {
        return dao.getOportunidadByIdDirect(id)
    }

    suspend fun saveOportunidad(oportunidad: OportunidadEntity): Long {
        return if (oportunidad.id == 0) {
            dao.insertOportunidad(oportunidad)
        } else {
            dao.updateOportunidad(oportunidad)
            oportunidad.id.toLong()
        }
    }

    suspend fun deleteOportunidad(oportunidad: OportunidadEntity) {
        dao.deleteOportunidad(oportunidad)
    }

    suspend fun deleteOportunidadById(id: Int) {
        dao.deleteOportunidadById(id)
    }
}
