package ru.captaindmitro.warehouseapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.captaindmitro.warehouseapp.R
import ru.captaindmitro.warehouseapp.databinding.ItemHomeProductBinding
import ru.captaindmitro.warehouseapp.domain.models.Product
import ru.captaindmitro.warehouseapp.presentation.home.HomeFragmentDirections

class HomeAdapter(
    private val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    private val asyncListDiffer =  AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.apply {
            binding.root.setOnClickListener { view ->
                onClickListener(position)
                view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment())
            }
            bind(asyncListDiffer.currentList[position])
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun setData(newProducts: List<Product>) {
        asyncListDiffer.submitList(newProducts)
    }

    class HomeViewHolder(val binding: ItemHomeProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.nameTextView.text = product.name
            binding.codeTextView.text = binding.root.context.getString(R.string.code_field, product.code)
            binding.priceTextView.text = binding.root.context.getString(R.string.price_field, product.price)
            binding.remainTextView.text = product.remain.toString()
        }

    }
}
