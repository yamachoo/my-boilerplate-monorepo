package com.yamachoo.server.fixture

import java.time.OffsetDateTime
import kotlin.reflect.KType

object OffsetDateTimeFixturePlugin : ArbFixturePlugin {
    override fun supports(type: KType): Boolean {
        return type.classifier == OffsetDateTime::class
    }

    override fun generate(): OffsetDateTime {
        return OffsetDateTime.now()
    }
}
