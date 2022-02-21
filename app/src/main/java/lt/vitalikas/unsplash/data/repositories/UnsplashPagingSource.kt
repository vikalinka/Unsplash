package lt.vitalikas.unsplash.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import javax.inject.Inject

class UnsplashPagingSource @Inject constructor(
    private val query: String,
    private val api: UnsplashApi
) : PagingSource<Int, FeedPhoto>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, FeedPhoto>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedPhoto> {
        try {
            val currentPageKey = params.key ?: STARTING_PAGE_INDEX
            val prevPageKey = (currentPageKey - 1).takeIf { it != 1 }

            val photos = api.searchPhotos(
                query,
                currentPageKey,
                params.loadSize,
                ORDER_BY
            )

            val nextPageKey = (currentPageKey + 1).takeIf { photos.isNotEmpty() }

            return LoadResult.Page(
                data = photos,
                prevKey = prevPageKey,
                nextKey = nextPageKey
            )
        } catch (t: Throwable) {
            return LoadResult.Error(t)
        }
    }

    companion object {
        private const val ITEMS_PER_PAGE = 10
        private const val STARTING_PAGE_INDEX = 1
        private const val ORDER_BY = "latest"
    }
}