package com.yamachoo.server.domain.user

import com.yamachoo.server.domain.shared.values.Email
import java.time.OffsetDateTime

data class DraftUser(
    val username: Username,
    val email: Email,
) {
    val id: UInt = UInt.MIN_VALUE
    val createdAt: OffsetDateTime = OffsetDateTime.MIN
}
