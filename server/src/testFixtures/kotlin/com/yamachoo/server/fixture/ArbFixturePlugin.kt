package com.yamachoo.server.fixture

import kotlin.reflect.KType

interface ArbFixturePlugin {
    fun supports(type: KType): Boolean
    fun generate(): Any
}
