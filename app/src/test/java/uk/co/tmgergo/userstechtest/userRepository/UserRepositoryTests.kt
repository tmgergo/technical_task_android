package uk.co.tmgergo.userstechtest.userRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserRepositoryTests {

    private lateinit var closableMocks: AutoCloseable
    private lateinit var repository: UserRepository
    @Mock private lateinit var mockUserDataSource: UserDataSource
    @Mock private lateinit var mockSuccessResponse: Response<Unit>
    @Mock private lateinit var mockFailureResponse: Response<Unit>

    @Before
    fun setup() {
        closableMocks = MockitoAnnotations.openMocks(this)
        whenever(mockSuccessResponse.isSuccessful).thenReturn(true)
        whenever(mockFailureResponse.isSuccessful).thenReturn(false)
    }

    @After
    fun teardown() {
        closableMocks.close()
    }

    @Test
    fun `should provide failure when querying users fails`() = runTest {
        `given querying users will fail`()
        `and the repository is created`()

        val result = `when users are queried`()

        `then failure is provided`(result)
    }

    @Test
    fun `should provide users when they are available`() = runTest {
        `given some users are available`(listOf(
            UserDTO(1, "John Doe", "john.doe@mail.com", "male", "inactive"),
            UserDTO(2, "Jane Doe", "jane.doe@mail.com", "female", "active"),
        ))
        `and the repository is created`()

        val users = `when users are queried`()

        `then the correct users are provided`(users, listOf(
            User(1, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE),
            User(2, "Jane Doe", "jane.doe@mail.com", Gender.FEMALE, UserStatus.ACTIVE)
        ))
    }

    @Test
    fun `should filter out users with unknown gender`() = runTest {
        `given some users are available`(listOf(
            UserDTO(1, "John Doe", "john.doe@mail.com", "unknown", "inactive"),
            UserDTO(2, "Jane Doe", "jane.doe@mail.com", "female", "active"),
        ))
        `and the repository is created`()

        val users = `when users are queried`()

        `then the correct users are provided`(users, listOf(
            User(2, "Jane Doe", "jane.doe@mail.com", Gender.FEMALE, UserStatus.ACTIVE)
        ))
    }

    @Test
    fun `should filter out users with unknown status`() = runTest {
        `given some users are available`(listOf(
            UserDTO(1, "John Doe", "john.doe@mail.com", "male", "unknown"),
            UserDTO(2, "Jane Doe", "jane.doe@mail.com", "female", "active"),
        ))
        `and the repository is created`()

        val users = `when users are queried`()

        `then the correct users are provided`(users, listOf(
            User(2, "Jane Doe", "jane.doe@mail.com", Gender.FEMALE, UserStatus.ACTIVE)
        ))
    }

    @Test
    fun `should provide success when user deletion is successful`() = runTest {
        `given user deletion will succeed`()
        `and the repository is created`()

        val result = `when a user is deleted`(User(1, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE))

        `then success is provided`(result)
    }

    @Test
    fun `should provide failure when user deletion is unsuccessful`() = runTest {
        `given user deletion will fail`()
        `and the repository is created`()

        val result = `when a user is deleted`(User(1, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE))

        `then failure is provided`(result)
    }

    @Test
    fun `should provide failure when trying to delete a user with no id`() = runTest {
        `given the repository is created`()

        val result = `when a user is deleted`(User(id = null, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE))

        `then failure is provided`(result)
    }

    @Test
    fun `should provide user when user addition is successful`() = runTest {
        `given user addition will succeed`(UserDTO(1, "John Doe", "john.doe@mail.com", "male", "inactive"))
        `and the repository is created`()

        val result = `when a user is added`(User(id = null, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE))

        `then the correct user is provided`(result, User(1, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE))
    }

    @Test
    fun `should provide failure when user addition is not successful`() = runTest {
        `given user addition will fail`()
        `and the repository is created`()

        val result = `when a user is added`(User(id = null, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE))

        `then failure is provided`(result)
    }

    private fun `then the correct user is provided`(result: Result<User>, expectedUser: User) {
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull(), `is`(expectedUser))
    }

    private suspend fun `when a user is added`(user: User) =
        repository.addUser(user)

    private suspend fun `given user addition will succeed`(user: UserDTO) {
        whenever(mockUserDataSource.addUser(any(), any(), any(), any())).thenReturn(user)
    }

    private suspend fun `given user addition will fail`() {
        whenever(mockUserDataSource.addUser(any(), any(), any(), any())).thenThrow(RuntimeException())
    }

    private fun `then success is provided`(result: Result<Any>) {
        assertThat(result.isSuccess, `is`(true))
    }

    private fun `then failure is provided`(result: Result<Any>) {
        assertThat(result.isSuccess, `is`(false))
    }

    private suspend fun `when a user is deleted`(user: User) =
        repository.deleteUser(user)


    private suspend fun `given some users are available`(users :List<UserDTO>) {
        whenever(mockUserDataSource.getUsers()).thenReturn(users)
    }

    private suspend fun `given querying users will fail`() {
        whenever(mockUserDataSource.getUsers()).thenThrow(RuntimeException())
    }

    private fun `and the repository is created`() {
        repository = UserRepositoryFactory.create(UnconfinedTestDispatcher(), mockUserDataSource)
    }

    private suspend fun `when users are queried`(): Result<List<User>> =
        repository.getUsers()

    private fun `then the correct users are provided`(actual: Result<List<User>>, expectedUsers: List<User>) {
        assertThat(actual.isSuccess, `is`(true))
        assertThat(actual.getOrNull()?.size, `is`(expectedUsers.size))
        assertThat(actual.getOrNull(), `is`(expectedUsers))
    }

    private fun `given the repository is created`() {
        `and the repository is created`()
    }

    private suspend fun `given user deletion will succeed`() {
        whenever(mockUserDataSource.deleteUser(any())).thenReturn(mockSuccessResponse)
    }

    private suspend fun `given user deletion will fail`() {
        whenever(mockUserDataSource.deleteUser(any())).thenReturn(mockFailureResponse)
    }

}