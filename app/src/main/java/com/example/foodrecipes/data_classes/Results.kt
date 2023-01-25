package com.example.foodrecipes.data_classes

import com.example.foodrecipes.room.RecipeItem

data class Results(
    var count: Int? = null,
    var results: List<RecipeItem>? = null
)
