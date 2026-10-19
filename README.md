# Gestión Móvil de Oportunidades y Activaciones Corporativas - OpYCorp

**Asignatura:** DSY1105 · Desarrollo de Aplicaciones Móviles  
**Institución:** Duoc UC - Escuela de Informática y Telecomunicaciones  
**Caso de Estudio:** DSY1105_007D - Agencia de Marketing Directo y Gestión de Beneficios Corporativos  
**Versión de la App:** 1.5  

---

## 👥 Integrantes del Equipo 5

- Joaquin Diez
- Marco Palma

---

## 🔗 Trello del Proyecto
- **Link Board:** [https://trello.com/b/NwGWgOoB/opycorp-gestion-de-oportunidades](https://trello.com/b/NwGWgOoB/opycorp-gestion-de-oportunidades)

---

## 📌 Descripción del Proyecto

Esta aplicación móvil Android está diseñada para la **Agencia Atenas**, una empresa enfocada en la gestión de convenios y beneficios corporativos (clínicas dentales, servicios automotrices, veterinarios, entre otros). 

El objetivo principal es resolver la dispersión de información y la falta de anticipación en la captación de oportunidades comerciales. La solución centraliza la identificación, organización, priorización, captura de evidencias fotográficas y seguimiento de eventos corporativos (ferias empresariales, actividades municipales, jornadas de beneficios, entre otros).

---

## 🚀 Funcionalidades Implementadas

### 1. Identidad Visual y Marca Corporativa
- **Paleta de Colores Personalizada:** Aplicación del tema de marca oficial utilizando la paleta compuesta por:
  - Azul Pizarra (`#4C6793`) como color Primario.
  - Púrpura Profundo (`#5C2E7E`) como color Secundario.
  - Celeste Suave (`#8BBCCC`) como color Terciario.
  - Negro Puro (`#000000`) y tonos neutros de alto contraste para superficies y fondos.
- **Logotipo e Iconografía:** Integración del logotipo e imagotipo corporativo `logo.png` (Gestión Atenea) en la pantalla de inicio de sesión (*LoginScreen*), encabezado del menú principal (*MenuScreen*) y como icono oficial de la aplicación (*Launcher Icon*).

### 2. Autenticación e Identificación
- Pantalla de inicio de sesión (*LoginScreen*) con credenciales del ejecutivo comercial y supervisor.
- Control de sesión e integración del nombre del usuario en la interfaz.

### 3. Menú Principal (*MenuScreen*)
- Panel de bienvenida personalizado con el logotipo institucional.
- Métricas rápidas con contadores dinámicos de oportunidades totales, alta prioridad y confirmadas.
- Accesos directos para la navegación hacia el listado y registro de oportunidades.
- Opción para cerrar sesión (*Logout*).

### 4. Módulo de Oportunidades
- **Listado de Oportunidades (*ListaOportunidadesScreen*):**
  - Muestra la lista de eventos u oportunidades almacenadas localmente en Room.
  - **Filtros por Prioridad:** Chips de filtrado dinámico (Todas, Alta, Media, Baja).
  - **Búsqueda en tiempo real:** Por nombre, tipo o fuente de información.
  - Badges e indicadores visuales de prioridad, estado de gestión e indicador de evidencia fotográfica.
  - Diálogo de confirmación para eliminación de registros.
- **Formulario de Registro y Modificación (*OportunidadFormScreen*):**
  - Registro de datos clave: Nombre, Tipo de oportunidad, Descripción, Fuente de información, Prioridad, Potencial Comercial, Estado de gestión y Observaciones.
  - **Validaciones en tiempo real:** Control de campos obligatorios con mensajes de error visuales integrados en el `UiState`.

### 5. Recursos Nativos del Dispositivo
- **Cámara Nativa de Evidencias (CameraX):** Captura e integración de fotos para evidencia fotográfica asociada a la oportunidad. La imagen se almacena de forma segura en el almacenamiento interno local (`filesDir`), evitando cierres de aplicación (*crashes*) mediante ejecución en el hilo principal (`Main Thread`) y optimización de memoria con reescalado `inSampleSize`.
- **Lector de Código QR (ML Kit Barcode Scanning):** Escaneo de códigos QR de eventos para autocompletar automáticamente los datos de la oportunidad.
- **Geolocalización (GPS Nativo):** Captura de coordenadas geográficas de referencia (Latitud y Longitud) del evento o punto de activación.

### 6. Arquitectura y Persistencia
- **Patrón MVVM Real (Model-View-ViewModel):** Separación estricta de responsabilidades entre Vista (`Compose`), Estado (`OportunidadUiState` + `StateFlow`), Lógica de Negocio (`OportunidadViewModel`), Repositorio (`OportunidadRepository`) y Acceso a Datos (`OportunidadDao`).
- **Persistencia Local Offline (Room Database):** Todos los registros permanecen guardados en la base de datos local del dispositivo, persistiendo incluso al reiniciar la aplicación.
- **Navegación Reactiva:** Implementada con `Jetpack Navigation Compose`.
- **Indicador de Versión Persistente:** Muestra la versión unificada de la app (`v1.5`) en la esquina inferior de todas las pantallas.

---

## 🛠️ Tecnologías y Librerías Utilizadas

- **Lenguaje:** Kotlin 2.x
- **UI Toolkit:** Jetpack Compose con Material Design 3
- **Arquitectura:** MVVM con `StateFlow` y `UiState`
- **Persistencia:** Room Database + KSP
- **Navegación:** Navigation Compose
- **Cámara:** AndroidX CameraX (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`)
- **Escáner QR:** Google ML Kit Barcode Scanning
- **Ubicación:** Google Play Services Location API
- **Imágenes:** Coil Compose / Android BitmapFactory

---

## 💻 Pasos para Ejecutar el Proyecto

### Requisitos Previos
1. **Android Studio:** Versión Ladybug (2024.2.1) o superior.
2. **JDK:** Java Development Kit 11 o 17.
3. **Android SDK:** API Level 35 (Android 15) como Target SDK, Mínimo API 24 (Android 7.0).
4. **Dispositivo o Emulador:** Dispositivo físico Android o AVD con soporte de cámara y GPS habilitado.

### Pasos de Instalación y Ejecución

1. **Clonar o descargar el repositorio:**
   ```bash
   git clone https://github.com/R4ft4s/Evaluacion-2---Apps-moviles.git
   ```

2. **Abrir en Android Studio:**
   - Iniciar Android Studio.
   - Seleccionar **Open** y navegar hasta la carpeta raíz del proyecto (`Evaluacion-2---Apps-moviles`).

3. **Sincronizar Gradle:**
   - Esperar a que Android Studio descargue las dependencias y sincronice el proyecto (`Sync Project with Gradle Files`).

4. **Ejecutar la Aplicación:**
   - Seleccionar el módulo `app` en la barra superior.
   - Seleccionar un emulador AVD o un dispositivo Android físico conectado mediante depuración USB.
   - Presionar **Run** (`Shift + F10`) o el botón verde de reproducción ▶️.

5. **Permisos en la Aplicación:**
   - Al utilizar las funciones de **Evidencias fotográficas (Cámara)**, **Escanear Código QR** o **Obtener Ubicación GPS**, acepte los permisos emergentes de Cámara y Ubicación solicitados por el sistema operativo.

---

## 🧪 Pruebas Unitarias

Para ejecutar la suite de pruebas unitarias implementadas en el proyecto:
```bash
./gradlew test
```
o desde Android Studio haciendo clic derecho en la carpeta `src/test/java` y seleccionando **Run Tests**.
