package mick.studio.itsfuntorun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.CardRunBinding
import mick.studio.itsfuntorun.helpers.customTransformation
import mick.studio.itsfuntorun.models.RunModel

interface RunListener {
    fun onRunClick(run: RunModel)
}
class RunListAdapter constructor(private var runs: List<RunModel>, private val listener: RunListener) :
                                RecyclerView.Adapter<RunListAdapter.MainHolder>() {
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
           val binding = CardRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)

           return MainHolder(binding)
       }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val run = runs[holder.adapterPosition]
        holder.bind(run, listener)
    }

    override fun getItemCount(): Int = runs.size

    class MainHolder(private val binding : CardRunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(run: RunModel, listener: RunListener) {
            binding.run = run
            binding.imageIcon.setImageResource(R.drawable.baseline_image_not_supported_24)
            if(run.photoUrl != "") {
                Picasso.get().load(run.photoUrl)
                    .resize(200,200)
                    .transform(customTransformation())
                    .centerCrop()
                    .into(binding.imageIcon)
            }
            binding.root.setOnClickListener { listener.onRunClick(run) }
            binding.executePendingBindings()
        }
    }
}