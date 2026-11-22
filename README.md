# Gestión Móvil de Oportunidades y Activaciones Corporativas - OpYCorp (Caso 007D)

**Asignatura:** DSY1105 · Desarrollo de Aplicaciones Móviles  
**Institución:** Duoc UC - Escuela de Informática y Telecomunicaciones  
**Caso de Estudio:** DSY1105_007D - Agencia de Marketing Directo y Gestión de Beneficios Corporativos  
**Versión de la App:** 1.5 (Production Ready)  

---

## 👥 Integrantes del Equipo 5

- Joaquin Diez
- Marco Palma

---

## 🔗 Trello del Proyecto
- **Link Board:** [https://trello.com/b/NwGWgOoB/opycorp-gestion-de-oportunidades](https://trello.com/b/NwGWgOoB/opycorp-gestion-de-oportunidades)

---

## 📌 Descripción del Proyecto

Esta aplicación móvil Android está desarrollada para la **Agencia Atenas**, especializada en la gestión de convenios y activaciones corporativas (clínicas dentales, servicios automotrices, veterinarios y ferias empresariales). 

La solución centraliza el flujo completo descrito en el **Diagrama del Caso 007D**:
1. **Perfiles & Roles (Login):** Ejecutivo Comercial, Supervisor Comercial y Gestión de Activaciones.
2. **Registro y Gestión de Oportunidades:** Eventos, organizaciones, contactos ficticios, público esperado, prioridades y potencial.
3. **Flujo Formal de Estados:** `Evento identificado` ➔ `Contacto pendiente` ➔ `Contacto realizado` ➔ `Propuesta enviada` ➔ `En espera de respuesta` ➔ (`Participación confirmada` / `Oportunidad descartada`) ➔ `Cerrada`.
4. **Coordinación de Activaciones y Resultados:** Condiciones de espacio, costo y requisitos, asignación de responsables, adjuntar evidencias (Cámara nativa CameraX), geolocalización GPS y registro de resultados de la activación.
5. **Consumo de API REST Externa (Retrofit) & Microservicio Backend (Spring Boot):** Catálogo de eventos externos con importación en 1-click y sincronización con microservicio backend.

---

## 🚀 Funcionalidades & Arquitectura Implementada

### 1. Perfiles y Roles de Usuario
- **Ejecutivo Comercial:** Registro y gestión de oportunidades, contactos y propuestas.
- **Supervisor Comercial:** Filtrado, búsqueda, evaluación de oportunidades, ajuste de prioridad y reasignación de responsables.
- **Gestión de Activaciones:** Coordinación de participación, condiciones, asignación de responsable, evidencias y registro de resultados/cierre.

### 2. Consumo de API REST Externa y Microservicio Spring Boot
- **Retrofit Client:** Consumo del servicio externo de eventos públicos (`/api/eventos-externos`) y endpoints CRUD de oportunidades (`/api/oportunidades`).
- **Microservicio Spring Boot (`backend-spring-boot/`):** Servidor backend desarrollado en Java con Spring Boot, JPA y base de datos H2 operativa, listo para sincronización completa.

### 3. Recursos Nativos del Dispositivo
- **Cámara Nativa (CameraX):** Captura de evidencia fotográfica almacenada de forma segura en memoria interna.
- **Lector QR (ML Kit):** Escaneo de códigos QR de eventos para autocompletar formularios.
- **Geolocalización (GPS):** Obtención de coordenadas y comuna de referencia.

### 4. Persistencia Offline First (Room Database)
- Base de datos local Room con soporte para `OportunidadEntity` enriquecida y `GestionSeguimientoEntity` (historial de cambios y seguimientos).

---

## 💻 Pasos para Ejecutar el Proyecto

### 1. Aplicación Móvil Android
1. Abrir la carpeta del proyecto en **Android Studio Ladybug** (o superior).
2. Esperar la sincronización de dependencias Gradle (`Sync Project with Gradle Files`).
3. Ejecutar en un emulador o dispositivo físico Android (API 24+).
4. **Credenciales Demo por Perfil:**
   - **Ejecutivo:** `ejecutivo@atenas.cl` / `demo123`
   - **Supervisor:** `supervisor@atenas.cl` / `demo123`
   - **Activaciones:** `activaciones@atenas.cl` / `demo123`

### 2. Microservicio Spring Boot Backend (`backend-spring-boot/`)
1. Abrir `backend-spring-boot/pom.xml` en IntelliJ IDEA y configurar un JDK 17 o superior, o instalar Maven y Java 17+.
2. Desde la carpeta `backend-spring-boot`, ejecutar:
   ```bash
   mvn spring-boot:run
   ```
3. La API estará disponible en `http://localhost:8080`. Rutas principales:
   - `GET http://localhost:8080/api/eventos-externos`
   - `GET http://localhost:8080/api/oportunidades`
4. Para conectar Android en el emulador, inicia el backend en el mismo computador; la app usa `http://10.0.2.2:8080/api/` para acceder al host. En un dispositivo físico, ambos deben estar en la misma red Wi-Fi y hay que reemplazar `10.0.2.2` en `RetrofitClient.kt` por la IP local del computador.

---

## 📦 Generación de APK Firmado (IE 3.3.1)

El proyecto incluye configurada la llave de firmado digital en `app/release.jks` y su configuración en `app/build.gradle.kts`. Para compilar el APK firmado de producción:
```bash
./gradlew assembleRelease
```
El archivo APK generado se ubica en:
`app/build/outputs/apk/release/app-release.apk`

---

## 🧪 Pruebas Unitarias (IE 3.2.1)

Para ejecutar la suite de pruebas unitarias (cobertura >= 80% en ViewModel y Repository):
```bash
./gradlew test
```
