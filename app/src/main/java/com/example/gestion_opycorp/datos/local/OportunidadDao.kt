package com.example.gestion_opycorp.datos.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OportunidadDao {

    @Query("SELECT * FROM oportunidades ORDER BY id DESC")
    fun getOportunidades(): Flow<List<OportunidadEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :id")
    fun getOportunidadById(id: Int): Flow<OportunidadEntity?>

    @Query("SELECT * FROM oportunidades WHERE id = :id")
    suspend fun getOportunidadByIdDirect(id: Int): OportunidadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOportunidad(oportunidad: OportunidadEntity): Long

    @Update
    suspend fun updateOportunidad(oportunidad: OportunidadEntity)

    @Delete
    suspend fun deleteOportunidad(oportunidad: OportunidadEntity)

    @Query("DELETE FROM oportunidades WHERE id = :id")
    suspend fun deleteOportunidadById(id: Int)
}
