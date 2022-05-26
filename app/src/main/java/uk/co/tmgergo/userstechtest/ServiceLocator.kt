package uk.co.tmgergo.userstechtest

import uk.co.tmgergo.userstechtest.userRepository.UserRepository
import uk.co.tmgergo.userstechtest.utils.TextProvider

class ServiceLocator(
    val userRepository: UserRepository,
    val textProvider: TextProvider,
)
