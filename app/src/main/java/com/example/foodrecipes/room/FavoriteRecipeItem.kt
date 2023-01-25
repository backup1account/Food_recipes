package com.example.foodrecipes.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favrecipes_table")
data class FavoriteRecipeItem(
    @Embedded val recipeItem: RecipeItem
) {
    @PrimaryKey(autoGenerate = true) var favUid: Int = 0
}
