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

        view.onAddUserListener = { name, email ->
            viewModel.addUser(name, email)
        }

        view.displayLoadingIndicator()
        viewModel.presentUsers()
    }
}
