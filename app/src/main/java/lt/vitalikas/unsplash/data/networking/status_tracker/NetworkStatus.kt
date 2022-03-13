package lt.vitalikas.unsplash.data.networking.status_tracker

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}