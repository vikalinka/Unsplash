package lt.vitalikas.unsplash.data.db.mappers

interface Mapper<in I, out O> {
    fun map(from: I): O
}