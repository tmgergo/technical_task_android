package uk.co.tmgergo.userstechtest.userList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.tmgergo.userstechtest.databinding.ListItemUserBinding
import uk.co.tmgergo.userstechtest.userRepository.User

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private var users = mutableListOf<User>()
    var onDeleteUserListener: OnDeleteUserListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user) {
            onDeleteUserListener?.invoke(user)
            true
        }
    }

    override fun getItemCount(): Int = users.size

    fun replaceItems(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, onLongClickListener: View.OnLongClickListener) {
            binding.root.setOnLongClickListener(onLongClickListener)
            binding.name.text = user.name
            binding.email.text = user.email
        }
    }
}
