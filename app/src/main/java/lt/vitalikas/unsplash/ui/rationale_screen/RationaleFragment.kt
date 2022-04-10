package lt.vitalikas.unsplash.ui.rationale_screen

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import lt.vitalikas.unsplash.R

class RationaleFragment : DialogFragment() {

    private val onGrantButtonClickCallback: OnGrantButtonClickCallback?
        get() = parentFragment?.childFragmentManager?.primaryNavigationFragment?.let {
            it as OnGrantButtonClickCallback
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.perm_all)
            .setPositiveButton(
                getString(R.string.btn_grant)
            ) { _, _ ->
                onGrantButtonClickCallback?.onGrantButtonClick()
            }
            .setNegativeButton(
                getString(R.string.btn_cancel),
                null
            )
            .create()
    }
}