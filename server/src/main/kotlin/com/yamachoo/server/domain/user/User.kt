package com.yamachoo.server.domain.user

import java.time.OffsetDateTime

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: OffsetDateTime,
)
