package uk.co.tmgergo.userstechtest.userRepository

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserDataSource {
    @GET("/public/v2/users")
    suspend fun getUsers() : List<UserDTO>

    @POST("/public/v2/users")
    suspend fun addUser(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("gender") gender: String,
        @Query("status") status: String) : UserDTO

    @DELETE("/public/v2/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int) : Response<Unit>
}

object UserDataSourceFactory {
    val BASE_URL = "https://gorest.co.in/".toHttpUrl()
    const val ACCESS_TOKEN = "[your access token here]"

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
