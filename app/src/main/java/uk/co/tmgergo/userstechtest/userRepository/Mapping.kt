package uk.co.tmgergo.userstechtest.userRepository

import uk.co.tmgergo.userstechtest.userRepository.ApiConstants.GENDER_FEMALE
import uk.co.tmgergo.userstechtest.userRepository.ApiConstants.GENDER_MALE
import uk.co.tmgergo.userstechtest.userRepository.ApiConstants.STATUS_ACTIVE
import uk.co.tmgergo.userstechtest.userRepository.ApiConstants.STATUS_INACTIVE

fun UserDTO.toUser() : User? =
    gender.toGender()?.let { gender ->
        status.toUserStatus()?.let { userStatus ->
            User(
                id = id,
                name = name,
                email = email,
                gender = gender,
                status = userStatus
            )
        }
    }

fun String.toGender() : Gender? =
    when(this) {
        GENDER_MALE -> Gender.MALE
        GENDER_FEMALE -> Gender.FEMALE
        else -> null
    }

fun String.toUserStatus() : UserStatus? =
    when(this) {
        STATUS_ACTIVE -> UserStatus.ACTIVE
        STATUS_INACTIVE -> UserStatus.INACTIVE
        else -> null
    }
