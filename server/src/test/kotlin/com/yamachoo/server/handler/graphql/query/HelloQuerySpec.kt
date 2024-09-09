package com.yamachoo.server.handler.graphql.query

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester

@SpringBootTest
@AutoConfigureGraphQlTester
class HelloQuerySpec(
    private val graphQlTester: GraphQlTester,
) : DescribeSpec({
    describe("hello") {
        it("should return Hello, World!") {
            val query = """
                query {
                    hello
                }
            """.trimIndent()
            graphQlTester.document(query)
                .execute()
                .path("data") { data ->
                    data.path("hello").entity(String::class.java).isEqualTo("Hello, World!")
                }
        }

        it("should return Hello, Yamachoo!") {
            graphQlTester.documentName("helloQuery")
                .variable("name", "Yamachoo")
                .execute()
                .path("data") { data ->
                    data.path("hello").entity(String::class.java).isEqualTo("Hello, Yamachoo!")
                }
        }

        val validationErrorTable = table(
            headers("name", "reason"),
            row("", "empty string"),
            row(Arb.string(11..20).next(), "too long string"),
        )

        forAll(validationErrorTable) { name, reason ->
            it("should throw validation error for $reason") {
                graphQlTester.documentName("helloQuery")
                    .variable("name", name)
                    .execute()
                    .errors()
                    .expect { error ->
                        error.message.equals("/hello/name size must be between 1 and 10")
                    }
                    .verify()
            }
        }
    }
})
