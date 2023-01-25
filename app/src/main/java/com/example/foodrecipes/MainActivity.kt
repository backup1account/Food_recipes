package com.example.foodrecipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navigateToFragment(HomeFragment())

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.homepage -> navigateToFragment(HomeFragment())
                R.id.favorites -> navigateToFragment(FavoriteRecipesFragment())
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}