package uk.co.tmgergo.userstechtest.userRepository

data class UserDTO(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val status: String,
)

object ApiConstants {
    const val GENDER_MALE = "male"
    const val GENDER_FEMALE = "female"

    const val STATUS_ACTIVE = "active"
    const val STATUS_INACTIVE = "inactive"
}
