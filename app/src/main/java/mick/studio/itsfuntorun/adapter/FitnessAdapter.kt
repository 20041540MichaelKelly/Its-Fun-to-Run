package mick.studio.itsfuntorun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.CardFitnessBinding
import mick.studio.itsfuntorun.databinding.CardRunBinding
import mick.studio.itsfuntorun.helpers.customTransformation
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.fitness.FitnessModel

interface FitnessListener {
    fun onFitnessClick(excersise: FitnessModel)
}
class FitnessAdapter constructor(private var excersises: List<FitnessModel>, private val listener: FitnessListener) :
                                RecyclerView.Adapter<FitnessAdapter.MainHolder>() {
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
           val binding = CardFitnessBinding.inflate(LayoutInflater.from(parent.context), parent, false)

           return MainHolder(binding)
       }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val excersise = excersises[holder.adapterPosition]
        holder.bind(excersise, listener)
    }

    override fun getItemCount(): Int = excersises.size

    class MainHolder(private val binding : CardFitnessBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(excersises: FitnessModel, listener: FitnessListener) {
            binding.excersise = excersises

            binding.root.setOnClickListener { listener.onFitnessClick(excersises) }
            binding.executePendingBindings()
        }
    }
}