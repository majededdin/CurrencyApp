package com.majed.currencyapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majed.currencyapp.data.model.service.RatesWithName
import com.majed.currencyapp.databinding.ItemRateBinding

class RatesAdapter(
    private val callback: RatesCallBack
) : RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    private var items: ArrayList<RatesWithName> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.txtName.text = name
                binding.txtValue.text = value.toString()
            }
        }
    }


    fun setItem(items: List<RatesWithName>) {
        this.items = items as ArrayList<RatesWithName>
        notifyDataSetChanged()
    }


    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size


    inner class ViewHolder(val binding: ItemRateBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val rate = items[bindingAdapterPosition]
            callback.onDefaultClicked(rate)
        }
    }

    interface RatesCallBack {
        fun onDefaultClicked(ratesWithName: RatesWithName)
    }

}