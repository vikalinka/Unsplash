package lt.vitalikas.unsplash.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.domain.models.Search

class UnsplashPagingSource(
    val query: String,
    private val api: UnsplashApi,
    val pageSize: Int
) : PagingSource<Int, Search>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        // get the most recently accessed index in data list
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate it manually
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            // pageIndex = params.key, perPage = params.loadSize

            val pageIndex = params.key ?: STARTING_PAGE_INDEX

            // initial load size (initial perPage value) = 3 * NETWORK_PAGE_SIZE
            val photos = api.searchPhotos(
                query = query,
                page = pageIndex,
                perPage = params.loadSize,
                orderBy = ORDER_BY
            )

            LoadResult.Page(
                data = photos,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (photos.size == params.loadSize)
                    pageIndex + (params.loadSize / pageSize)
                else
                    null
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val ORDER_BY = "latest"
    }
}