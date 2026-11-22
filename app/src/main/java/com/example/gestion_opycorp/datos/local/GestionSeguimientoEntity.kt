package com.example.gestion_opycorp.datos.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gestiones_seguimiento",
    foreignKeys = [
        ForeignKey(
            entity = OportunidadEntity::class,
            parentColumns = ["id"],
            childColumns = ["oportunidadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["oportunidadId"])]
)
data class GestionSeguimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val oportunidadId: Int,
    val fecha: String,
    val usuario: String,
    val tipoAccion: String, // Contacto, Propuesta, Cambio Estado, Evaluación, etc.
    val detalle: String,
    val resultado: String = ""
)
