package com.example.foodrecipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodrecipes.interfaces.OnItemClickListener
import com.example.foodrecipes.room.RecipeItem
import com.example.foodrecipes.room.RecipesApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoriteRecipesFragment : Fragment(), OnItemClickListener {
    private var columnCount = 2

    private val recipesViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((activity?.application as RecipesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_recipes_list, container, false)
        val rView = view.findViewById<RecyclerView>(R.id.favorites_rview)

        if (rView is RecyclerView) {
            with(rView) {
                layoutManager = GridLayoutManager(context, columnCount)

                recipesViewModel.allFavoriteRecipes.observe(viewLifecycleOwner) { favRecipes ->
                    adapter = FavoriteRecipesRecyclerViewAdapter(favRecipes, this@FavoriteRecipesFragment)
                }
            }
        }

        return view
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
    }

    override fun onRecipeItemClick(item: RecipeItem) {
        val fragmentDetails = RecipeDetailsFragment()
        fragmentDetails.arguments = bundleOf("recipe_id" to item.uid)

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragmentDetails)
            .commit()
    }

    override fun onAddFavoriteItemClick(item: RecipeItem) {}

    override fun onDeleteFavoriteItemClick(item: RecipeItem) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                recipesViewModel.deleteFavoriteRecipe(item)
            }
        }
    }
}