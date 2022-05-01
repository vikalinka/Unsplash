package lt.vitalikas.unsplash.data.services.photo_service

sealed class WorkType {
    class Reaction(val reaction: Boolean) : WorkType()
    object Download : WorkType()
}
