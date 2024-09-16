package com.yamachoo.server.handler.graphql.scalar

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.util.Locale
import java.util.regex.Pattern

object EmailScalar {
    val EMAIL: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Email")
        .description("A custom scalar that handles emails")
        .coercing(object : Coercing<Any, Any> {
            override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): Any {
                return serializeEmail(dataFetcherResult)
            }

            override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): Any {
                return parseEmailFromVariable(input)
            }

            override fun parseLiteral(
                input: Value<*>,
                variables: CoercedVariables,
                graphQLContext: GraphQLContext,
                locale: Locale,
            ): Any {
                return parseEmailFromAstLiteral(input)
            }
        })
        .build()

    private fun looksLikeAnEmailAddress(possibleEmailValue: String): Boolean {
        return Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", possibleEmailValue)
    }

    private fun serializeEmail(dataFetcherResult: Any): String {
        val possibleEmailValue = dataFetcherResult.toString()
        if (looksLikeAnEmailAddress(possibleEmailValue)) {
            return possibleEmailValue
        } else {
            throw CoercingSerializeException("Unable to serialize $possibleEmailValue as an email address")
        }
    }

    private fun parseEmailFromVariable(input: Any): String {
        if (input is String) {
            val possibleEmailValue = input.toString()
            if (looksLikeAnEmailAddress(possibleEmailValue)) {
                return possibleEmailValue
            }
        }
        throw CoercingParseValueException("Unable to parse variable value $input as an email address")
    }

    private fun parseEmailFromAstLiteral(input: Value<*>): String {
        if (input is StringValue) {
            val possibleEmailValue: String = input.value
            if (looksLikeAnEmailAddress(possibleEmailValue)) {
                return possibleEmailValue
            }
        }
        throw CoercingParseLiteralException("Value is not any email address : '$input'")
    }
}
