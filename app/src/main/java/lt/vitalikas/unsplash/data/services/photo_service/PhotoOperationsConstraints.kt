package lt.vitalikas.unsplash.data.services.photo_service

import androidx.work.Constraints
import androidx.work.NetworkType

object PhotoOperationsConstraints {

    object ReactionConstraints {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(false)
            .build()
    }

    object FetchingConstraints {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()
    }
}