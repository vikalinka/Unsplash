package lt.vitalikas.unsplash.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class FeedPhotosPagingSource @Inject constructor(
    private val api: UnsplashApi
) : PagingSource<Int, FeedPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, FeedPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedPhoto> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val photos = api.getFeedPhotos(
                pageIndex,
                ORDER_BY
            )
            Timber.d("Photos = $photos")
            val nextKey =
                if (photos.isEmpty()) null else pageIndex + (params.loadSize / DEFAULT_ITEM_NUMBER_PER_PAGE)
            LoadResult.Page(
                data = photos,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val DEFAULT_ITEM_NUMBER_PER_PAGE = 10
        private const val STARTING_PAGE_INDEX = 1
        private const val ORDER_BY = "popular"
    }
}