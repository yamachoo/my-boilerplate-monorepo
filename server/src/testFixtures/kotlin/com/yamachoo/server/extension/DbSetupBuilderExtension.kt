package com.yamachoo.server.extension

import com.ninja_squad.dbsetup_kotlin.DbSetupBuilder
import com.ninja_squad.dbsetup_kotlin.mappedValues
import com.yamachoo.server.domain.user.User
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object DbSetupBuilderExtension {
    @PublishedApi
    internal val entityToTableNameMap = mapOf<KClass<out Any>, String>(
        User::class to "users",
    )

    @Suppress("SpreadOperator")
    @JvmName("insertEntities")
    inline fun <reified T : Any> DbSetupBuilder.insert(entities: List<T>) {
        val tableName = entityToTableNameMap[T::class] ?: T::class.simpleName!!.camelToSnake()
        insertInto(tableName) {
            entities.forEach {
                mappedValues(*it.toPairArray())
            }
        }
    }

    @PublishedApi
    internal inline fun <reified T : Any> T.toPairArray(
        excludeProperties: List<KProperty1<T, Any?>> = emptyList()
    ): Array<Pair<String, Any?>> {
        val excludes = excludeProperties.map { it.name.camelToSnake() }
        return T::class.memberProperties
            .filter { prop ->
                prop.visibility == KVisibility.PUBLIC && !excludes.contains(prop.name.camelToSnake())
            }
            .map { prop ->
                val value = prop.get(this)?.let { processValue(it) }
                prop.name.camelToSnake() to value
            }
            .toTypedArray()
    }

    @PublishedApi
    internal fun String.camelToSnake(): String =
        replace(Regex("(?<=.)([A-Z])"), "_$1").lowercase()

    @PublishedApi
    internal fun processValue(value: Any): Any? {
        return when {
            value::class.isValue -> {
                value::class.memberProperties.first().getter.call(value)
            }
            value is Enum<*> -> value.name
            value is Collection<*> -> value.joinToString(",")
            else -> value.toString()
        }
    }
}
