package lt.vitalikas.unsplash.ui.feed_screen

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PhotoOffsetDecoration @Inject constructor(
    @ApplicationContext private val context: Context
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = 8.dpToPx(context)
        with(outRect) {
            left = offset
            right = offset
            top = offset
            bottom = offset
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.densityDpi
        val pixelsInDp = density / DisplayMetrics.DENSITY_DEFAULT
        return this * pixelsInDp
    }
}