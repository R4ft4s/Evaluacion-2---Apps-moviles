package com.example.gestion_opycorp.datos.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GestionSeguimientoDao {

    @Query("SELECT * FROM gestiones_seguimiento WHERE oportunidadId = :oportunidadId ORDER BY id DESC")
    fun getSeguimientosForOportunidad(oportunidadId: Int): Flow<List<GestionSeguimientoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeguimiento(seguimiento: GestionSeguimientoEntity): Long

    @Query("DELETE FROM gestiones_seguimiento WHERE oportunidadId = :oportunidadId")
    suspend fun deleteSeguimientosForOportunidad(oportunidadId: Int)
}
