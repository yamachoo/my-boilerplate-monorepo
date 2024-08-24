package com.yamachoo.server.fixture

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.of
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uByte
import io.kotest.property.arbitrary.uInt
import io.kotest.property.arbitrary.uLong
import io.kotest.property.arbitrary.uShort
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

object ArbFixture {
    inline fun <reified T : Any> fixture(noinline overrides: () -> Map<String, Any?> = { emptyMap() }): T =
        mountClass(T::class.constructors, overrides)

    fun <T : Any> fixture(kClass: KClass<T>, overrides: () -> Map<String, Any?> = { emptyMap() }): T =
        mountClass(kClass.constructors, overrides)

    fun <T : Any> mountClass(
        constructors: Collection<KFunction<T>>,
        overrides: () -> Map<String, Any?> = { emptyMap() },
    ): T {
        val constructor = constructors.firstOrNull {
            it.parameters.any { param -> param.name == "serializationConstructorMarker" }
        } ?: constructors.first()

        val argumentMap = constructor.parameters.associateWith { parameter ->
            overrides()[parameter.name] ?: getCustomOrRandomValue(parameter.type, parameter.name)
        }

        return constructor.callBy(argumentMap)
    }

    private fun getCustomOrRandomValue(type: KType, name: String? = null): Any {
        val plugin = ArbFixturePluginManager.findPlugin(type, name)
        return plugin?.generate(type, name) ?: getRandomParameterValue(type)
    }

    @Suppress("CyclomaticComplexMethod")
    private fun getRandomParameterValue(type: KType, asArb: Boolean = false): Any {
        val classifier = type.classifier as? KClass<*>

        val arb = when {
            // Integer types
            classifier == Byte::class -> Arb.byte()
            classifier == Short::class -> Arb.short()
            classifier == Int::class -> Arb.int()
            classifier == Long::class -> Arb.long()
            // Floating-point types
            classifier == Float::class -> Arb.float()
            classifier == Double::class -> Arb.double()
            // Unsigned integer types
            classifier == UByte::class -> Arb.uByte()
            classifier == UShort::class -> Arb.uShort()
            classifier == UInt::class -> Arb.uInt()
            classifier == ULong::class -> Arb.uLong()
            // Boolean type
            classifier == Boolean::class -> Arb.boolean()
            // Character and String types
            classifier == Char::class -> Arb.char()
            classifier == String::class -> Arb.string()
            // Collection types
            classifier == List::class -> {
                val elementType = requireNotNull(
                    type.arguments.firstOrNull()?.type
                ) { "Invalid type argument for List" }
                Arb.list(getRandomParameterValue(type = elementType, asArb = true) as Arb<*>)
            }
            classifier == Set::class -> {
                val elementType = requireNotNull(type.arguments.firstOrNull()?.type) { "Invalid type argument for Set" }
                Arb.set(getRandomParameterValue(type = elementType, asArb = true) as Arb<*>)
            }
            classifier == Map::class -> {
                val keyType = requireNotNull(type.arguments[0].type) { "Invalid key type argument for Map" }
                val valueType = requireNotNull(type.arguments[1].type) { "Invalid value type argument for Map" }
                Arb.map(
                    getRandomParameterValue(type = keyType, asArb = true) as Arb<*>,
                    getRandomParameterValue(type = valueType, asArb = true) as Arb<*>,
                )
            }
            // Data, enum, and value classes
            classifier?.isData == true -> arbitrary { createInstanceFromDataClass(classifier) }
            classifier?.java?.isEnum == true -> {
                val enumConstants = classifier.java.enumConstants
                require(enumConstants.isEmpty()) { "Enum class ${classifier.qualifiedName} has no constants defined." }
                Arb.of(enumConstants)
            }
            classifier?.isValue == true -> arbitrary { createInstanceFromValueClass(classifier) }
            else -> throw IllegalArgumentException("Unsupported type: ${classifier?.qualifiedName} for KType: $type")
        }

        return if (asArb) arb else arb.single()
    }

    private fun createInstanceFromDataClass(classifier: KClass<*>): Any {
        val primaryConstructor =
            requireNotNull(classifier.primaryConstructor) { "Data class must have a primary constructor" }
        val params = primaryConstructor.parameters.associateWith { parameter ->
            getCustomOrRandomValue(parameter.type, parameter.name)
        }
        return primaryConstructor.callBy(params)
    }

    private fun createInstanceFromValueClass(classifier: KClass<*>): Any {
        val primaryConstructor =
            requireNotNull(classifier.primaryConstructor) { "Value class must have a primary constructor" }
        val parameter = primaryConstructor.parameters.first()
        val paramValue = getCustomOrRandomValue(parameter.type, parameter.name)
        return primaryConstructor.call(paramValue)
    }
}
