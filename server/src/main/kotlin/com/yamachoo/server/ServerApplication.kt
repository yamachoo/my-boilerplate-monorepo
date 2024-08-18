package com.yamachoo.server

import com.expediagroup.graphql.generator.GraphQLTypeResolver
import com.expediagroup.graphql.generator.SimpleTypeResolver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ServerApplication {
    @Bean
    fun typeResolver(): GraphQLTypeResolver = SimpleTypeResolver(mapOf())
}

fun main(vararg args: String) {
    runApplication<ServerApplication>(*args)
}
