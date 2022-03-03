package lt.vitalikas.unsplash.data.repositories.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse
import timber.log.Timber

class CollectionsPagingSource(
    private val api: UnsplashApi,
    private val page: Int,
    private val pageSize: Int
) : PagingSource<Int, CollectionResponse>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, CollectionResponse>): Int? {
        // get the most recently accessed index in data list
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate it manually
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionResponse> {
        return try {
            // pageIndex = params.key, perPage = params.loadSize
            val pageIndex = params.key ?: page

            // initial load size (initial perPage value) = 3 * PAGE_SIZE
            val response = api.getCollections(
                page = pageIndex,
                perPage = params.loadSize
            )

            Timber.d("$response")

            LoadResult.Page(
                data = response,
                prevKey = if (pageIndex > 1) pageIndex - 1 else null,
                nextKey = if (response.isEmpty()) null else pageIndex + (params.loadSize / pageSize)
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}