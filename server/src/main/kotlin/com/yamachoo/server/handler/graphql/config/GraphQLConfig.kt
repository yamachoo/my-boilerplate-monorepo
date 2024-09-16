package com.yamachoo.server.handler.graphql.config

import com.yamachoo.server.handler.graphql.scalar.EmailScalar
import graphql.scalars.ExtendedScalars
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQLConfig {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        val validationRules = ValidationRules.newValidationRules()
            .build()
        val schemaWiring = ValidationSchemaWiring(validationRules)

        return RuntimeWiringConfigurer { wiringBuilder ->
            wiringBuilder.apply {
                directiveWiring(schemaWiring)
                scalar(ExtendedScalars.DateTime)
                scalar(EmailScalar.EMAIL)
            }
        }
    }
}
