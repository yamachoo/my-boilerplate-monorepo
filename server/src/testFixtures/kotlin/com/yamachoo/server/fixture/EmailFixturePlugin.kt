package com.yamachoo.server.fixture

import com.yamachoo.server.domain.shared.values.Email
import io.github.serpro69.kfaker.Faker
import kotlin.reflect.KType

object EmailFixturePlugin : ArbFixturePlugin {
    override fun supports(type: KType): Boolean {
        return type.classifier == Email::class
    }

    override fun generate(): Email {
        return Email(Faker().internet.email())
    }
}
