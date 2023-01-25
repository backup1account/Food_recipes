package com.example.foodrecipes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.foodrecipes.interfaces.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide

import com.example.foodrecipes.room.RecipeItem


class HomeRecyclerViewAdapter(
    private val values: List<RecipeItem>,
    private val onItemClicked: OnItemClickListener
) : ListAdapter<RecipeItem, HomeRecyclerViewAdapter.RecipeViewHolder>(RecipesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = values[position]

        holder.bind(item.name, item.thumbnail_url, item.favorite)

        holder.titleView.setOnClickListener {
            onItemClicked.onRecipeItemClick(item)
        }

        holder.favoriteBtn.setOnClickListener {
            when (item.favorite) {
                1 -> {
                    onItemClicked.onDeleteFavoriteItemClick(item)
                    holder.favoriteBtn.setBackgroundResource(R.drawable.added_favorite_item_foreground)
                }
                0 -> {
                    onItemClicked.onAddFavoriteItemClick(item)
                    holder.favoriteBtn.setBackgroundResource(R.drawable.add_favorite_foreground)
                }
            }
        }
    }

    override fun getItemCount(): Int = values.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val titleView: TextView = itemView.findViewById(R.id.item_title)

        val favoriteBtn: ImageButton = itemView.findViewById(R.id.add_favorite_recipe_btn)

        fun bind(title: String?, imageUrl: String?, isFavorite: Int?) {
            titleView.text = title

            if (imageUrl != null) {
                Glide.with(itemView)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView)
            }

            when (isFavorite) {
                1 -> favoriteBtn.setBackgroundResource(R.drawable.added_favorite_item_foreground)
                0 -> favoriteBtn.setBackgroundResource(R.drawable.add_favorite_foreground)
            }
        }

        companion object {
            fun create(parent: ViewGroup): RecipeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_home, parent, false)
                return RecipeViewHolder(view)
            }
        }
    }

    class RecipesComparator : DiffUtil.ItemCallback<RecipeItem>() {
        override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean {
            return oldItem.name == newItem.name
        }
    }
}