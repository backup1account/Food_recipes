package com.example.foodrecipes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes_table")
    fun getAllRecipes(): Flow<List<RecipeItem>>

    @Query("SELECT * FROM recipes_table WHERE name LIKE :text")
    fun findByTitle(text: String): Flow<List<RecipeItem>>

    @Query("SELECT * FROM favrecipes_table")
    fun getFavoriteRecipes(): Flow<List<FavoriteRecipeItem>>

    @Query("SELECT * FROM recipes_table WHERE uid LIKE :id")
    fun getRecipe(id: Int) : Flow<RecipeItem>

    @Insert(entity = RecipeItem::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertListOfRecipes(itemList: List<RecipeItem>)

    @Query("UPDATE recipes_table SET favorite=1 WHERE uid LIKE :id")
    fun addFavoriteRecipeRecipesTable(id: Int)

    @Insert(entity = FavoriteRecipeItem::class, onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteRecipeFavrecipesTable(item: FavoriteRecipeItem)

    @Transaction
    fun addFavoriteRecipe(item: RecipeItem) {
        val favItem = FavoriteRecipeItem(item)
        addFavoriteRecipeRecipesTable(item.uid)
        addFavoriteRecipeFavrecipesTable(favItem)
    }

    @Query("UPDATE recipes_table SET favorite=0 WHERE uid LIKE :id")
    fun deleteFavoriteRecipeRecipesTable(id: Int)

    @Query("DELETE FROM favrecipes_table WHERE uid LIKE :id")
    fun deleteFavoriteRecipeFavrecipesTable(id: Int)

    @Transaction
    fun deleteFavoriteRecipe(item: RecipeItem) {
        deleteFavoriteRecipeRecipesTable(item.uid)
        deleteFavoriteRecipeFavrecipesTable(item.uid)
    }

    @Query("DELETE FROM recipes_table")
    fun deleteAllRecipes()
}