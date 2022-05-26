package uk.co.tmgergo.userstechtest

import android.app.Application
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import uk.co.tmgergo.userstechtest.userRepository.UserDataSourceFactory
import uk.co.tmgergo.userstechtest.userRepository.UserRepositoryFactory
import uk.co.tmgergo.userstechtest.utils.AndroidTextProvider

class UsersApplication : Application() {

    val viewModelFactory: ViewModelFactory
        get() = _viewModelFactory
    private lateinit var _viewModelFactory : ViewModelFactory

    override fun onCreate() {
        super.onCreate()
        val httpClient = OkHttpClient()
        val userDataSource = UserDataSourceFactory.create(UserDataSourceFactory.BASE_URL, httpClient, UserDataSourceFactory.ACCESS_TOKEN)
        val userRepository = UserRepositoryFactory.create(Dispatchers.IO, userDataSource)
        val serviceLocator = ServiceLocator(userRepository, AndroidTextProvider(this))
        _viewModelFactory = ViewModelFactory(serviceLocator)
    }
}