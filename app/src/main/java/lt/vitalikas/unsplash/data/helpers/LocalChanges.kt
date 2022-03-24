package lt.vitalikas.unsplash.data.helpers

/**
 * Class for saving photo details (photo reactions with total count) local changes
 * to avoid DiffUtil to work with mutable list.
 */
class LocalChanges {
    val reactionFlags = mutableMapOf<String, Boolean>()
    val reactionTotalCounts = mutableMapOf<String, Int>()
}

/**
 * Non-data class which allows passing the same reference
 * to the MutableStateFlow multiple times in a row.
 */
class OnChange<T>(val value: T)