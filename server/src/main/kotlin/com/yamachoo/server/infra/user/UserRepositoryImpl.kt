package com.yamachoo.server.infra.user

import com.yamachoo.server.domain.user.DraftUser
import com.yamachoo.server.domain.user.User
import com.yamachoo.server.domain.user.UserRepository
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val db: R2dbcDatabase,
) : UserRepository {
    override suspend fun create(user: DraftUser): User {
        return db.runQuery {
            val u = Meta.user
            QueryDsl.insert(u).single(User(user))
        }
    }
}
