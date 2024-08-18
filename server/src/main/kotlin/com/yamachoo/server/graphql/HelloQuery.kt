package com.yamachoo.server.graphql

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class HelloQuery : Query {
    fun hello(name: String? = "World"): String {
        return "Hello, $name!"
    }
}
