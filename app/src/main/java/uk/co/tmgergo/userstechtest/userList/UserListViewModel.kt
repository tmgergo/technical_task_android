package uk.co.tmgergo.userstechtest.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.tmgergo.userstechtest.userRepository.User
import uk.co.tmgergo.userstechtest.userRepository.UserRepository
import uk.co.tmgergo.userstechtest.utils.TextProvider

typealias UsersListener = (List<User>) -> Unit
typealias ErrorListener = (String) -> Unit

class UserListViewModel(
    private val userRepository: UserRepository,
    private val textProvider: TextProvider,
) : ViewModel() {

    var usersListener: UsersListener? = null
    var errorListener: ErrorListener? = null

    fun presentUsers() {
        viewModelScope.launch {
            userRepository.getUsers()
                .fold(
                    { users -> usersListener?.invoke(users) },
                    { errorListener?.invoke(textProvider.getGenericErrorMessage()) }
                )

        }
    }
}
