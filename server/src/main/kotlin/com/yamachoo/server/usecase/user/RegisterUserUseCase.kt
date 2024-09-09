package com.yamachoo.server.usecase.user

import com.yamachoo.server.domain.user.DraftUser
import com.yamachoo.server.domain.user.User
import com.yamachoo.server.domain.user.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterUserUseCase(
    private val userRepository: UserRepository,
) {
    private val logging = KotlinLogging.logger {}

    @Transactional
    suspend fun call(user: DraftUser): User {
        val registeredUser = userRepository.create(user)

        logging.info { "User registered: $registeredUser" }

        return registeredUser
    }
}
