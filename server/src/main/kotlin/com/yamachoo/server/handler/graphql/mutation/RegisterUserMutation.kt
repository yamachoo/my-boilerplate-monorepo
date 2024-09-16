package com.yamachoo.server.handler.graphql.mutation

import com.yamachoo.server.handler.graphql.type.RegisterUserInput
import com.yamachoo.server.handler.graphql.type.User
import com.yamachoo.server.handler.graphql.type.from
import com.yamachoo.server.usecase.user.RegisterUserUseCase
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import com.yamachoo.server.domain.shared.values.Email as DomainEmail
import com.yamachoo.server.domain.user.DraftUser as DomainDraftUser

@Controller
class RegisterUserMutation(
    private val registerUserUseCase: RegisterUserUseCase,
) {
    @MutationMapping
    suspend fun registerUser(
        @Argument
        input: RegisterUserInput,
    ): User {
        val user = registerUserUseCase.call(
            DomainDraftUser(
                username = input.username,
                email = DomainEmail(input.email),
            ),
        )

        return User.from(user)
    }
}
