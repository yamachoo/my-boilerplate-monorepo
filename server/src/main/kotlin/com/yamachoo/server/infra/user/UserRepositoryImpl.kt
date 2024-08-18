package com.yamachoo.server.infra.user

import com.yamachoo.server.domain.user.DraftUser
import com.yamachoo.server.domain.user.User
import com.yamachoo.server.domain.user.UserRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class UserRepositoryImpl : UserRepository {
    override suspend fun create(user: DraftUser): User {
        return User(
            id = 1,
            name = user.name,
            email = user.email,
            createdAt = OffsetDateTime.now(),
        )
    }
}
