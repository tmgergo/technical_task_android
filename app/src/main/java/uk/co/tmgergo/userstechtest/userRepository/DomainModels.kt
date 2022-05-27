package uk.co.tmgergo.userstechtest.userRepository

data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val gender: Gender,
    val status: UserStatus,
)

enum class Gender {
    FEMALE,
    MALE,
}

enum class UserStatus {
    ACTIVE,
    INACTIVE,
}
