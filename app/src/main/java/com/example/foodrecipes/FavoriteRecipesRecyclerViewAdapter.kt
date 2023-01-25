package com.example.foodrecipes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.foodrecipes.interfaces.OnItemClickListener
import com.example.foodrecipes.room.FavoriteRecipeItem


class FavoriteRecipesRecyclerViewAdapter(
    private val values: List<FavoriteRecipeItem>,
    private val onFavoriteItemClicked: OnItemClickListener
) : ListAdapter<FavoriteRecipeItem, FavoriteRecipesRecyclerViewAdapter.FavoriteRecipeViewHolder>(FavoriteRecipesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder {
        return FavoriteRecipeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item.recipeItem.name, item.recipeItem.thumbnail_url)

        holder.titleView.setOnClickListener {
            onFavoriteItemClicked.onRecipeItemClick(item.recipeItem)
        }

        holder.favoriteBtn.setOnClickListener {
            onFavoriteItemClicked.onDeleteFavoriteItemClick(item.recipeItem)
        }
    }

    override fun getItemCount(): Int = values.size

    class FavoriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fav_item_image)
        val titleView: TextView = itemView.findViewById(R.id.fav_item_title)

        val favoriteBtn: ImageButton = itemView.findViewById(R.id.add_favorite_recipe_btn2)

        fun bind(title: String?, imageUrl: String?) {
            titleView.text = title

            if (imageUrl != null) {
                Glide.with(itemView)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView)
            }

            favoriteBtn.setBackgroundResource(R.drawable.added_favorite_item_foreground)
        }

        companion object {
            fun create(parent: ViewGroup): FavoriteRecipeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_favorite_recipes, parent, false)
                return FavoriteRecipeViewHolder(view)
            }
        }
    }

    class FavoriteRecipesComparator : DiffUtil.ItemCallback<FavoriteRecipeItem>() {
        override fun areItemsTheSame(oldItem: FavoriteRecipeItem, newItem: FavoriteRecipeItem): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: FavoriteRecipeItem, newItem: FavoriteRecipeItem): Boolean {
            return oldItem.recipeItem == newItem.recipeItem
        }
    }

}