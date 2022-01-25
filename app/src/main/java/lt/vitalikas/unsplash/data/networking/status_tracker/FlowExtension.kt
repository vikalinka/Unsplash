package lt.vitalikas.unsplash.data.networking.status_tracker

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T> Flow<NetworkStatus>.map(
    crossinline onUnavailable: suspend () -> T,
    crossinline onAvailable: suspend () -> T
): Flow<T> = map { status ->
    when (status) {
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
    }
}