package com.example.foodrecipes

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodrecipes.interfaces.OnItemClickListener
import com.example.foodrecipes.room.RecipeItem
import com.example.foodrecipes.room.RecipesApplication
import kotlinx.coroutines.*


class HomeFragment : Fragment(), OnItemClickListener {
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
        val view = inflater.inflate(R.layout.fragment_home_list, container, false)
        val rView = view.findViewById<RecyclerView>(R.id.home_rview)

        val searchBar = view.findViewById<SearchView>(R.id.searchbar)

        if (rView is RecyclerView) {
            with(rView) {
                layoutManager = GridLayoutManager(context, columnCount)

                recipesViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
                    adapter = HomeRecyclerViewAdapter(recipes, this@HomeFragment)

                    searchBar.isSubmitButtonEnabled = true

                    searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            if (query != null) {
                                recipesViewModel.findByTitle("$query%").observe(viewLifecycleOwner) {
                                    adapter = HomeRecyclerViewAdapter(it, this@HomeFragment)
                                }
                            }
                            return true
                        }
                        override fun onQueryTextChange(query: String?): Boolean {
                            return true
                        }
                    })
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

    override fun onAddFavoriteItemClick(item: RecipeItem) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                recipesViewModel.addFavoriteRecipe(item)
            }
        }
    }

    override fun onDeleteFavoriteItemClick(item: RecipeItem) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                recipesViewModel.deleteFavoriteRecipe(item)
            }
        }
    }

//    fun searchDatabase(query: String) {
//        recipesViewModel.findByTitle(query).observe(this) {
//            // set adapter data = it
//            Log.d("a", "$it")
//        }
//
//    }

}