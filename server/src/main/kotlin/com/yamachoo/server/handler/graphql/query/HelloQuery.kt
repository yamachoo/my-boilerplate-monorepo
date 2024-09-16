package com.yamachoo.server.handler.graphql.query

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class HelloQuery {
    @QueryMapping
    fun hello(
        @Argument
        name: String,
    ): String {
        return "Hello, $name!"
    }
}
