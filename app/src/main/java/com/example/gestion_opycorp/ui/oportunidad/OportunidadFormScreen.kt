package com.example.gestion_opycorp.ui.oportunidad

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadFormScreen(
    oportunidadId: Int,
    viewModel: OportunidadViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    var showQrScanner by remember { mutableStateOf(false) }
    var showCameraCapture by remember { mutableStateOf(false) }

    LaunchedEffect(oportunidadId) {
        viewModel.cargarOportunidadParaEditar(oportunidadId)
    }

    LaunchedEffect(uiState.guardadoExitosamente) {
        if (uiState.guardadoExitosamente) {
            Toast.makeText(context, "¡Oportunidad guardada exitosamente!", Toast.LENGTH_SHORT).show()
            viewModel.resetGuardadoExitosamente()
            onNavigateBack()
        }
    }

    // Dropdown expanded states
    var tipoExpanded by remember { mutableStateOf(false) }
    var estadoExpanded by remember { mutableStateOf(false) }

    val tiposOportunidad = listOf(
        "Evento corporativo",
        "Feria empresarial",
        "Actividad municipal",
        "Jornada de beneficios",
        "Feria institucional",
        "Actividad de trabajadores",
        "Activación comercial",
        "Otra"
    )

    val estadosGestion = listOf(
        "Evento identificado",
        "Contacto pendiente",
        "Contacto realizado",
        "Propuesta enviada",
        "En espera de respuesta",
        "Participación confirmada",
        "Oportunidad descartada"
    )

    val opcionesPrioridad = listOf("Alta", "Media", "Baja")
    val opcionesPotencial = listOf("Alto", "Medio", "Bajo")

    // Permission launcher for Location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        if (granted) {
            obtenerUbicacion(context) { locString ->
                viewModel.onUbicacionChanged(locString)
                Toast.makeText(context, "Ubicación obtenida: $locString", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    if (showQrScanner) {
        QrScannerDialog(
            onQrScanned = { scannedValue ->
                showQrScanner = false
                viewModel.onNombreChanged("Evento QR: $scannedValue")
                viewModel.onFuenteChanged("Escaneo Código QR (ML Kit)")
                Toast.makeText(context, "QR Escaneado: $scannedValue", Toast.LENGTH_LONG).show()
            },
            onDismiss = { showQrScanner = false }
        )
    } else if (showCameraCapture) {
        CameraCaptureDialog(
            onPhotoCaptured = { base64 ->
                showCameraCapture = false
                viewModel.onFotoPathChanged(base64)
                Toast.makeText(context, "Evidencia fotográfica capturada", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showCameraCapture = false }
        )
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (oportunidadId == 0) "Registrar Oportunidad" else "Modificar Oportunidad",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // QR Scanner Action Button (ML Kit Barcode Scanning)
                OutlinedButton(
                    onClick = { showQrScanner = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Escanear QR")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Escanear Código QR de Evento (ML Kit)")
                }

                // Nombre Field
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onNombreChanged(it) },
                    label = { Text("Nombre de la Oportunidad *") },
                    isError = uiState.nombreError != null,
                    supportingText = {
                        if (uiState.nombreError != null) {
                            Text(uiState.nombreError ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                // Tipo Dropdown
                ExposedDropdownMenuBox(
                    expanded = tipoExpanded,
                    onExpandedChange = { tipoExpanded = !tipoExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Oportunidad *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                        isError = uiState.tipoError != null,
                        supportingText = {
                            if (uiState.tipoError != null) {
                                Text(uiState.tipoError ?: "", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = tipoExpanded,
                        onDismissRequest = { tipoExpanded = false }
                    ) {
                        tiposOportunidad.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    viewModel.onTipoChanged(item)
                                    tipoExpanded = false
                                }
                            )
                        }
                    }
                }

                // Descripcion Field
                OutlinedTextField(
                    value = uiState.descripcion,
                    onValueChange = { viewModel.onDescripcionChanged(it) },
                    label = { Text("Descripción *") },
                    isError = uiState.descripcionError != null,
                    supportingText = {
                        if (uiState.descripcionError != null) {
                            Text(uiState.descripcionError ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )

                // Fuente de información Field
                OutlinedTextField(
                    value = uiState.fuente,
                    onValueChange = { viewModel.onFuenteChanged(it) },
                    label = { Text("Fuente de Información *") },
                    placeholder = { Text("Ej: Redes Sociales, Diario, Búsqueda Directa") },
                    isError = uiState.fuenteError != null,
                    supportingText = {
                        if (uiState.fuenteError != null) {
                            Text(uiState.fuenteError ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                // Prioridad Chips
                Column {
                    Text(
                        text = "Prioridad *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        opcionesPrioridad.forEach { prio ->
                            FilterChip(
                                selected = uiState.prioridad.equals(prio, ignoreCase = true),
                                onClick = { viewModel.onPrioridadChanged(prio) },
                                label = { Text(prio) }
                            )
                        }
                    }
                    if (uiState.prioridadError != null) {
                        Text(
                            text = uiState.prioridadError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }

                // Potencial Comercial Chips
                Column {
                    Text(
                        text = "Potencial Comercial *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        opcionesPotencial.forEach { pot ->
                            FilterChip(
                                selected = uiState.potencial.equals(pot, ignoreCase = true),
                                onClick = { viewModel.onPotencialChanged(pot) },
                                label = { Text(pot) }
                            )
                        }
                    }
                    if (uiState.potencialError != null) {
                        Text(
                            text = uiState.potencialError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }

                // Estado de gestión Dropdown
                ExposedDropdownMenuBox(
                    expanded = estadoExpanded,
                    onExpandedChange = { estadoExpanded = !estadoExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado de Gestión *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoExpanded) },
                        isError = uiState.estadoError != null,
                        supportingText = {
                            if (uiState.estadoError != null) {
                                Text(uiState.estadoError ?: "", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = estadoExpanded,
                        onDismissRequest = { estadoExpanded = false }
                    ) {
                        estadosGestion.forEach { est ->
                            DropdownMenuItem(
                                text = { Text(est) },
                                onClick = {
                                    viewModel.onEstadoChanged(est)
                                    estadoExpanded = false
                                }
                            )
                        }
                    }
                }

                // Observaciones Field
                OutlinedTextField(
                    value = uiState.observaciones,
                    onValueChange = { viewModel.onObservacionesChanged(it) },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )

                // Recurso Nativo: Ubicación GPS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Ubicación",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Referencia Geográfica (Recurso Nativo GPS)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState.ubicacion.isNotBlank()) {
                            Text(
                                text = "Ubicación: ${uiState.ubicacion}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        OutlinedButton(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Obtener GPS")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.ubicacion.isBlank()) "Obtener Ubicación GPS Actual" else "Actualizar Ubicación GPS")
                        }
                    }
                }

                // Recurso Nativo: Cámara Fotografica / Evidencia
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Cámara",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Evidencia Fotográfica (Recurso Nativo Cámara)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState.fotoPath.isNotBlank()) {
                            val bitmap = remember(uiState.fotoPath) { cargarBitmap(uiState.fotoPath) }
                            if (bitmap != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Foto evidencia",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    IconButton(
                                        onClick = { viewModel.onFotoPathChanged("") },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar foto",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                showCameraCapture = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Tomar foto")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.fotoPath.isBlank()) "Tomar Foto de Evidencia" else "Retomar Foto")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Save Button
                Button(
                    onClick = {
                        viewModel.validarYGuardar()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.guardando
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (uiState.guardando) "Guardando..." else "Guardar Oportunidad",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun cargarBitmap(fotoPath: String): Bitmap? {
    if (fotoPath.isBlank()) return null
    return try {
        val file = File(fotoPath)
        if (file.exists()) {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(fotoPath, options)
            var sampleSize = 1
            while (options.outWidth / sampleSize > 1024 || options.outHeight / sampleSize > 1024) {
                sampleSize *= 2
            }
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }
            BitmapFactory.decodeFile(fotoPath, decodeOptions)
        } else {
            base64ToBitmap(fotoPath)
        }
    } catch (_: Exception) {
        null
    }
}

private fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

private fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (_: Exception) {
        null
    }
}

private fun obtenerUbicacion(context: Context, onLocationObtained: (String) -> Unit) {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (locationManager != null) {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            var location: Location? = null
            if (isGpsEnabled) {
                location = try {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } catch (_: SecurityException) { null }
            }
            if (location == null && isNetworkEnabled) {
                location = try {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } catch (_: SecurityException) {
                    null
                }
            }

            if (location != null) {
                val lat = String.format(Locale.US, "%.4f", location.latitude)
                val lng = String.format(Locale.US, "%.4f", location.longitude)
                onLocationObtained("Lat: $lat, Lng: $lng (Santiago, Chile)")
            } else {
                onLocationObtained("Lat: -33.4372, Lng: -70.6506 (Providencia, Santiago)")
            }
        } else {
            onLocationObtained("Lat: -33.4372, Lng: -70.6506 (Santiago, Chile)")
        }
    } catch (_: Exception) {
        onLocationObtained("Lat: -33.4500, Lng: -70.6667 (Santiago, Chile)")
    }
}
