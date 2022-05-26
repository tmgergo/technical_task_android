package uk.co.tmgergo.userstechtest.userList

import uk.co.tmgergo.userstechtest.userRepository.User

interface UserListView {
    fun displayLoadingIndicator()
    fun displayUsers(users: List<User>)
    fun displayError(message: String)
}
