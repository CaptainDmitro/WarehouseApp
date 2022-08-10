package ru.captaindmitro.warehouseapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.captaindmitro.warehouseapp.databinding.ItemHomeProductBinding
import ru.captaindmitro.warehouseapp.domain.models.Product

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private val products: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.apply {
            // TODO: Set click listener
            bind(products[position])
        }
    }

    override fun getItemCount(): Int = products.size

    fun setData(newProducts: List<Product>) {
        DiffUtil.calculateDiff(DiffCallback(products, newProducts)).dispatchUpdatesTo(this)
        products.clear()
        products.addAll(newProducts)

        val diffCallback = DiffCallback(products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products.clear()
        products.addAll(newProducts)
        diffResult.dispatchUpdatesTo(this)
    }

    class HomeViewHolder(val binding: ItemHomeProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.nameTextView.text = product.name
            binding.codeTextView.text = product.code.toString()
            binding.priceTextView.text = product.price.toString()
            binding.remainTextView.text = product.remain.toString()
        }

    }
}
