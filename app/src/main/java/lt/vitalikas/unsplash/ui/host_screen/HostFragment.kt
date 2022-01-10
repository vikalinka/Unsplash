package lt.vitalikas.unsplash.ui.host_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentHostBinding

class HostFragment : Fragment(R.layout.fragment_host) {

    private val binding by viewBinding(FragmentHostBinding::bind)
    private val bottomNav get() = binding.bottomNavigation

    private val navController by lazy {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container)
                ?: error("Fragment not found")

        navHostFragment.findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNavigation()
        setListener()
    }

    private fun setUpNavigation() {
        bottomNav.setupWithNavController(navController)
    }

    private fun setListener() {
        bottomNav.setOnItemSelectedListener { item ->
            item.onNavDestinationSelected(navController)
        }
    }
}