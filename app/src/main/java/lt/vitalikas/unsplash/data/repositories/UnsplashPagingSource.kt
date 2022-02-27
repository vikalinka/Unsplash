package lt.vitalikas.unsplash.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto

class UnsplashPagingSource(
    private val api: UnsplashApi,
    private val query: String,
    private val page: Int,
    private val pageSize: Int,
    private val orderBy: String
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
            val pageIndex = params.key ?: page

            // initial load size (initial perPage value) = 3 * PAGE_SIZE
            val response = api.searchPhotos(
                query = query,
                page = pageIndex,
                perPage = params.loadSize,
                orderBy = orderBy
            )
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (pageIndex > 1) pageIndex - 1 else null,
                nextKey = if (photos.isEmpty()) null else pageIndex + (params.loadSize / pageSize)
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}