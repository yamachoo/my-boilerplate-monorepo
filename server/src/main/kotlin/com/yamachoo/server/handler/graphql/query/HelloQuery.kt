package com.yamachoo.server.handler.graphql.query

import dev.openfeature.sdk.OpenFeatureAPI
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class HelloQuery(private val openFeatureAPI: OpenFeatureAPI) {
    @QueryMapping
    fun hello(
        @Argument
        name: String,
    ): String {
        val client = openFeatureAPI.client

        if (client.getBooleanValue("welcome-message", false)) {
            return "Hello, $name. Welcome to this OpenFeature-enabled API!"
        }

        return "Hello, $name!"
    }
}
