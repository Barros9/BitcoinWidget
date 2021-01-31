package com.barros.bitcoinwidget

import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.text.DecimalFormat

// Const
const val BASE_URL = "https://pro-api.coinmarketcap.com/"
const val YOUR_KEY = ""
const val EUR = "EUR"
const val BTC = "BTC"

// Models
class ApiResponse(val data: List<BitcoinItem>)
data class BitcoinItem(val name: String, val symbol: String, val quote: Quote)
data class Quote(@field:Json(name = EUR) val currency: Currency)
data class Currency(val price: Float) {
    fun formatPrice(): String {
        val decimalFormat = DecimalFormat("#.##€")
        return decimalFormat.format(price.toDouble())
    }
}

// Api service
object BitcoinService {
    fun getBitcoinService(): BitcoinApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(BitcoinApi::class.java)
    }
}

interface BitcoinApi {
    @Headers(
        "Accept: application/json",
        "X-CMC_PRO_API_KEY: $YOUR_KEY"
    )
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getInfo(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("convert") convert: String = EUR
    ): Response<ApiResponse>
}

