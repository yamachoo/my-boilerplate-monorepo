package com.yamachoo.server.handler.graphql.query

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import jakarta.validation.ConstraintViolationException
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HelloQuerySpec(
    private val helloQuery: HelloQuery,
) : DescribeSpec({
    describe("hello") {
        it("should return Hello, World!") {
            helloQuery.hello() shouldBe "Hello, World!"
        }

        it("should return Hello, Yamachoo!") {
            helloQuery.hello("Yamachoo") shouldBe "Hello, Yamachoo!"
        }

        val validationErrorTable = table(
            headers("name", "reason"),
            row("", "empty string"),
            row(Arb.string(11..20).next(), "too long string"),
        )

        forAll(validationErrorTable) { name, reason ->
            it("should throw validation error for $reason") {
                shouldThrow<ConstraintViolationException> {
                    helloQuery.hello(name)
                }
            }
        }
    }
})
