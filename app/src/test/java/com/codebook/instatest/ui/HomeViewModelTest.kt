import android.content.ContentResolver
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.codebook.instatest.core.enums.Methods
import com.codebook.instatest.core.enums.RequestBodyType
import com.codebook.instatest.data.FakeFailureGetRequestHomeRepository
import com.codebook.instatest.data.FakeFailurePostJSONRequestRepository
import com.codebook.instatest.data.FakeFailurePostJSONWithUnknownHostExceptionRequestRepository
import com.codebook.instatest.data.FakeSuccessPostJSONRequestRepository
import com.codebook.instatest.data.FakeSuccessfulGetRequestHomeRepository
import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.repo.HomeRepository
import com.codebook.instatest.domain.FakePostMultipartRequestUseCase
import com.codebook.instatest.domain.remote.GetRequestUseCaseImpl
import com.codebook.instatest.domain.remote.PostJsonRequestUseCaseImpl
import com.codebook.instatest.ui.screens.homeScreen.HomeViewModel
import com.codebook.instatest.ui.screens.homeScreen.states.HomeUiEvents
import com.codebook.instatest.ui.screens.homeScreen.states.NetworkState
import com.codebook.instatest.ui.screens.homeScreen.states.RequestHeader
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel


    private fun setup(
        homeRepository: HomeRepository
    ) {

        viewModel = HomeViewModel(
            getRequestUseCaseImpl = GetRequestUseCaseImpl(homeRepository),
            postJsonRequestUseCaseImpl = PostJsonRequestUseCaseImpl(homeRepository),
            postMultipartRequestUseCaseImpl = FakePostMultipartRequestUseCase(homeRepository),
            contentResolver = mock(ContentResolver::class.java)
        )
    }

    private fun runRequest() {

        val latch = CountDownLatch(1)
        // Observe the LiveData
        val observer = Observer<NetworkState<Call>> {
            if (it is NetworkState.Success) {
                latch.countDown() // Decrement the latch count when data is received
            } // Decrement the latch count when data is received
        }
        viewModel.apiResponse.observeForever(observer)
        viewModel.hitTheApi()
        latch.await(2, TimeUnit.SECONDS)
    }

    @Test
    fun testSuccessfulGetRequest_200() {
        val repo = FakeSuccessfulGetRequestHomeRepository()
        setup(repo)
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/Getsuccess?id=99"
        viewModel.onEvent(HomeUiEvents.UrlEntered(url))
        viewModel.onEvent(HomeUiEvents.MethodSelected(Methods.GET))

        runRequest()
        val apiResponseValue = viewModel.apiResponse.value
        Assert.assertEquals(
            200,
            (apiResponseValue as NetworkState.Success).data.overview?.statusCode
        )
    }

    @Test
    fun testFailedGetRequest_500() {
        val repo = FakeFailureGetRequestHomeRepository()
        setup(repo)
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/GetFailed?id=99"

        viewModel.onEvent(HomeUiEvents.UrlEntered(url))
        viewModel.onEvent(HomeUiEvents.MethodSelected(Methods.GET))
        viewModel.onEvent(
            HomeUiEvents.AddRequestHeader(
                RequestHeader(
                    "x-api-key",
                    "PMAK-66817683802075000135eedd-26618c34fb15708a38d52ca49c8f426d15"
                )
            )
        )
        runRequest()
        assertEquals(
            500,
            (viewModel.apiResponse.value as NetworkState.Success<Call>).data.overview?.statusCode
        )

    }


    @Test
    fun testSuccessfulPostRequest_200() {
        val repository = FakeSuccessPostJSONRequestRepository()
        setup(repository)
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/addUserTrue"
        val jsonBody = "{\"phone\":\"01010101010\",\"name\":\"New Ahmed\"}"

        viewModel.onEvent(HomeUiEvents.UrlEntered(url))
        viewModel.onEvent(HomeUiEvents.MethodSelected(Methods.POST))
        viewModel.onEvent(HomeUiEvents.RequestBodyTypeSelected(RequestBodyType.JSON))
        viewModel.onEvent(HomeUiEvents.AddJSONBody(jsonBody))
        runRequest()
        Assert.assertEquals(
            200,
            (viewModel.apiResponse.value as NetworkState.Success).data.overview?.statusCode
        )
    }

    @Test
    fun testFailurePostRequest_404() {
        val repository = FakeFailurePostJSONRequestRepository()
        setup(repository)
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/addUserTrue"
        val jsonBody = "{\"phone\":\"01010101010\",\"name\":\"New Ahmed\"}"

        viewModel.onEvent(HomeUiEvents.UrlEntered(url))
        viewModel.onEvent(HomeUiEvents.MethodSelected(Methods.POST))
        viewModel.onEvent(HomeUiEvents.RequestBodyTypeSelected(RequestBodyType.JSON))
        viewModel.onEvent(HomeUiEvents.AddJSONBody(jsonBody))
        runRequest()
        Assert.assertEquals(
            404,
            (viewModel.apiResponse.value as NetworkState.Success).data.overview?.statusCode
        )
    }

    @Test
    fun testFailurePostRequest_With_UnknownHostException() {
        val repository = FakeFailurePostJSONWithUnknownHostExceptionRequestRepository()
        setup(repository)
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/addUserTrue"
        val jsonBody = "{\"phone\":\"01010101010\",\"name\":\"New Ahmed\"}"

        viewModel.onEvent(HomeUiEvents.UrlEntered(url))
        viewModel.onEvent(HomeUiEvents.MethodSelected(Methods.POST))
        viewModel.onEvent(HomeUiEvents.RequestBodyTypeSelected(RequestBodyType.JSON))
        viewModel.onEvent(HomeUiEvents.AddJSONBody(jsonBody))
        runRequest()
        Assert.assertEquals(
            -1,
            (viewModel.apiResponse.value as NetworkState.Success).data.overview?.statusCode
        )
        Assert.assertEquals(
            "Could not Resolve Host",
            (viewModel.apiResponse.value as NetworkState.Success).data.overview?.status
        )
    }
}