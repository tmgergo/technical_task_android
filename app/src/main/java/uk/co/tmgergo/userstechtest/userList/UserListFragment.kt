package uk.co.tmgergo.userstechtest.userList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.tmgergo.userstechtest.R
import uk.co.tmgergo.userstechtest.UsersApplication
import uk.co.tmgergo.userstechtest.databinding.FragmentUserListBinding


class UserListFragment : Fragment(R.layout.fragment_user_list) {

    private lateinit var binding: FragmentUserListBinding
    private val viewModel by lazy { createViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserListBinding.bind(view)

        val adapter = initRecyclerView()
        UserListViewController(AndroidUserListView(binding, adapter), viewModel)
    }

    private fun createViewModel(): UserListViewModel {
        val viewModel by viewModels<UserListViewModel> {
            (activity?.application as UsersApplication).viewModelFactory
        }
        return viewModel
    }

    private fun initRecyclerView(): UserListAdapter {
        with(binding.userListContent.recyclerView) {
            val layoutMngr = LinearLayoutManager(context)
            layoutManager = LinearLayoutManager(context)

            val dividerItemDecoration = DividerItemDecoration(context, layoutMngr.orientation)
            addItemDecoration(dividerItemDecoration)

            val userListAdapter = UserListAdapter()
            adapter = userListAdapter
            return userListAdapter
        }
    }
}
