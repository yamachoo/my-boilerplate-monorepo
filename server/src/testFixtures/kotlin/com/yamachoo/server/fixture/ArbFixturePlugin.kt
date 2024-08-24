package com.yamachoo.server.fixture

import kotlin.reflect.KType

interface ArbFixturePlugin {
    fun supports(type: KType, name: String? = null): Boolean
    fun generate(type: KType, name: String? = null): Any
}
