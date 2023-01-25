package com.example.foodrecipes

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.foodrecipes.room.RecipeItem
import com.example.foodrecipes.room.RecipesApplication


private const val RECIPE_ID = "recipe_id"

class RecipeDetailsFragment : Fragment() {
    private var param1: Int? = null

    private val recipesViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((activity?.application as RecipesApplication).repository)
    }

    var selectedItem: RecipeItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(RECIPE_ID)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)

        param1?.let {
            recipesViewModel.getRecipe(it).observe(viewLifecycleOwner) { recipe ->
                selectedItem = recipe
                view.findViewById<TextView>(R.id.recipe_detail_title).text = selectedItem?.name

                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")

                val preparations = StringBuilder()
                val ingredients = StringBuilder()

                val tagsLayout: GridLayout = view.findViewById(R.id.tags_layout)


                if (selectedItem?.created_at != null) {
                    val date = java.util.Date(selectedItem?.created_at!!.toLong() * 1000)
                    sdf.format(date)
                    view.findViewById<TextView>(R.id.recipe_detail_created_at).text = date.toString()
                }

                view.findViewById<TextView>(R.id.recipe_detail_description).text = when {
                    Build.VERSION.SDK_INT >= 24 -> Html.fromHtml(selectedItem?.description, Html.FROM_HTML_MODE_LEGACY)
                    else -> Html.fromHtml(selectedItem?.description)
                }

                Glide.with(this)
                    .load(selectedItem?.thumbnail_url)
                    .centerCrop()
                    .into( view.findViewById(R.id.recipe_detail_image))


                selectedItem?.instructions?.forEach { instruction ->
                    preparations.append(instruction.display_text)
                }

                selectedItem?.sections?.get(0)?.components?.forEach { ingredient ->
                    ingredients.append("• " + ingredient.raw_text + "\n")
                }

                selectedItem?.tags?.forEach { tag ->
                    val params = GridLayout.LayoutParams()
                    val textView = TextView(context)

                    params.width = LayoutParams.WRAP_CONTENT
                    params.height = LayoutParams.WRAP_CONTENT
                    params.setMargins(5, 5, 5, 10)

                    textView.textSize = 14F
                    textView.setBackgroundResource(R.drawable.textview_border)
                    textView.text = tag.display_name

                    tagsLayout.addView(textView, params)
                }

                view.findViewById<TextView>(R.id.recipe_detail_preparation).text = preparations.toString()
                view.findViewById<TextView>(R.id.recipe_detail_ingredients).text = ingredients.toString()
            }
        }

        return view
    }
}