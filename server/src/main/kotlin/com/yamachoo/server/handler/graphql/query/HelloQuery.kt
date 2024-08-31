package com.yamachoo.server.handler.graphql.query

import jakarta.validation.constraints.Size
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated

@Controller
@Validated
class HelloQuery {
    @QueryMapping
    fun hello(
        @Argument
        @Size(min = 1, max = 10)
        name: String,
    ): String {
        return "Hello, $name!"
    }
}
