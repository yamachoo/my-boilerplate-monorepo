package com.yamachoo.server.graphql

import com.expediagroup.graphql.server.operations.Query
import jakarta.validation.constraints.Size
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
class HelloQuery : Query {
    fun hello(
        @Size(min = 1, max = 10)
        name: String? = "World",
    ): String {
        return "Hello, $name!"
    }
}
