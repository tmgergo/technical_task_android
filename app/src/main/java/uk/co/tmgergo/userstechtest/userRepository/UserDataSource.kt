package uk.co.tmgergo.userstechtest.userRepository

interface UserDataSource {
    suspend fun getUsers() : List<UserDTO>
}
