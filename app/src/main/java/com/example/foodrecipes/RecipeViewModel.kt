package com.example.foodrecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.room.FavoriteRecipeItem
import com.example.foodrecipes.room.RecipeItem
import com.example.foodrecipes.room.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    val allRecipes: LiveData<List<RecipeItem>> = repository.allRecipes.asLiveData()
    val allFavoriteRecipes: LiveData<List<FavoriteRecipeItem>> = repository.allFavoriteRecipes.asLiveData()

    fun getRecipe(id: Int): LiveData<RecipeItem> {
        return repository.getRecipe(id).asLiveData()
    }

    fun findByTitle(text: String): LiveData<List<RecipeItem>> {
        return repository.findByTitle(text).asLiveData()
    }

    suspend fun addFavoriteRecipe(item: RecipeItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.addFavoriteRecipe(item)
    }

    suspend fun deleteFavoriteRecipe(item: RecipeItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFavoriteRecipe(item)
    }
}