package uk.co.tmgergo.userstechtest.userList

import uk.co.tmgergo.userstechtest.userRepository.User

typealias OnDeleteUserListener = (User) -> Unit

abstract class UserListView {
    var onDeleteUserListener: OnDeleteUserListener? = null

    abstract fun displayLoadingIndicator()
    abstract fun displayUsers(users: List<User>)
    abstract fun displayError(message: String)
}
