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
    fun create(baseUrl: HttpUrl, client: OkHttpClient): UserDataSource {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserDataSource::class.java)
    }
}
