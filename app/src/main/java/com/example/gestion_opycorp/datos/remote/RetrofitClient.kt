package com.example.gestion_opycorp.datos.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    val apiService: OportunidadApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OportunidadApiService::class.java)
    }

    // Default Fallback / Mock External Events Catalogue for API REST testing
    fun getMockEventosExternos(): List<EventoExternoModel> {
        return listOf(
            EventoExternoModel(
                id = 101,
                nombre = "Congreso de Bienestar Laboral 2026",
                fecha = "2026-04-12",
                comuna = "Santiago Centro",
                organizacion = "Asociación Chilena de Seguridad (ACHS)",
                descripcion = "Encuentro masivo de prevencionistas y recursos humanos para convenios institucionales.",
                tipo = "Congreso corporativo",
                publicoEsperado = "1500 profesionales"
            ),
            EventoExternoModel(
                id = 102,
                nombre = "Expo Salud & Deporte Providencia",
                fecha = "2026-04-18",
                comuna = "Providencia",
                organizacion = "Corporación de Desarrollo Social Providencia",
                descripcion = "Jornada al aire libre enfocada en operativos de salud preventiva, dental y kinesiológica.",
                tipo = "Actividad municipal",
                publicoEsperado = "3000 personas"
            ),
            EventoExternoModel(
                id = 103,
                nombre = "Feria de Beneficios Colaboradores Enel",
                fecha = "2026-04-25",
                comuna = "Las Condes",
                organizacion = "Enel Distribución Chile",
                descripcion = "Feria interna de prestadores de servicios de salud, automotriz y bienestar para personal corporativo.",
                tipo = "Jornada de beneficios",
                publicoEsperado = "1000 colaboradores"
            ),
            EventoExternoModel(
                id = 104,
                nombre = "Encuentro Anual de Proveedores Vitacura",
                fecha = "2026-05-02",
                comuna = "Vitacura",
                organizacion = "Cámara Comercial Vitacura",
                descripcion = "Rueda de negocios para activación de convenios directos con empresas y pymes locales.",
                tipo = "Activación comercial",
                publicoEsperado = "600 empresarios"
            )
        )
    }
}
