package com.yamachoo.server.fixture

import com.yamachoo.server.domain.user.Username
import io.github.serpro69.kfaker.Faker
import kotlin.reflect.KType

object UsernameFixturePlugin : ArbFixturePlugin {
    override fun supports(type: KType, name: String?): Boolean {
        return type.classifier == Username::class && name == "username"
    }

    override fun generate(type: KType, name: String?): Any {
        return Faker().name.name()
    }
}
