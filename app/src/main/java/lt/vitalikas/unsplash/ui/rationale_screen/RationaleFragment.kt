package lt.vitalikas.unsplash.ui.rationale_screen

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import lt.vitalikas.unsplash.R

class RationaleFragment : DialogFragment() {

    private val launcher: Launcher?
        get() = parentFragment?.childFragmentManager?.primaryNavigationFragment?.let {
            it as Launcher
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.perm_all)
            .setPositiveButton(
                getString(R.string.btn_grant)
            ) { _, _ ->
                launcher?.onGrantButtonClick()
            }
            .setNegativeButton(
                getString(R.string.btn_cancel),
                null
            )
            .create()
    }
}