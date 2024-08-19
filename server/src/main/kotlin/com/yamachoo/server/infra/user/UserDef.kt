package com.yamachoo.server.infra.user

import com.yamachoo.server.domain.user.User
import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntityDef(User::class)
@KomapperTable(name = "users", alwaysQuote = true)
data class UserDef(
    @KomapperId
    @KomapperAutoIncrement
    val id: Nothing,
    @KomapperCreatedAt
    val createdAt: Nothing,
)
