package lt.vitalikas.unsplash.ui.auth_screen

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentAuthBinding
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import timber.log.Timber

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val loading get() = binding.pbLoading
    private val signin get() = binding.mbSignin
    private val image get() = binding.ivAuthImage
    private val text get() = binding.tvAuthTitle

    private val authViewModel by viewModels<AuthViewModel>()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data
        if (result.resultCode == Activity.RESULT_OK && intent != null) {
            val tokenExchangeRequest =
                AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()
            val exception = AuthorizationException.fromIntent(intent)
            when {
                exception != null -> {
                    loading.isVisible = false
                    listOf(image, text, signin).forEach { view ->
                        view.isVisible = true
                    }
                    showSnackbar(R.string.auth_failed)
                    Timber.d("Authorization failed")
                }
                tokenExchangeRequest != null ->
                    authViewModel.performTokenRequest(tokenExchangeRequest)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindAuthViewModel()
        bindData()
    }

    private fun bindAuthViewModel() {
        signin.setOnClickListener {
            authViewModel.openLoginPage()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    authViewModel.authState.collect { state ->
                        when (state) {
                            is AuthState.Loading -> {
                                loading.isVisible = true
                                listOf(image, text, signin).forEach { view ->
                                    view.isVisible = false
                                }
                            }
                            is AuthState.LoggedIn -> {
                                loading.isVisible = false
                                listOf(image, text, signin).forEach { view ->
                                    view.isVisible = false
                                }
                                Timber.d("Authorization succeed")
                                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToHostFragment())
                            }
                            is AuthState.Error -> {
                                loading.isVisible = false
                                listOf(image, text, signin).forEach { view ->
                                    view.isVisible = true
                                }
                                showSnackbar(state.id)
                                Timber.d("Authorization failed")
                            }
                            is AuthState.NotLoggedIn -> {
                                Timber.d("Not logged in")
                            }
                        }
                    }
                }
            }
        }

        authViewModel.authPageIntent.observe(viewLifecycleOwner) { authIntent ->
            launcher.launch(authIntent)
        }
    }

    private fun showSnackbar(@StringRes textRes: Int) {
        Snackbar
            .make(
                requireView(),
                getString(textRes),
                Snackbar.LENGTH_LONG
            )
            .setAction(getString(R.string.retry)) {
                authViewModel.openLoginPage()
            }
            .show()
    }

    private fun bindData() {
        text.text = getString(R.string.sign_in_text)
        Glide.with(this)
            .load(R.drawable.signin)
            .into(image)
    }
}