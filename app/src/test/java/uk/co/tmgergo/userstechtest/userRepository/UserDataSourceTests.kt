package uk.co.tmgergo.userstechtest.userRepository


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import uk.co.tmgergo.userstechtest.utils.enqueueResponse
import uk.co.tmgergo.userstechtest.utils.enqueueResponseWithResourceBody
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class UserDataSourceTests {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var userDataSource: UserDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should provide users when fetch is successful`() = runTest {
        `given data fetch will succeed`(200, "users_success.json")
        `and the data source is created`()

        val users = `when users data is fetched`()

        `then the correct users are provided`(users, listOf(
            UserDTO(1111, "John Doe", "john.doe@mail.com", "male", "active"),
            UserDTO(1112, "Jane Doe", "jane.doe@mail.com", "female", "inactive")
        ))
    }

    @Test(expected = com.google.gson.stream.MalformedJsonException::class)
    fun `should throw error for malformed fetch response json`() = runTest {
        `given data fetch will succeed`(200, "users_malformed.json")
        `and the data source is created`()

        `when users data is fetched`()

        // then the expected exception is thrown
    }

    @Test(expected = HttpException::class)
    fun `should throw error for failed fetch response`() = runTest {
        `given data fetch will fail`(500)
        `and the data source is created`()

        `when users data is fetched`()

        // then the expected exception is thrown
    }

    private fun `and the data source is created`() {
        userDataSource = UserDataSourceFactory.create(mockWebServer.url("/"), client)
    }

    private fun `given data fetch will succeed`(statusCode: Int, responseResource: String) {
        mockWebServer.enqueueResponseWithResourceBody(statusCode, responseResource)
    }

    private suspend fun `when users data is fetched`(): List<UserDTO> {
        return userDataSource.getUsers()
    }

    private fun `then the correct users are provided`(actual: List<UserDTO>, expected: List<UserDTO>) {
        assertThat(actual, `is`(expected))
    }

    private fun `given data fetch will fail`(statusCode: Int) {
        mockWebServer.enqueueResponse(statusCode)
    }

}
