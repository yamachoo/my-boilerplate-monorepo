package com.yamachoo.server.handler.graphql.mutation

import com.yamachoo.server.domain.user.DraftUser
import com.yamachoo.server.domain.user.User
import com.yamachoo.server.extension.DbSetupBuilderExtension.insert
import com.yamachoo.server.extension.DbSetupExtension.prepareDatabase
import com.yamachoo.server.fixture.ArbFixture.fixture
import com.yamachoo.server.handler.graphql.type.RegisterUserInput
import com.yamachoo.server.infra.config.DatabaseTestConfiguration
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.komapper.core.UniqueConstraintException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import javax.sql.DataSource

@SpringBootTest
@Import(DatabaseTestConfiguration::class)
class RegisterUserMutationSpec(
    private val registerUserMutation: RegisterUserMutation,
    private val dataSource: DataSource,
) : DescribeSpec({
    beforeSpec {
        Fixture.call(dataSource)
    }

    describe("registerUser") {
        it("should register user") {
            val draftUser = fixture<DraftUser>()
            val input = RegisterUserInput(
                username = draftUser.username,
                email = draftUser.email.value,
            )
            val user = registerUserMutation.registerUser(input)

            user.username shouldBe input.username
            user.email shouldBe input.email
        }

        it("should throw unique constraint error when email is already registered") {
            val input = RegisterUserInput(
                username = TestData.registeredUser.username,
                email = TestData.registeredUser.email.value,
            )

            shouldThrow<UniqueConstraintException> {
                registerUserMutation.registerUser(input)
            }
        }
    }
}) {
    private object TestData {
        val registeredUser = fixture<User>()
    }

    private object Fixture {
        fun call(dataSource: DataSource) {
            prepareDatabase(dataSource) {
                insert(listOf(TestData.registeredUser))
            }
        }
    }
}
