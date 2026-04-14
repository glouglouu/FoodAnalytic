package com.example.foodanalytic.api.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BarcodeApiService {
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProductInfo(
        @Path("barcode") barcode: String
    ): Response<BarcodeResponse>
}