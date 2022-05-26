package uk.co.tmgergo.userstechtest.userList

import uk.co.tmgergo.userstechtest.userRepository.User

typealias OnDeleteUserListener = (User) -> Unit
typealias OnAddUserListener = (User) -> Unit

abstract class UserListView {
    abstract var onDeleteUserListener: OnDeleteUserListener?
    abstract var onAddUserListener: OnAddUserListener?
    abstract fun displayLoadingIndicator()
    abstract fun displayUsers(users: List<User>)
    abstract fun displayError(message: String)
}
