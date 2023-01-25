package com.example.foodrecipes.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipesDao: RecipeDao) {
    val allRecipes: Flow<List<RecipeItem>> = recipesDao.getAllRecipes()
    val allFavoriteRecipes: Flow<List<FavoriteRecipeItem>> = recipesDao.getFavoriteRecipes()

    @WorkerThread
    fun getRecipe(id: Int): Flow<RecipeItem> {
        return recipesDao.getRecipe(id)
    }

    @WorkerThread
    suspend fun addFavoriteRecipe(item: RecipeItem) {
        recipesDao.addFavoriteRecipe(item)
    }

    @WorkerThread
    suspend fun deleteFavoriteRecipe(item: RecipeItem) {
        recipesDao.deleteFavoriteRecipe(item)
    }

    @WorkerThread
    fun findByTitle(text: String) : Flow<List<RecipeItem>> {
        return recipesDao.findByTitle(text)
    }

    @WorkerThread
    suspend fun insertListOfRecipes(itemList: List<RecipeItem>) {
        recipesDao.insertListOfRecipes(itemList)
    }
}