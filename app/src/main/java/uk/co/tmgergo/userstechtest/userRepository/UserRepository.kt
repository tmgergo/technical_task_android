package uk.co.tmgergo.userstechtest.userRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface UserRepository {
    suspend fun getUsers() : Result<List<User>>
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
