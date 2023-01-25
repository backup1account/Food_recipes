package com.example.foodrecipes.interfaces

import com.example.foodrecipes.room.RecipeItem

interface OnItemClickListener {
    fun onRecipeItemClick(item: RecipeItem)
    fun onAddFavoriteItemClick(item: RecipeItem)
    fun onDeleteFavoriteItemClick(item: RecipeItem)
}