package com.aisle.testapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aisle.testapp.R
import com.aisle.testapp.databinding.ActivityMainBinding
import com.aisle.testapp.others.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initGUI()
    }

    private fun initGUI() {
        /*initialise all UI components...*/
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        val startDestinationId =
            if (sharedPreferences.getBoolean(AppConstants.USER_LOGGED_IN, false)) {
                R.id.notesFragment
            } else {
                R.id.loginFragment
            }
        /*setting the destination id based on user logged in or not...*/
        navGraph.setStartDestination(startDestinationId)
        navController.graph = navGraph
        binding.bottomNavigationView.setupWithNavController(navController)

        /*to set notification badges...*/
        binding.bottomNavigationView.getOrCreateBadge(R.id.notesFragment).number = 9
        binding.bottomNavigationView.getOrCreateBadge(R.id.matchesFragment).apply {
            maxCharacterCount = 3
            number = 50
        }

        /*to hide bottom navigation view for login and otp fragment...*/
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.verifyOtpFragment) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}