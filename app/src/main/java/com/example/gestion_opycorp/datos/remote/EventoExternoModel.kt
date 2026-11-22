package com.example.gestion_opycorp.datos.remote

import com.google.gson.annotations.SerializedName

data class EventoExternoModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("comuna")
    val comuna: String,

    @SerializedName("organizacion")
    val organizacion: String,

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("tipo")
    val tipo: String = "Feria empresarial",

    @SerializedName("publicoEsperado")
    val publicoEsperado: String = "500 personas"
)
