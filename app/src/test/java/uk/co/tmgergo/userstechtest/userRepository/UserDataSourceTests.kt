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
import retrofit2.Response
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
        `and the data source is created`("ACCESS_TOKEN")

        val users = `when users data is fetched`()

        `then the authorization header is set`("ACCESS_TOKEN")
        `and the correct users are provided`(users, listOf(
            UserDTO(1111, "John Doe", "john.doe@mail.com", "male", "active"),
            UserDTO(1112, "Jane Doe", "jane.doe@mail.com", "female", "inactive")
        ))
    }

    @Test(expected = com.google.gson.stream.MalformedJsonException::class)
    fun `should throw error for malformed fetch response json`() = runTest {
        `given data fetch will succeed`(200, "users_malformed.json")
        `and the data source is created`("ACCESS_TOKEN")

        `when users data is fetched`()

        // then the expected exception is thrown
    }

    @Test(expected = HttpException::class)
    fun `should throw error for failed fetch response`() = runTest {
        `given data fetch will fail`(500)
        `and the data source is created`("ACCESS_TOKEN")

        `when users data is fetched`()

        // then the expected exception is thrown
    }

    @Test
    fun `should return failed response when user deletion fails with 404`() = runTest {
        `given user deletion will fail`(404)
        `and the data source is created`("ACCESS_TOKEN")

        val response = `when a user is deleted`()

        `then should return a failed response`(response, 404)
    }

    @Test
    fun `should return success response when user deletion is successful`() = runTest {
        `given user deletion will succeed`()
        `and the data source is created`("ACCESS_TOKEN")

        val response = `when a user is deleted`()

        `then should return a successful response`(response, 204)
    }

    @Test
    fun `should return user response when user addition is successful`() = runTest {
        `given user addition will succeed`(201, "add_user_success.json")
        `and the data source is created`("ACCESS_TOKEN")

        val user = `when a user is added`()

        `then the user is returned`(user)
    }

    @Test(expected = HttpException::class)
    fun `should throw error for failed user addition`() = runTest {
        `given user addition will fail`(422)
        `and the data source is created`("ACCESS_TOKEN")

        `when a user is added`()

        // then the expected exception is thrown
    }

    private fun `then the user is returned`(user: UserDTO) {
        assertThat(user, `is`(UserDTO(1111, "John Doe", "john.doe@mail.com", "male", "active")))
    }

    private suspend fun `when a user is added`() =
        userDataSource.addUser("John Doe", "john.doe@mail.com", "male", "active")

    private fun `then should return a successful response`(response: Response<Unit>, expectedStatusCode: Int) {
        assertThat(response.isSuccessful, `is`(true))
        assertThat(response.code(), `is`(expectedStatusCode))
    }

    private fun `given user deletion will succeed`() {
        mockWebServer.enqueueResponse(204)
    }

    private fun `and the data source is created`(accessToken: String) {
        userDataSource = UserDataSourceFactory.create(mockWebServer.url("/"), client, accessToken)
    }

    private fun `given data fetch will succeed`(statusCode: Int, responseResource: String) {
        mockWebServer.enqueueResponseWithResourceBody(statusCode, responseResource)
    }

    private fun `given user addition will succeed`(statusCode: Int, responseResource: String) {
        mockWebServer.enqueueResponseWithResourceBody(statusCode, responseResource)
    }

    private suspend fun `when users data is fetched`(): List<UserDTO> {
        return userDataSource.getUsers()
    }

    private fun `and the correct users are provided`(actual: List<UserDTO>, expected: List<UserDTO>) {
        assertThat(actual, `is`(expected))
    }

    private fun `given data fetch will fail`(statusCode: Int) {
        mockWebServer.enqueueResponse(statusCode)
    }

    private fun `given user addition will fail`(statusCode: Int) {
        mockWebServer.enqueueResponse(statusCode)
    }

    private fun `then the authorization header is set`(accessToken: String) {
        assertThat(mockWebServer.takeRequest().getHeader("Authorization"), `is`("Bearer $accessToken"))
    }

    private fun `given user deletion will fail`(statusCode: Int) {
        mockWebServer.enqueueResponse(statusCode)
    }

    private suspend fun `when a user is deleted`() =
        userDataSource.deleteUser(1111)

    private fun `then should return a failed response`(response: Response<Unit>, expectedStatusCode: Int) {
        assertThat(response.isSuccessful, `is`(false))
        assertThat(response.code(), `is`(expectedStatusCode))
    }


}
