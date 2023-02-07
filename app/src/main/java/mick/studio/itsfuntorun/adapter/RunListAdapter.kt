package mick.studio.itsfuntorun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mick.studio.itsfuntorun.databinding.CardRunBinding
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
            binding.runInKms.text = run.runInKms
            binding.runInTime.text = run.runInTime
            binding.root.setOnClickListener { listener.onRunClick(run) }
        }
    }
}