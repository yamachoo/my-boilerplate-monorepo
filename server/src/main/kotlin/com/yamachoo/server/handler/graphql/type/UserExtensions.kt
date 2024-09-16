package com.yamachoo.server.handler.graphql.type

import com.yamachoo.server.domain.user.User as DomainUser

fun User.Companion.from(user: DomainUser): User {
    return User(
        id = user.id.toString(),
        username = user.username,
        email = user.email.value,
        createdAt = user.createdAt,
    )
}
