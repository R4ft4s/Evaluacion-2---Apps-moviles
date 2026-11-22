package com.example.gestion_opycorp.datos.remote

import com.example.gestion_opycorp.datos.local.OportunidadEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OportunidadApiService {

    @GET("eventos-externos")
    suspend fun getEventosExternos(): Response<List<EventoExternoModel>>

    @GET("oportunidades")
    suspend fun getOportunidades(): Response<List<OportunidadEntity>>

    @POST("oportunidades")
    suspend fun createOportunidad(@Body oportunidad: OportunidadEntity): Response<OportunidadEntity>

    @PUT("oportunidades/{id}")
    suspend fun updateOportunidad(
        @Path("id") id: Int,
        @Body oportunidad: OportunidadEntity
    ): Response<OportunidadEntity>

    @DELETE("oportunidades/{id}")
    suspend fun deleteOportunidad(@Path("id") id: Int): Response<Unit>
}
