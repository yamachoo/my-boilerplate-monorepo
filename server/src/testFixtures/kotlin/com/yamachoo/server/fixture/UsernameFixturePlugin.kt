package com.yamachoo.server.fixture

import com.yamachoo.server.domain.user.Username
import io.github.serpro69.kfaker.Faker
import kotlin.reflect.KType

object UsernameFixturePlugin : ArbFixturePlugin {
    override fun supports(type: KType): Boolean {
        return type.classifier == Username::class
    }

    override fun generate(): Username {
        return Faker().name.name()
    }
}
