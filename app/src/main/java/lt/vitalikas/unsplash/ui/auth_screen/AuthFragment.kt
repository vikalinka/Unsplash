package lt.vitalikas.unsplash.ui.auth_screen

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentAuthBinding
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import timber.log.Timber

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

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
                exception != null -> authViewModel.onAuthFailed()
                tokenExchangeRequest != null ->
                    authViewModel.performTokenRequest(tokenExchangeRequest)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openLoginPage()
        bindAuthViewModel()
    }

    private fun openLoginPage() {
        authViewModel.openLoginPage()
    }

    private fun bindAuthViewModel() {
        authViewModel.authPageIntent.observe(viewLifecycleOwner) { authIntent ->
            launcher.launch(authIntent)
        }

        authViewModel.authFailed.observe(viewLifecycleOwner) {
            Timber.d("$it")
        }

        authViewModel.authSuccess.observe(viewLifecycleOwner) {
            Timber.d("AUTH SUCCESS")
        }
    }
}