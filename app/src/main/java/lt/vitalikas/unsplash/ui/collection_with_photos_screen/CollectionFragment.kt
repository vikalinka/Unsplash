package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentCollectionBinding

@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private val binding by viewBinding(FragmentCollectionBinding::bind)

    private val collectionViewModel by viewModels<CollectionViewModel>()

    private val args by navArgs<CollectionFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCollection(args.id)
    }

    private fun getCollection(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            collectionViewModel.getCollection(id)
        }
    }
}