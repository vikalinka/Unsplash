//package lt.vitalikas.unsplash.data.repositories
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import lt.vitalikas.unsplash.data.apis.UnsplashApi
//import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
//import lt.vitalikas.unsplash.domain.models.FeedPhoto
//import okio.IOException
//import retrofit2.HttpException
//import javax.inject.Inject
//
//class FeedPhotosPagingSource @Inject constructor(
//    private val api: UnsplashApi
//) : PagingSource<Int, FeedPhotoEntity>() {
//
//    override fun getRefreshKey(state: PagingState<Int, FeedPhotoEntity>): Int? =
//        state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedPhotoEntity> {
//        val pageIndex = params.key ?: STARTING_PAGE_INDEX
//        return try {
//            val photos = api.getFeedPhotos(
//                pageIndex,
//                ITEMS_PER_PAGE,
//                ORDER_BY
//            ).map { photo ->
//                FeedPhotoEntity(
//                    id = photo.id,
//                    createdAt = photo.createdAt,
//                    updatedAt = photo.updatedAt,
//                    width = photo.width,
//                    height = photo.height,
//                    color = photo.color,
//                    blurHash = photo.blurHash,
//                    likes = photo.likes,
//                    likedByUser = photo.likedByUser,
//                    description = photo.description,
//                    "121"
//                )
//            }
//            val prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1
//            val nextKey =
//                if (photos.isEmpty()) null else pageIndex + 1
//            LoadResult.Page(
//                data = photos,
//                prevKey = prevKey,
//                nextKey = nextKey
//            )
//        } catch (e: IOException) {
//            return LoadResult.Error(e)
//        } catch (e: HttpException) {
//            return LoadResult.Error(e)
//        }
//    }
//
//    companion object {
//        private const val ITEMS_PER_PAGE = 12
//        private const val STARTING_PAGE_INDEX = 1
//        private const val ORDER_BY = "popular"
//    }
//}