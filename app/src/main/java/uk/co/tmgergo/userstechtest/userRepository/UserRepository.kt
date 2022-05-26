package uk.co.tmgergo.userstechtest.userRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface UserRepository {
    suspend fun getUsers() : Result<List<User>>
    suspend fun addUser(user: User) : Result<User>
    suspend fun deleteUser(user: User) : Result<Unit>
}

class UserRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val dataSource: UserDataSource,
    private val dtoMapper: ((UserDTO) -> User?),
    private val domainMapper: ((User) -> UserDTO),
) : UserRepository, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatcher

    override suspend fun getUsers() : Result<List<User>> {
        return withContext(coroutineContext) {
            runCatching {
                dataSource.getUsers()
            }.fold(
                { list -> Result.success(list.mapNotNull { user -> dtoMapper.invoke(user) }) },
                { Result.failure(it) }
            )
        }
    }

    override suspend fun addUser(user: User) : Result<User> {
        return withContext(coroutineContext) {
            runCatching {
                with(domainMapper.invoke(user)) {
                    dataSource.addUser(name, email, gender, status)
                }
            }.fold(
                {
                    dtoMapper.invoke(it)?.let { added ->
                        Result.success(added)
                    } ?: run {
                        Result.failure(Throwable())
                    }
                },
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
            User::toDtoUser,
        )
    }
}
