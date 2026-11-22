package com.example.gestion_opycorp

import com.example.gestion_opycorp.datos.local.OportunidadEntity
import com.example.gestion_opycorp.datos.remote.EventoExternoModel
import com.example.gestion_opycorp.datos.remote.OportunidadApiService
import com.example.gestion_opycorp.datos.remote.RetrofitClient
import com.example.gestion_opycorp.datos.repository.OportunidadRepository
import com.example.gestion_opycorp.ui.oportunidad.OportunidadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

import kotlinx.coroutines.test.UnconfinedTestDispatcher

class FakeOportunidadApiService : OportunidadApiService {
    override suspend fun getEventosExternos(): Response<List<EventoExternoModel>> {
        return Response.success(RetrofitClient.getMockEventosExternos())
    }

    override suspend fun getOportunidades(): Response<List<OportunidadEntity>> {
        return Response.success(emptyList())
    }

    override suspend fun createOportunidad(oportunidad: OportunidadEntity): Response<OportunidadEntity> {
        return Response.success(oportunidad)
    }

    override suspend fun updateOportunidad(id: Int, oportunidad: OportunidadEntity): Response<OportunidadEntity> {
        return Response.success(oportunidad)
    }

    override suspend fun deleteOportunidad(id: Int): Response<Unit> {
        return Response.success(Unit)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class OportunidadViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: OportunidadViewModel
    private lateinit var repository: OportunidadRepository
    private lateinit var fakeDao: FakeOportunidadDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeOportunidadDao()
        val fakeApi = FakeOportunidadApiService()
        repository = OportunidadRepository(fakeDao, null, fakeApi, testDispatcher)
        viewModel = OportunidadViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setUsuario actualiza el perfil y nombre de usuario`() {
        viewModel.setUsuario("Marco Palma", "Supervisor Comercial")
        val state = viewModel.uiState.value
        assertEquals("Marco Palma", state.usuarioNombre)
        assertEquals("Supervisor Comercial", state.usuarioRol)
    }

    @Test
    fun `onSearchQueryChanged actualiza la consulta de busqueda`() {
        viewModel.onSearchQueryChanged("Salud")
        assertEquals("Salud", viewModel.searchQuery.value)
    }

    @Test
    fun `onFilterPrioridadChanged actualiza el filtro de prioridad`() {
        viewModel.onFilterPrioridadChanged("Alta")
        assertEquals("Alta", viewModel.filterPrioridad.value)
    }

    @Test
    fun `onFilterEstadoChanged actualiza el filtro de estado`() {
        viewModel.onFilterEstadoChanged("Propuesta enviada")
        assertEquals("Propuesta enviada", viewModel.filterEstado.value)
    }

    @Test
    fun `cargarEventosExternosApi obtiene los eventos desde la API REST`() = runTest {
        viewModel.cargarEventosExternosApi()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.eventosExternos.isNotEmpty())
        assertNotNull(state.mensajeApi)
    }

    @Test
    fun `importarEventoExterno agrega una nueva oportunidad`() = runTest {
        val eventoDemo = EventoExternoModel(
            id = 999,
            nombre = "Feria de Prueba Importación",
            fecha = "2026-04-01",
            comuna = "Santiago",
            organizacion = "Organización Test",
            descripcion = "Descripción Importada",
            tipo = "Feria empresarial",
            publicoEsperado = "500 personas"
        )

        viewModel.importarEventoExterno(eventoDemo)
        advanceUntilIdle()

        val listaActualizada = repository.getOportunidades().first()
        assertTrue(listaActualizada.any { it.nombre == "Feria de Prueba Importación" })
    }

    @Test
    fun `modificaciones de campos actualizan correctamente el UiState`() {
        viewModel.onOrganizacionChanged("Municipalidad Las Condes")
        viewModel.onContactoNombreChanged("Pedro Gómez")
        viewModel.onContactoTelefonoChanged("+56911112222")
        viewModel.onPublicoEsperadoChanged("1000 personas")
        viewModel.onResponsableChanged("Joaquín Díez")
        viewModel.onCondicionesEspacioChanged("Stand 4x4")
        viewModel.onCondicionesCostoChanged("$200.000")
        viewModel.onCondicionesRequisitosChanged("Credenciales")
        viewModel.onResultadoActivacionChanged("Éxito total")
        viewModel.onImpactoEvaluacionChanged("100% positivo")
        viewModel.onProximaAccionChanged("Reunión de seguimiento")

        val state = viewModel.uiState.value
        assertEquals("Municipalidad Las Condes", state.organizacion)
        assertEquals("Pedro Gómez", state.contactoNombre)
        assertEquals("+56911112222", state.contactoTelefono)
        assertEquals("1000 personas", state.publicoEsperado)
        assertEquals("Joaquín Díez", state.responsable)
        assertEquals("Stand 4x4", state.condicionesEspacio)
        assertEquals("$200.000", state.condicionesCosto)
        assertEquals("Credenciales", state.condicionesRequisitos)
        assertEquals("Éxito total", state.resultadoActivacion)
        assertEquals("100% positivo", state.impactoEvaluacion)
        assertEquals("Reunión de seguimiento", state.proximaAccion)
    }
}
