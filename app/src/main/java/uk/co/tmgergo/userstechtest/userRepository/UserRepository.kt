package uk.co.tmgergo.userstechtest.userRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface UserRepository {
    suspend fun getUsers() : Result<List<User>>
    suspend fun deleteUser(user: User) : Result<Unit>
}

class UserRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val dataSource: UserDataSource,
    private val mapper: ((UserDTO) -> User?),
) : UserRepository, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatcher

    override suspend fun getUsers() : Result<List<User>> {
        return withContext(coroutineContext) {
            runCatching {
                dataSource.getUsers()
            }.fold(
                { list -> Result.success(list.mapNotNull { item -> mapper.invoke(item) }) },
                { Result.failure(it) }
            )
        }
    }

    override suspend fun deleteUser(user: User) : Result<Unit> {
        return withContext(coroutineContext) {
            runCatching {
                user.id?.let { id ->
                    dataSource.deleteUser(id)
                }
            }.fold(
                { response -> if (response?.isSuccessful == true) Result.success(Unit) else Result.failure(Throwable()) },
                { Result.failure(it) }
            )
        }
    }
}

object UserRepositoryFactory {
    fun create(dispatcher: CoroutineDispatcher, dataSource: UserDataSource,): UserRepository {
        return UserRepositoryImpl(
            dispatcher,
            dataSource,
            UserDTO::toUser,
        )
    }
}
