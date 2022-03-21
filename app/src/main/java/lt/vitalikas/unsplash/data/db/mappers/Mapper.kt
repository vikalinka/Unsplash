package lt.vitalikas.unsplash.data.db.mappers

interface Mapper<in I, out O> {
    fun mapPojoToEntity(from: I): O
}