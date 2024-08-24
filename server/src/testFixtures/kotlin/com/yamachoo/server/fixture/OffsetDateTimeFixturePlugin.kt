package com.yamachoo.server.fixture

import java.time.OffsetDateTime
import kotlin.reflect.KType

object OffsetDateTimeFixturePlugin : ArbFixturePlugin {
    override fun supports(type: KType, name: String?): Boolean {
        return type.classifier == OffsetDateTime::class
    }

    override fun generate(type: KType, name: String?): Any {
        return OffsetDateTime.now()
    }
}
