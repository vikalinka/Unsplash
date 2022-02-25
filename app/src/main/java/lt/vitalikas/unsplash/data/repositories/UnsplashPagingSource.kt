package lt.vitalikas.unsplash.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto

class UnsplashPagingSource(
    private val api: UnsplashApi,
    private val query: String
) : PagingSource<Int, SearchPhoto>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, SearchPhoto>): Int? {
        // get the most recently accessed index in data list
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate it manually
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchPhoto> {
        return try {

            // pageIndex = params.key, perPage = params.loadSize
            val pageIndex = params.key ?: STARTING_PAGE_INDEX

            // initial load size (initial perPage value) = 3 * NETWORK_PAGE_SIZE
            val response = api.searchPhotos(query = query)

            LoadResult.Page(
                data = response.results,
                prevKey = if (pageIndex > 1) pageIndex - 1 else null,
                nextKey = if (pageIndex < response.total) pageIndex + 1 else null
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    companion object {
        // default per_page value = 1
        private const val STARTING_PAGE_INDEX = 1

        // default order_by value = latest
        private const val ORDER_BY = "latest"
    }
}