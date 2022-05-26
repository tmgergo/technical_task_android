package uk.co.tmgergo.userstechtest.userList

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import uk.co.tmgergo.userstechtest.R
import uk.co.tmgergo.userstechtest.databinding.FragmentUserListBinding
import uk.co.tmgergo.userstechtest.userRepository.User


class AndroidUserListView(private val binding: FragmentUserListBinding, private val adapter: UserListAdapter) : UserListView() {
    init {
        binding.userListContent.fab.setOnClickListener { view ->
            Snackbar.make(view, "TODO: add user", Snackbar.LENGTH_LONG).show()
        }
    }

    override var onDeleteUserListener: OnDeleteUserListener? = null
        set(value) {
            field = value

            adapter.onDeleteUserListener = { user ->
                AlertDialog.Builder(binding.root.context)
                    .setMessage(R.string.are_you_sure_you_want_to_remove_this_user)
                    .setCancelable(false)
                    .setPositiveButton(R.string.OK) { dialog, id -> value?.invoke(user) }
                    .setNegativeButton(R.string.No) { dialog, id -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }

    override var onAddUserListener: OnAddUserListener? = null

    override fun displayLoadingIndicator() {
        hideErrorMessage()
        hideUserList()
        showLoadingIndicator()
    }

    override fun displayUsers(users: List<User>) {
        hideErrorMessage()
        hideLoadingIndicator()
        adapter.replaceItems(users)
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

    private fun hideErrorMessage() {
        binding.errorMessage.visibility = GONE
    }

    private fun showLoadingIndicator() {
        binding.loadingIndicator.visibility = VISIBLE
    }

    private fun hideLoadingIndicator() {
        binding.loadingIndicator.visibility = GONE
    }

    private fun showUserList() {
        binding.userListContent.root.visibility = VISIBLE
    }

    private fun hideUserList() {
        binding.userListContent.root.visibility = GONE
    }
}
