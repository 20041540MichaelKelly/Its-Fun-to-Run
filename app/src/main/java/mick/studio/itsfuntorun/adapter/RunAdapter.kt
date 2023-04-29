//package mick.studio.itsfuntorun.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import mick.studio.itsfuntorun.R
//import mick.studio.itsfuntorun.databinding.CardRunBinding
//import mick.studio.itsfuntorun.databinding.FragmentRunBinding
//import mick.studio.itsfuntorun.models.RunModel
//
//class RunAdapter constructor(private var run: RunModel)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunAdapter.MainHolder {
//        val binding = FragmentRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//
//        return RunAdapter.MainHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: RunAdapter.MainHolder, position: Int) {
//        holder.bind(run)
//    }
//
//    class MainHolder(private val binding : FragmentRunBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(run: RunModel) {
//            //binding.paymentamount.text = donation.amount.toString()
//            //binding.paymentmethod.text = donation.paymentmethod
//
//            binding.run = run
//            binding.imageIcon.setImageResource(R.drawable.baseline_image_not_supported_24)
//            //Include this call to force the bindings to happen immediately
//            binding.executePendingBindings()
//        }
//    }
//}