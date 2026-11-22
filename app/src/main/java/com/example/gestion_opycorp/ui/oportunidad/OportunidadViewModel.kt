package com.example.gestion_opycorp.ui.oportunidad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestion_opycorp.datos.local.OportunidadEntity
import com.example.gestion_opycorp.datos.remote.EventoExternoModel
import com.example.gestion_opycorp.datos.repository.OportunidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OportunidadViewModel(
    private val repository: OportunidadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OportunidadUiState())
    val uiState: StateFlow<OportunidadUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterPrioridad = MutableStateFlow("Todas")
    val filterPrioridad: StateFlow<String> = _filterPrioridad.asStateFlow()

    private val _filterEstado = MutableStateFlow("Todas")
    val filterEstado: StateFlow<String> = _filterEstado.asStateFlow()

    val oportunidadesList: StateFlow<List<OportunidadEntity>> = combine(
        repository.getOportunidades(),
        _searchQuery,
        _filterPrioridad,
        _filterEstado
    ) { lista, query, prioridad, estado ->
        lista.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.nombre.contains(query, ignoreCase = true) ||
                    item.descripcion.contains(query, ignoreCase = true) ||
                    item.fuente.contains(query, ignoreCase = true) ||
                    item.tipo.contains(query, ignoreCase = true) ||
                    item.organizacion.contains(query, ignoreCase = true)

            val matchesPrioridad = prioridad == "Todas" || item.prioridad.equals(prioridad, ignoreCase = true)
            val matchesEstado = estado == "Todas" || item.estado.equals(estado, ignoreCase = true)

            matchesQuery && matchesPrioridad && matchesEstado
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setUsuario(nombre: String, rol: String) {
        _uiState.update { it.copy(usuarioNombre = nombre, usuarioRol = rol) }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterPrioridadChanged(prioridad: String) {
        _filterPrioridad.value = prioridad
    }

    fun onFilterEstadoChanged(estado: String) {
        _filterEstado.value = estado
    }

    fun onNombreChanged(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, nombreError = null) }
    }

    fun onTipoChanged(tipo: String) {
        _uiState.update { it.copy(tipo = tipo, tipoError = null) }
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, descripcionError = null) }
    }

    fun onFuenteChanged(fuente: String) {
        _uiState.update { it.copy(fuente = fuente, fuenteError = null) }
    }

    fun onPrioridadChanged(prioridad: String) {
        _uiState.update { it.copy(prioridad = prioridad, prioridadError = null) }
    }

    fun onPotencialChanged(potencial: String) {
        _uiState.update { it.copy(potencial = potencial, potencialError = null) }
    }

    fun onEstadoChanged(estado: String) {
        _uiState.update { it.copy(estado = estado, estadoError = null) }
    }

    fun onObservacionesChanged(observaciones: String) {
        _uiState.update { it.copy(observaciones = observaciones) }
    }

    fun onUbicacionChanged(ubicacion: String) {
        _uiState.update { it.copy(ubicacion = ubicacion) }
    }

    fun onFotoPathChanged(path: String) {
        _uiState.update { it.copy(fotoPath = path) }
    }

    fun onOrganizacionChanged(organizacion: String) {
        _uiState.update { it.copy(organizacion = organizacion) }
    }

    fun onContactoNombreChanged(contactoNombre: String) {
        _uiState.update { it.copy(contactoNombre = contactoNombre) }
    }

    fun onContactoTelefonoChanged(contactoTelefono: String) {
        _uiState.update { it.copy(contactoTelefono = contactoTelefono) }
    }

    fun onPublicoEsperadoChanged(publicoEsperado: String) {
        _uiState.update { it.copy(publicoEsperado = publicoEsperado) }
    }

    fun onResponsableChanged(responsable: String) {
        _uiState.update { it.copy(responsable = responsable) }
    }

    fun onCondicionesEspacioChanged(condicionesEspacio: String) {
        _uiState.update { it.copy(condicionesEspacio = condicionesEspacio) }
    }

    fun onCondicionesCostoChanged(condicionesCosto: String) {
        _uiState.update { it.copy(condicionesCosto = condicionesCosto) }
    }

    fun onCondicionesRequisitosChanged(condicionesRequisitos: String) {
        _uiState.update { it.copy(condicionesRequisitos = condicionesRequisitos) }
    }

    fun onResultadoActivacionChanged(resultadoActivacion: String) {
        _uiState.update { it.copy(resultadoActivacion = resultadoActivacion) }
    }

    fun onImpactoEvaluacionChanged(impactoEvaluacion: String) {
        _uiState.update { it.copy(impactoEvaluacion = impactoEvaluacion) }
    }

    fun onProximaAccionChanged(proximaAccion: String) {
        _uiState.update { it.copy(proximaAccion = proximaAccion) }
    }

    fun cargarOportunidadParaEditar(id: Int) {
        if (id <= 0) {
            limpiarFormulario()
            return
        }
        viewModelScope.launch {
            val item = repository.getOportunidadByIdDirect(id)
            if (item != null) {
                _uiState.value = _uiState.value.copy(
                    id = item.id,
                    nombre = item.nombre,
                    tipo = item.tipo,
                    descripcion = item.descripcion,
                    fuente = item.fuente,
                    prioridad = item.prioridad,
                    potencial = item.potencial,
                    estado = item.estado,
                    observaciones = item.observaciones,
                    ubicacion = item.ubicacion,
                    fotoPath = item.fotoPath,
                    organizacion = item.organizacion,
                    contactoNombre = item.contactoNombre,
                    contactoTelefono = item.contactoTelefono,
                    publicoEsperado = item.publicoEsperado,
                    responsable = item.responsable,
                    condicionesEspacio = item.condicionesEspacio,
                    condicionesCosto = item.condicionesCosto,
                    condicionesRequisitos = item.condicionesRequisitos,
                    resultadoActivacion = item.resultadoActivacion,
                    impactoEvaluacion = item.impactoEvaluacion,
                    proximaAccion = item.proximaAccion,
                    guardadoExitosamente = false
                )
            }
        }
    }

    fun validarYGuardar(): Boolean {
        val currentState = _uiState.value

        var esValido = true
        var nombreErr: String? = null
        var tipoErr: String? = null
        var descErr: String? = null
        var fuenteErr: String? = null
        var prioErr: String? = null
        var potErr: String? = null
        var estErr: String? = null

        if (currentState.nombre.isBlank()) {
            nombreErr = "El nombre de la oportunidad es obligatorio"
            esValido = false
        }
        if (currentState.tipo.isBlank()) {
            tipoErr = "El tipo de oportunidad es obligatorio"
            esValido = false
        }
        if (currentState.descripcion.isBlank()) {
            descErr = "La descripción es obligatoria"
            esValido = false
        }
        if (currentState.fuente.isBlank()) {
            fuenteErr = "La fuente de información es obligatoria"
            esValido = false
        }
        if (currentState.prioridad.isBlank()) {
            prioErr = "La prioridad es obligatoria"
            esValido = false
        }
        if (currentState.potencial.isBlank()) {
            potErr = "El potencial comercial es obligatorio"
            esValido = false
        }
        if (currentState.estado.isBlank()) {
            estErr = "El estado de gestión es obligatorio"
            esValido = false
        }

        _uiState.update {
            it.copy(
                nombreError = nombreErr,
                tipoError = tipoErr,
                descripcionError = descErr,
                fuenteError = fuenteErr,
                prioridadError = prioErr,
                potencialError = potErr,
                estadoError = estErr
            )
        }

        if (esValido) {
            _uiState.update { it.copy(guardando = true) }
            viewModelScope.launch {
                val entity = OportunidadEntity(
                    id = currentState.id,
                    nombre = currentState.nombre.trim(),
                    tipo = currentState.tipo.trim(),
                    descripcion = currentState.descripcion.trim(),
                    fuente = currentState.fuente.trim(),
                    prioridad = currentState.prioridad.trim(),
                    potencial = currentState.potencial.trim(),
                    estado = currentState.estado.trim(),
                    observaciones = currentState.observaciones.trim(),
                    ubicacion = currentState.ubicacion.trim(),
                    fotoPath = currentState.fotoPath.trim(),
                    fechaDeteccion = if (currentState.id == 0) "2026-03-08" else "",
                    organizacion = currentState.organizacion.trim(),
                    contactoNombre = currentState.contactoNombre.trim(),
                    contactoTelefono = currentState.contactoTelefono.trim(),
                    publicoEsperado = currentState.publicoEsperado.trim(),
                    responsable = currentState.responsable.ifBlank { currentState.usuarioNombre },
                    condicionesEspacio = currentState.condicionesEspacio.trim(),
                    condicionesCosto = currentState.condicionesCosto.trim(),
                    condicionesRequisitos = currentState.condicionesRequisitos.trim(),
                    resultadoActivacion = currentState.resultadoActivacion.trim(),
                    impactoEvaluacion = currentState.impactoEvaluacion.trim(),
                    proximaAccion = currentState.proximaAccion.trim()
                )
                repository.saveOportunidad(entity, currentState.usuarioNombre)
                _uiState.update {
                    it.copy(
                        guardando = false,
                        guardadoExitosamente = true
                    )
                }
            }
        }
        return esValido
    }

    // Consumo de API REST Externa con Retrofit
    fun cargarEventosExternosApi() {
        _uiState.update { it.copy(cargandoEventosExternos = true, mensajeApi = null) }
        viewModelScope.launch {
            val eventos = repository.fetchEventosExternos()
            _uiState.update {
                it.copy(
                    eventosExternos = eventos,
                    cargandoEventosExternos = false,
                    mensajeApi = "Se cargaron ${eventos.size} eventos desde la API REST"
                )
            }
        }
    }

    fun importarEventoExterno(evento: EventoExternoModel) {
        viewModelScope.launch {
            val usuario = _uiState.value.usuarioNombre
            repository.importarEventoExternoComoOportunidad(evento, usuario)
            _uiState.update {
                it.copy(mensajeApi = "¡Evento '${evento.nombre}' importado con éxito como Oportunidad!")
            }
        }
    }

    fun resetGuardadoExitosamente() {
        _uiState.update { it.copy(guardadoExitosamente = false) }
    }

    fun limpiarFormulario() {
        _uiState.update {
            OportunidadUiState(
                usuarioNombre = it.usuarioNombre,
                usuarioRol = it.usuarioRol
            )
        }
    }

    fun eliminarOportunidad(id: Int) {
        viewModelScope.launch {
            repository.deleteOportunidadById(id)
        }
    }
}

class OportunidadViewModelFactory(
    private val repository: OportunidadRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OportunidadViewModel::class.java)) {
            return OportunidadViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
