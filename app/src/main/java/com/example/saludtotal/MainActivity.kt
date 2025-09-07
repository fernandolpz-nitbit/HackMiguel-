package com.tuempresa.saludtotal.test

import android.os.Bundle
import android.view.View // ✅ IMPORTACIÓN NECESARIA PARA 'View.GONE' Y 'View.VISIBLE'
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tuempresa.saludtotal.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main)
            ?.findNavController()
            ?: throw IllegalStateException("NavController no encontrado")

        // Ajustamos la configuración para que no incluya 'home' en la barra de acción principal,
        // ya que esa pantalla no tendrá barra de título.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // Este listener se encarga de mostrar u ocultar las barras de navegación
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Si estamos en la pantalla de login (HomeFragment)
                R.id.navigation_home -> {
                    binding.navView.visibility = View.GONE
                    supportActionBar?.hide()
                }
                // Para todas las demás pantallas
                else -> {
                    binding.navView.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }

    }
}