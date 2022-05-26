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

    private var usersState: List<User>? = null

    fun presentUsers() {
        usersState?.let {
            usersListener?.invoke(it)
        } ?: run {
            fetchUsers()
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user).onSuccess {
                fetchUsers()
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user).onSuccess {
                fetchUsers()
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            userRepository.getUsers()
                .fold(
                    { users ->
                        usersState = users
                        usersListener?.invoke(users)
                    },
                    { errorListener?.invoke(textProvider.getGenericErrorMessage()) }
                )
        }
    }
}
