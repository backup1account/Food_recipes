package com.example.foodrecipes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipes.data_classes.IngredientComponent
import com.example.foodrecipes.data_classes.Instruction
import com.example.foodrecipes.data_classes.Tag

@Entity(tableName = "recipes_table")
data class RecipeItem(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "instructions") val instructions: MutableList<Instruction>?,
    @ColumnInfo(name= "created_at") val created_at: Int?,
    @ColumnInfo(name="sections") val sections: MutableList<IngredientComponent>?,
    @ColumnInfo(name = "tags") val tags: MutableList<Tag>?,
    @ColumnInfo(name = "thumbnail_url") val thumbnail_url: String?
) {
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
    @ColumnInfo(name="favorite") var favorite: Int = 0
}
