package com.yamachoo.server.handler.graphql.config

import graphql.scalars.ExtendedScalars
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQLConfig {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder ->
            wiringBuilder.apply {
                scalar(ExtendedScalars.DateTime)
            }
        }
    }
}
