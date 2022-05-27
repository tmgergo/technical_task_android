package uk.co.tmgergo.userstechtest.userList

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import uk.co.tmgergo.userstechtest.R
import uk.co.tmgergo.userstechtest.databinding.DialogAddUserBinding
import uk.co.tmgergo.userstechtest.databinding.FragmentUserListBinding
import uk.co.tmgergo.userstechtest.userRepository.User


class AndroidUserListView(
    private val binding: FragmentUserListBinding,
    private val adapter: UserListAdapter
) : UserListView() {
    init {
        binding.userListContent.fab.setOnClickListener {
            showAddUserDialog()
        }
    }

    override var onDeleteUserListener: OnDeleteUserListener? = null
        set(value) {
            field = value

            adapter.onDeleteUserListener = { user ->
                showDeleteConfirmationDialog {
                    value?.invoke(user)
                }
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

    private fun showDeleteConfirmationDialog(positiveAction: ()-> Unit) {
        AlertDialog.Builder(binding.root.context)
            .setCancelable(false)
            .setMessage(R.string.are_you_sure_you_want_to_remove_this_user)
            .setPositiveButton(R.string.ok) { _, _ -> positiveAction.invoke() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun showAddUserDialog() {
        val dialogBinding :DialogAddUserBinding = DialogAddUserBinding.inflate(LayoutInflater.from(binding.root.context))
        AlertDialog.Builder(binding.root.context)
            .setCancelable(false)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.add) { _, _ ->
                onAddUserListener?.invoke(dialogBinding.name.text.toString(), dialogBinding.email.text.toString())
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}
