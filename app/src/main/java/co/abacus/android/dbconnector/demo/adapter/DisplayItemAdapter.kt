package co.abacus.android.dbconnector.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.abacus.android.dbconnector.demo.databinding.AdapterDisplayItemListBinding
import co.abacus.android.dbconnector.demo.model.DisplayItem

class DisplayItemAdapter : RecyclerView.Adapter<DisplayItemAdapter.ViewHolder>(){

    private val items = mutableListOf<DisplayItem>()

    fun setItems(items : List<DisplayItem>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding : AdapterDisplayItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DisplayItem){
            binding.id.text = item.id
            binding.name.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(AdapterDisplayItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}