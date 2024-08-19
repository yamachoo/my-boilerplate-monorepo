package com.yamachoo.server.usecase.user

import com.yamachoo.server.domain.user.DraftUser
import com.yamachoo.server.domain.user.User
import com.yamachoo.server.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterUserUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional
    suspend fun call(user: DraftUser): User {
        return userRepository.create(user)
    }
}
