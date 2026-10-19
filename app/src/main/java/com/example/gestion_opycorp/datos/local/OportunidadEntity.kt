package com.example.gestion_opycorp.datos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oportunidades")
data class OportunidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fuente: String,
    val prioridad: String,
    val potencial: String,
    val estado: String = "Evento identificado",
    val observaciones: String = "",
    val ubicacion: String = "",
    val fotoPath: String = "",
    val fechaDeteccion: String = ""
)
