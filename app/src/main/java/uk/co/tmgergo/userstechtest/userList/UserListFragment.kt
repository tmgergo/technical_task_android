package uk.co.tmgergo.userstechtest.userList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import uk.co.tmgergo.userstechtest.R
import uk.co.tmgergo.userstechtest.UsersApplication
import uk.co.tmgergo.userstechtest.databinding.FragmentUserListBinding


class UserListFragment : Fragment(R.layout.fragment_user_list) {

    private lateinit var binding: FragmentUserListBinding
    private val viewModel by lazy { createViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserListBinding.bind(view)
        UserListViewController(AndroidUserListView(binding), viewModel)
    }

    private fun createViewModel(): UserListViewModel {
        val viewModel by viewModels<UserListViewModel> {
            (activity?.application as UsersApplication).viewModelFactory
        }
        return viewModel
    }
}
