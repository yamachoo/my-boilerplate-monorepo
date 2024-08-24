package com.yamachoo.server.handler.graphql.type

import com.expediagroup.graphql.generator.scalars.ID
import java.time.OffsetDateTime
import com.yamachoo.server.domain.user.User as DomainUser

data class User(
    val id: ID,
    val username: String,
    val email: String,
    val createdAt: OffsetDateTime,
) {
    companion object {
        operator fun invoke(user: DomainUser): User {
            return User(
                id = ID(user.id.toString()),
                username = user.username,
                email = user.email.value,
                createdAt = user.createdAt,
            )
        }
    }
}
