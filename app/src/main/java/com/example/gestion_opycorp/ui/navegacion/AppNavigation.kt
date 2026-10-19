package com.example.gestion_opycorp.ui.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestion_opycorp.datos.local.AppDatabase
import com.example.gestion_opycorp.datos.repository.OportunidadRepository
import com.example.gestion_opycorp.ui.auth.LoginScreen
import com.example.gestion_opycorp.ui.menu.MenuScreen
import com.example.gestion_opycorp.ui.oportunidad.ListaOportunidadesScreen
import com.example.gestion_opycorp.ui.oportunidad.OportunidadFormScreen
import com.example.gestion_opycorp.ui.oportunidad.OportunidadViewModel
import com.example.gestion_opycorp.ui.oportunidad.OportunidadViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Initialize Database and Repository
    val database = AppDatabase.getDatabase(context)
    val repository = OportunidadRepository(database.oportunidadDao())
    val viewModelFactory = OportunidadViewModelFactory(repository)

    val oportunidadViewModel: OportunidadViewModel = viewModel(factory = viewModelFactory)

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            // 1. Iniciar sesión
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { usuarioNombre ->
                        navController.navigate("menu/${usuarioNombre.ifBlank { "Ejecutivo Comercial" }}") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // 2. Menú principal
            composable(
                route = "menu/{usuario}",
                arguments = listOf(navArgument("usuario") { type = NavType.StringType })
            ) { backStackEntry ->
                val usuario = backStackEntry.arguments?.getString("usuario") ?: "Ejecutivo Comercial"
                MenuScreen(
                    usuarioNombre = usuario,
                    viewModel = oportunidadViewModel,
                    onNavigateToLista = {
                        navController.navigate("lista_oportunidades")
                    },
                    onNavigateToCrear = {
                        navController.navigate("form_oportunidad/0")
                    },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("menu/$usuario") { inclusive = true }
                        }
                    }
                )
            }

            // 3. Lista de Oportunidades
            composable("lista_oportunidades") {
                ListaOportunidadesScreen(
                    viewModel = oportunidadViewModel,
                    onNavigateToForm = { id ->
                        navController.navigate("form_oportunidad/$id")
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 4. Formulario de Oportunidad (Crear / Editar)
            composable(
                route = "form_oportunidad/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                OportunidadFormScreen(
                    oportunidadId = id,
                    viewModel = oportunidadViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // Persistent App Version Badge Overlay
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = RoundedCornerShape(topEnd = 8.dp),
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = "v1.2",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
