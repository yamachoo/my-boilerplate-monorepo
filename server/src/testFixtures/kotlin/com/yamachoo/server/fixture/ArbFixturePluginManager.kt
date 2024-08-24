package com.yamachoo.server.fixture

import kotlin.reflect.KType

object ArbFixturePluginManager {
    private val plugins = mutableListOf<ArbFixturePlugin>()

    fun register(plugin: ArbFixturePlugin) {
        plugins.add(plugin)
    }

    fun findPlugin(type: KType, name: String? = null): ArbFixturePlugin? {
        return plugins.firstOrNull { it.supports(type, name) }
    }
}
