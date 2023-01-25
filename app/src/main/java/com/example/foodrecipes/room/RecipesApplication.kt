package com.example.foodrecipes.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RecipesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { RecipeRepository(database.recipesDao()) }
}