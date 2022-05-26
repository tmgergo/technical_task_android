package uk.co.tmgergo.userstechtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.co.tmgergo.userstechtest.userList.UserListViewModel

class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            UserListViewModel::class.java -> UserListViewModel(serviceLocator.userRepository, serviceLocator.textProvider) as T
            else -> throw IllegalArgumentException("View model ${modelClass.canonicalName} could not be created")
        }
    }
}