package com.example.gestion_opycorp.ui.oportunidad

import com.example.gestion_opycorp.datos.remote.EventoExternoModel

data class OportunidadUiState(
    val id: Int = 0,
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val fuente: String = "",
    val prioridad: String = "",
    val potencial: String = "",
    val estado: String = "Evento identificado",
    val observaciones: String = "",
    val ubicacion: String = "",
    val fotoPath: String = "",
    val organizacion: String = "",
    val contactoNombre: String = "",
    val contactoTelefono: String = "",
    val publicoEsperado: String = "",
    val responsable: String = "Ejecutivo Comercial",
    val condicionesEspacio: String = "",
    val condicionesCosto: String = "",
    val condicionesRequisitos: String = "",
    val resultadoActivacion: String = "",
    val impactoEvaluacion: String = "",
    val proximaAccion: String = "",

    val usuarioNombre: String = "Usuario",
    val usuarioRol: String = "Ejecutivo Comercial",

    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val fuenteError: String? = null,
    val prioridadError: String? = null,
    val potencialError: String? = null,
    val estadoError: String? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,

    // API REST External Events State
    val eventosExternos: List<EventoExternoModel> = emptyList(),
    val cargandoEventosExternos: Boolean = false,
    val mensajeApi: String? = null
)
