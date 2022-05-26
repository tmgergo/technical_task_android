package uk.co.tmgergo.userstechtest.userList

class UserListViewController(view: UserListView, viewModel: UserListViewModel) {
    init {
        viewModel.usersListener = { users ->
            view.displayUsers(users)
        }

        viewModel.errorListener = { message ->
            view.displayError(message)
        }

        view.onDeleteUserListener = { user ->
            viewModel.deleteUser(user)
        }

        view.displayLoadingIndicator()
        viewModel.presentUsers()
    }
}
