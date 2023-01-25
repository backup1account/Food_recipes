package com.example.foodrecipes.room

import androidx.room.TypeConverter
import com.example.foodrecipes.data_classes.IngredientComponent
import com.example.foodrecipes.data_classes.InnerIngredientComponent
import com.example.foodrecipes.data_classes.Instruction
import com.example.foodrecipes.data_classes.Tag

class RecipesConverter {
    private val separator = ","

    @TypeConverter
    fun instructionsToString(instructions: MutableList<Instruction>?): String? {
        return instructions?.map { it.display_text }?.joinToString(separator = separator)
    }
    @TypeConverter
    fun stringToInstructions(instructions: String?): MutableList<Instruction> {
        val result: MutableList<Instruction> = mutableListOf()
        instructions?.split(separator)?.forEach { result.add(Instruction(it)) }
        return result
    }

    @TypeConverter
    fun sectionsToString(sections: MutableList<IngredientComponent>?): String {
        val result: MutableList<String?> = mutableListOf()
        sections?.get(0)?.components?.forEach {
            result.add(it.raw_text)
        }
        return result.joinToString(separator)
    }
    @TypeConverter
    fun stringToSections(sections: String?): MutableList<IngredientComponent> {
        val temp: MutableList<InnerIngredientComponent> = mutableListOf()
        val res: MutableList<IngredientComponent> = mutableListOf()

        sections?.split(separator)?.forEach {
            temp.add(InnerIngredientComponent(raw_text = it))
        }

        res.add(IngredientComponent(temp))
        return res
    }

    @TypeConverter
    fun tagsToString(tags: MutableList<Tag>?): String? {
        return tags?.joinToString {
            it.display_name.toString()
        }
    }
    @TypeConverter
    fun stringToTags(tags: String?): MutableList<Tag> {
        val result: MutableList<Tag> = mutableListOf()
        tags?.split(separator)?.forEach { result.add(Tag(it)) }
        return result
    }
}