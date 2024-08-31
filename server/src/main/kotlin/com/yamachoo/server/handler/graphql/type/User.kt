package com.yamachoo.server.handler.graphql.type

import java.time.OffsetDateTime
import com.yamachoo.server.domain.user.User as DomainUser

data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: OffsetDateTime,
) {
    companion object {
        operator fun invoke(user: DomainUser): User {
            return User(
                id = user.id.toString(),
                username = user.username,
                email = user.email.value,
                createdAt = user.createdAt,
            )
        }
    }
}
