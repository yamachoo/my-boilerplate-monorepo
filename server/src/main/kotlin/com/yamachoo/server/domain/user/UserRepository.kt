package com.yamachoo.server.domain.user

interface UserRepository {
    suspend fun create(user: DraftUser): User
}
