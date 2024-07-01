import android.content.ContentResolver
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.instatest.core.enums.Methods
import com.example.instatest.core.enums.RequestBodyType
import com.example.instatest.data.FakeFailureGetRequestHomeRepository
import com.example.instatest.data.FakeSuccessPostJSONRequestRepository
import com.example.instatest.data.FakeSuccessfulGetRequestHomeRepository
import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.model.Overview
import com.example.instatest.data.network.model.Request
import com.example.instatest.data.network.model.Response
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.domain.remote.GetRequestUseCase
import com.example.instatest.domain.remote.PostJsonRequestUseCase
import com.example.instatest.domain.remote.PostMultipartRequestUseCase
import com.example.instatest.domain.FakeGetRequestUseCase
import com.example.instatest.domain.FakePostJsonRequestUseCase
import com.example.instatest.domain.FakePostMultipartRequestUseCase
import com.example.instatest.domain.remote.GetRequestUseCaseImpl
import com.example.instatest.domain.remote.PostJsonRequestUseCaseImpl
import com.example.instatest.ui.screens.homeScreen.HomeViewModel
import com.example.instatest.ui.screens.homeScreen.states.HomeUiEvents
import com.example.instatest.ui.screens.homeScreen.states.NetworkState
import com.example.instatest.ui.screens.homeScreen.states.RequestHeader
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
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
    fun testSuccessfulGetRequest() {
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
    fun testFailedGetRequest() {
        val repo = FakeFailureGetRequestHomeRepository()
        setup(repo)
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/GetFailed?id=99"
        /*  val mockCall = Call(
              overview = Overview(URL(url), Methods.GET.name, "Server Error", 500),
              request = Request(headers),
              response = Response(body = """{"success":"false","msg":"something went wrong!"}""")
          )*/

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
    fun testSuccessfulPostRequest() {
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
}