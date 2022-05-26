package uk.co.tmgergo.userstechtest.userList

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import uk.co.tmgergo.userstechtest.userRepository.Gender
import uk.co.tmgergo.userstechtest.userRepository.User
import uk.co.tmgergo.userstechtest.userRepository.UserRepository
import uk.co.tmgergo.userstechtest.userRepository.UserStatus
import uk.co.tmgergo.userstechtest.utils.TextProvider

@ExperimentalCoroutinesApi
class UserListPresentationTests {

    private lateinit var viewModel: UserListViewModel
    @Mock private lateinit var mockView: UserListView
    @Mock private lateinit var mockUserRepository: UserRepository
    @Mock private lateinit var mockTextProvider: TextProvider
    private val errorMessage = "Oops!"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        whenever(mockTextProvider.getGenericErrorMessage()).thenReturn(errorMessage)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should show loading and present users when the repo provides them`() = runTest {
        `given the repository provides users`(listOf(
            User(1, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE),
            User(2, "Jane Doe", "jane.doe@mail.com", Gender.FEMALE, UserStatus.ACTIVE),
        ))
        `and the view model is created`()

        `when the view is ready to present data`()

        `then a loading indicator is shown`()
        `and the correct users are presented`(listOf(
            User(1, "John Doe", "john.doe@mail.com", Gender.MALE, UserStatus.INACTIVE),
            User(2, "Jane Doe", "jane.doe@mail.com", Gender.FEMALE, UserStatus.ACTIVE),
        ))
    }

    @Test
    fun `should show loading and an error when the repo cannot provide users`() = runTest {
        `given the repository cannot provide users`()
        `and the view model is created`()

        `when the view is ready to present data`()

        `then a loading indicator is shown`()
        `and an error message is presented`(errorMessage)
    }

    private suspend fun `given the repository cannot provide users`() {
        whenever(mockUserRepository.getUsers()).thenReturn(Result.failure(Throwable()))
    }

    private suspend fun `given the repository provides users`(users: List<User>) {
        whenever(mockUserRepository.getUsers()).thenReturn(Result.success(users))
    }

    private fun `and the view model is created`() {
        viewModel = UserListViewModel(mockUserRepository, mockTextProvider)
    }

    private fun `when the view is ready to present data`() {
        UserListViewController(mockView, viewModel)
    }

    private fun `then a loading indicator is shown`() {
        verify(mockView).displayLoadingIndicator()
    }

    private fun `and the correct users are presented`(users: List<User>) {
        verify(mockView).displayUsers(users)
    }

    private fun `and an error message is presented`(message: String) {
        verify(mockView).displayError(message)
    }
}