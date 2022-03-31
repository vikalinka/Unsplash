package lt.vitalikas.unsplash.ui.auth_screen

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private val signIn get() = binding.signInMaterialButton
    private val image get() = binding.authImageView
    private val text get() = binding.authTextView
    private val loading get() = binding.loadingProgressBar
    private val itemGroup get() = binding.itemGroup

    private val authViewModel by viewModels<AuthViewModel>()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultCode = result.resultCode
        val intent = result.data
        if (resultCode == Activity.RESULT_OK && intent != null) {
            val tokenRequest =
                AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()
            val exception = AuthorizationException.fromIntent(intent)
            when {
                exception != null -> {
                    loading.isVisible = false
                    itemGroup.isVisible = true
                    exception.message?.let { showSnackbar(it) }
                    Timber.d(exception)
                }
                tokenRequest != null ->
                    authViewModel.performTokenRequest(tokenRequest)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthIntent()
        observeAuth()
        setupUI()
    }

    private fun observeAuthIntent() {
        authViewModel.authPageIntent.observe(viewLifecycleOwner) { authIntent ->
            launcher.launch(authIntent)
        }
    }

    private fun observeAuth() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Loading -> {
                            loading.isVisible = true
                            itemGroup.isVisible = false
                        }
                        is AuthState.LoggedIn -> {
                            loading.isVisible = false
                            itemGroup.isVisible = false
                            Timber.d("Authorization succeeded")
                            showToast(R.string.auth_succeeded)
                            findNavController().navigate(
                                AuthFragmentDirections.actionAuthFragmentToHostFragment()
                            )
                        }
                        is AuthState.Error -> {
                            loading.isVisible = false
                            itemGroup.isVisible = true
                            showSnackbar(state.errorMsg)
                            Timber.d(state.errorMsg)
                        }
                        is AuthState.NotLoggedIn -> {
                            Timber.d("Not logged in")
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
        text.text = getString(R.string.sign_in_text)

        Glide.with(this)
            .load(R.drawable.signin)
            .into(image)

        signIn.setOnClickListener {
            authViewModel.openLoginPage()
        }
    }

    private fun showToast(@StringRes textRes: Int) {
        Toast.makeText(context, textRes, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackbar(msg: String) {
        Snackbar
            .make(
                requireView(),
                msg,
                Snackbar.LENGTH_LONG
            )
            .setAction(getString(R.string.retry)) {
                authViewModel.openLoginPage()
            }
            .show()
    }
}