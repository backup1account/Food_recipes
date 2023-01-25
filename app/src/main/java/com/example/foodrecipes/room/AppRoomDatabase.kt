package com.example.foodrecipes.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodrecipes.NetworkHelper
import com.example.foodrecipes.data_classes.Results
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Response
import java.io.IOException


@Database(entities = [RecipeItem::class, FavoriteRecipeItem::class], version = 1, exportSchema = false)
@TypeConverters(RecipesConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipeDao

    private class AppRoomDatabaseCallback(
        private val scope: CoroutineScope,
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.recipesDao())
                }
            }
        }

        @Suppress("RedundantSuspendModifier")
        suspend fun populateDatabase(recipeDao: RecipeDao) {
            recipeDao.deleteAllRecipes()

            NetworkHelper.client.newCall(NetworkHelper.getRequest()).enqueue(object: okhttp3.Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val resultList: MutableList<RecipeItem> = mutableListOf()

                        val body = response.body()?.string()
                        val gsonBody = Gson().fromJson(body, Results::class.java)

                        gsonBody.results?.forEach {
                            if (it.instructions != null) {
                                resultList.add(it)
                            }
                        }

                        gsonBody.results?.let { recipeDao.insertListOfRecipes(resultList) }
                    } else {
                        println("Response code: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("Error message: ${e.message}")
                }
            })


            Log.d("a", "db populating! yea")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val db_instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "recipes_database"
                )
                    .addCallback(AppRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = db_instance
                db_instance
            }
        }
    }

}