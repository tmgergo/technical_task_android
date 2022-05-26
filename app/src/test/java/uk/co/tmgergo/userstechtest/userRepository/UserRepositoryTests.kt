package uk.co.tmgergo.userstechtest.userRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserRepositoryTests {

    private lateinit var closableMocks: AutoCloseable
    private lateinit var repository: UserRepository
    @Mock private lateinit var mockUserDataSource: UserDataSource

    @Before
    fun setup() {
        closableMocks = MockitoAnnotations.openMocks(this)
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
        assertThat(actual.isSuccess, CoreMatchers.`is`(true))
        assertThat(actual.getOrNull()?.size, CoreMatchers.`is`(expectedUsers.size))
        assertThat(actual.getOrNull(), CoreMatchers.`is`(expectedUsers))
    }

    private fun `then failure is provided`(result: Result<List<User>>) {
        assertThat(result.isSuccess, CoreMatchers.`is`(false))
    }

}