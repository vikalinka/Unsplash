package lt.vitalikas.unsplash.utils

sealed class PermStatus {
    object Granted : PermStatus()
    object NotGranted : PermStatus()
    object NeedRationale : PermStatus()
    object NeedCheckSettings : PermStatus()
}