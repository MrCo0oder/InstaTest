import android.content.ContentResolver
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.instatest.core.enums.Methods
import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.model.Overview
import com.example.instatest.data.network.model.Request
import com.example.instatest.data.network.model.Response
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.domain.remote.GetRequestUseCase
import com.example.instatest.domain.remote.PostJsonRequestUseCase
import com.example.instatest.domain.remote.PostMultipartRequestUseCase
import com.example.instatest.ui.screens.homeScreen.HomeViewModel
import com.example.instatest.ui.screens.homeScreen.states.HomeUiEvents
import com.example.instatest.ui.screens.homeScreen.states.NetworkState
import com.example.instatest.ui.screens.homeScreen.states.RequestHeader
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.File
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        val repo = FakeHomeRepository()

        viewModel = HomeViewModel(
            getRequestUseCaseImpl = FakeGetRequestUseCase(repo),
            postJsonRequestUseCaseImpl = FakePostJsonRequestUseCase(repo),
            postMultipartRequestUseCaseImpl = FakePostMultipartRequestUseCase(repo),
            contentResolver = mock(ContentResolver::class.java)
        )
    }

    @Test
    fun testSuccessfulGetRequest() {
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/Getsuccess?id=99"
        val headers =
            mapOf("x-api-key" to "PMAK-66817683802075000135eedd-26618c34fb15708a38d52ca49c8f426d15")

        val mockCall = Call(
            overview = Overview(URL(url), Methods.GET.name, "Server Error", 500),
            request = Request(headers),
            response = Response(body = """{"success":"false","msg":"something went wrong!"}""")
        )

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
        val apiResponseValue = viewModel.apiResponse.value


        Assert.assertEquals(
            200,
            (apiResponseValue as NetworkState.Success).data.overview?.statusCode
        )


    }

    @Test
    fun testFailedGetRequest() {
        val url = "https://fab51db5-7ad2-43c4-a43a-9cdbb80f2079.mock.pstmn.io/GetFailed?id=99"
        val headers =
            mapOf("x-api-key" to "PMAK-66817683802075000135eedd-26618c34fb15708a38d52ca49c8f426d15")

        val mockCall = Call(
            overview = Overview(URL(url), Methods.GET.name, "Server Error", 500),
            request = Request(headers),
            response = Response(body = """{"success":"false","msg":"something went wrong!"}""")
        )


        val latch = CountDownLatch(1)

        // Observe the LiveData
        val observer = Observer<NetworkState<Call>> {
            latch.countDown() // Decrement the latch count when data is received
        }
        viewModel.apiResponse.observeForever(observer)

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
        viewModel.hitTheApi()

        latch.await(1, TimeUnit.SECONDS)

        if (viewModel.apiResponse.value is NetworkState.Success<Call>) {
            assertEquals(
                500,
                (viewModel.apiResponse.value as NetworkState.Success<Call>).data.overview?.statusCode
            )
        }
    }

}


class FakeHomeRepository : HomeRepository {
    override fun getRequest(url: String, headers: Map<String, String>): Call {
        Thread.sleep(1000) // Simulate network delay
        return Call(
            overview = Overview(URL(url), Methods.GET.name, "", 200),
            request = Request(headers),
            response = Response(body = """{"success":"true","msg":"Welcome Ahmed"}""")
        )
    }

    override fun postJsonRequest(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): Call {
        Thread.sleep(1000) // Simulate network delay
        return Call(
            overview = Overview(URL(url), Methods.POST.name, "OK", 0),
            request = Request(headers),
            response = Response(body = """{"success":"true","msg":"Welcome Ahmed"}""")
        )
    }

    override fun postMultipartRequest(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): Call {
        return Call(
            overview = Overview(URL(url), Methods.POST.name, "OK", 200),
            request = Request(headers),
            response = Response(body = """{"success":"true","msg":"Welcome Ahmed"}""")
        )
    }
}

class FakeGetRequestUseCase(private val fakeHomeRepository: FakeHomeRepository) :
    GetRequestUseCase {
    override operator fun invoke(url: String, headers: Map<String, String>): NetworkState<Call> {
        return NetworkState.Success(fakeHomeRepository.getRequest(url, headers))
    }
}

class FakePostJsonRequestUseCase(private val fakeHomeRepository: FakeHomeRepository) :
    PostJsonRequestUseCase {
    override fun invoke(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): NetworkState<Call> {
        return NetworkState.Success(fakeHomeRepository.postJsonRequest(url, headers, jsonBody))
    }
}

class FakePostMultipartRequestUseCase(private val fakeHomeRepository: FakeHomeRepository) :
    PostMultipartRequestUseCase {
    override fun invoke(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): NetworkState.Success<Call> {
        return NetworkState.Success(
            fakeHomeRepository.postMultipartRequest(
                url,
                headers,
                formKey,
                file,
                mimeType,
                fileContent
            )
        )
    }
}