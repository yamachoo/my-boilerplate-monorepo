package com.yamachoo.server.handler.graphql.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.yamachoo.server.domain.user.DraftUser
import com.yamachoo.server.handler.graphql.type.User
import com.yamachoo.server.usecase.user.RegisterUserUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
class RegisterUserMutation(
    private val registerUserUseCase: RegisterUserUseCase,
) : Mutation {
    suspend fun registerUser(
        @Valid
        input: RegisterUserInput,
    ): User {
        val user = registerUserUseCase.call(
            DraftUser(
                name = input.name,
                email = input.email,
            )
        )

        return User(user)
    }
}

data class RegisterUserInput(
    @field:Size(min = 1, max = 10)
    val name: String,
    @field:Email
    val email: String,
)
