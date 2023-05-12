package mick.studio.itsfuntorun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.CardRunBinding
import mick.studio.itsfuntorun.databinding.CardUserBinding
import mick.studio.itsfuntorun.helpers.customTransformation
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.users.UserModel

interface UserListener {
    fun onUserClick(user: UserModel)
}
class UserListAdapter constructor(private var users: List<UserModel>, private val listener: UserListener) :
                                RecyclerView.Adapter<UserListAdapter.MainHolder>() {
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
           val binding = CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

           return MainHolder(binding)
       }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val user = users[holder.adapterPosition]
        holder.bind(user, listener)
    }

    override fun getItemCount(): Int = users.size

    class MainHolder(private val binding : CardUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserModel, listener: UserListener) {
            binding.user = user
            binding.imageIcon.setImageResource(R.drawable.baseline_image_not_supported_24)
            if(user.image != "") {
                Picasso.get().load(user.image)
                    .resize(200,200)
                    .transform(customTransformation())
                    .centerCrop()
                    .into(binding.imageIcon)
            }
            binding.root.setOnClickListener { listener.onUserClick(user) }
            binding.executePendingBindings()
        }
    }
}