package uk.co.tmgergo.userstechtest.userRepository

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface UserDataSource {
    @GET("/public/v2/users")
    suspend fun getUsers() : List<UserDTO>
}

object UserDataSourceFactory {
    fun create(baseUrl: HttpUrl, client: OkHttpClient, accessToken: String): UserDataSource {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientWithAuthHeader(client, accessToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserDataSource::class.java)
    }

    private fun clientWithAuthHeader(client: OkHttpClient, accessToken: String) =
        client.newBuilder()
            .addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken")
                        .build()
                chain.proceed(request)
            }
            .build()
}
