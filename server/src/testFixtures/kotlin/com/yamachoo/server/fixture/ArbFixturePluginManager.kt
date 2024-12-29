package com.yamachoo.server.fixture

import kotlin.reflect.KType

object ArbFixturePluginManager {
    private val plugins = mutableListOf<ArbFixturePlugin>()

    init {
        register(EmailFixturePlugin)
        register(UsernameFixturePlugin)
        register(OffsetDateTimeFixturePlugin)
    }

    private fun register(plugin: ArbFixturePlugin) {
        plugins.add(plugin)
    }

    fun findPlugin(type: KType): ArbFixturePlugin? {
        return plugins.firstOrNull { it.supports(type) }
    }
}
