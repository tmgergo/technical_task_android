package uk.co.tmgergo.userstechtest.userList

import android.view.View.GONE
import android.view.View.VISIBLE
import uk.co.tmgergo.userstechtest.databinding.FragmentUserListBinding
import uk.co.tmgergo.userstechtest.userRepository.User


class AndroidUserListView(private val binding: FragmentUserListBinding) : UserListView {
    override fun displayLoadingIndicator() {
        hideErrorMessage()
        hideUserList()
        showLoadingIndicator()
    }

    override fun displayUsers(users: List<User>) {
        hideErrorMessage()
        hideLoadingIndicator()
        binding.numberOfUsers.text = "${users.size} users"
        showUserList()
    }

    override fun displayError(message: String) {
        hideLoadingIndicator()
        hideUserList()
        binding.errorMessage.text = message
        showErrorMessage()
    }

    private fun showErrorMessage() {
        binding.errorMessage.visibility = VISIBLE
    }

    private fun showLoadingIndicator() {
        binding.loadingIndicator.visibility = VISIBLE
    }

    private fun hideLoadingIndicator() {
        binding.loadingIndicator.visibility = GONE
    }

    private fun showUserList() {
        binding.numberOfUsers.visibility = VISIBLE
    }

    private fun hideUserList() {
        binding.numberOfUsers.visibility = GONE
    }

    private fun hideErrorMessage() {
        binding.errorMessage.visibility = GONE
    }
}