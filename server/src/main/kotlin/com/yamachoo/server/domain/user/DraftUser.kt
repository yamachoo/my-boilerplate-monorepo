package com.yamachoo.server.domain.user

import java.time.OffsetDateTime

data class DraftUser(
    val name: String,
    val email: String,
) {
    val id: Long = 0
    val createdAt: OffsetDateTime = OffsetDateTime.MIN
}
