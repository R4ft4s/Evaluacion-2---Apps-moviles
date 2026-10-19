package com.example.gestion_opycorp

import com.example.gestion_opycorp.datos.local.OportunidadEntity
import com.example.gestion_opycorp.datos.repository.OportunidadRepository
import com.example.gestion_opycorp.ui.oportunidad.OportunidadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeOportunidadDao : com.example.gestion_opycorp.datos.local.OportunidadDao {
    private val list = mutableListOf<OportunidadEntity>()

    override fun getOportunidades(): Flow<List<OportunidadEntity>> = flowOf(list)
    override fun getOportunidadById(id: Int): Flow<OportunidadEntity?> = flowOf(list.find { it.id == id })
    override suspend fun getOportunidadByIdDirect(id: Int): OportunidadEntity? = list.find { it.id == id }
    override suspend fun insertOportunidad(oportunidad: OportunidadEntity): Long {
        val newObj = oportunidad.copy(id = list.size + 1)
        list.add(newObj)
        return newObj.id.toLong()
    }
    override suspend fun updateOportunidad(oportunidad: OportunidadEntity) {
        val idx = list.indexOfFirst { it.id == oportunidad.id }
        if (idx != -1) list[idx] = oportunidad
    }
    override suspend fun deleteOportunidad(oportunidad: OportunidadEntity) {
        list.removeIf { it.id == oportunidad.id }
    }
    override suspend fun deleteOportunidadById(id: Int) {
        list.removeIf { it.id == id }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class OportunidadValidationTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: OportunidadViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val fakeDao = FakeOportunidadDao()
        val repository = OportunidadRepository(fakeDao)
        viewModel = OportunidadViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validarYGuardar con campos vacios debe fallar y mostrar errores`() {
        val esValido = viewModel.validarYGuardar()
        assertFalse(esValido)

        val uiState = viewModel.uiState.value
        assertNotNull(uiState.nombreError)
        assertNotNull(uiState.tipoError)
        assertNotNull(uiState.descripcionError)
        assertNotNull(uiState.fuenteError)
        assertNotNull(uiState.prioridadError)
        assertNotNull(uiState.potencialError)
        assertNull(uiState.estadoError) // estado has default value "Evento identificado"
    }

    @Test
    fun `validarYGuardar con campos completos debe tener exito`() {
        viewModel.onNombreChanged("Evento Test")
        viewModel.onTipoChanged("Feria empresarial")
        viewModel.onDescripcionChanged("Descripcion de prueba")
        viewModel.onFuenteChanged("Redes Sociales")
        viewModel.onPrioridadChanged("Alta")
        viewModel.onPotencialChanged("Alto")
        viewModel.onEstadoChanged("Evento identificado")

        val esValido = viewModel.validarYGuardar()
        assertTrue(esValido)
    }
}
