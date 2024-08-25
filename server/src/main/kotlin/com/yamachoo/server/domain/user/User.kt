package com.yamachoo.server.domain.user

import com.yamachoo.server.domain.shared.values.Email
import java.time.OffsetDateTime

data class User(
    val id: UInt,
    val username: Username,
    val email: Email,
    val createdAt: OffsetDateTime,
) {
    companion object {
        operator fun invoke(user: DraftUser) = User(
            id = user.id,
            username = user.username,
            email = user.email,
            createdAt = user.createdAt,
        )
    }
}
