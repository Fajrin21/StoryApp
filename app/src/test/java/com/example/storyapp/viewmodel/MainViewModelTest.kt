package com.example.storyapp.unit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.database.ListStoryDetail
import com.example.storyapp.repository.MainRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import com.example.storyapp.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Mock
    private lateinit var mainRepository: MainRepository

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when Story Should Not Be Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryDetail> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryDetail>>()
        expectedStory.value = data
        val token = "token"
        Mockito.`when`(mainRepository.getPagingStories(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(mainRepository)
        val actualStory: PagingData<ListStoryDetail> = mainViewModel.getPagingStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.StoryDetailDiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when Story Is Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryDetail> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryDetail>>()
        expectedStory.value = data
        val token = "ini token"
        Mockito.`when`(mainRepository.getPagingStories(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(mainRepository)
        val actualStory: PagingData<ListStoryDetail> = mainViewModel.getPagingStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.StoryDetailDiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, ListStoryDetail>() {
    companion object {
        fun snapshot(items: List<ListStoryDetail>): PagingData<ListStoryDetail> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryDetail>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryDetail> {
        return LoadResult.Page(emptyList(), prevKey = 0, nextKey = 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}