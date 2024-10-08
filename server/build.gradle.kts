import com.google.cloud.tools.jib.gradle.JibTask
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-test-fixtures`
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.dgs.codegen)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.jib)
}

group = "com.yamachoo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.graphql)
    implementation(libs.spring.data.commons)
    implementation(platform(libs.graphql.dgs.platform.dependencies))
    implementation(libs.graphql.java.extended.scalars)
    implementation(libs.graphql.java.extended.validation)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.reactor.kotlin.extensions)
    implementation(kotlin("reflect"))
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.kotlin.logging.jvm)

    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.komapper.spring.boot.starter.r2dbc)
    implementation(libs.komapper.dialect.mysql.r2dbc)
    ksp(libs.komapper.processor)

    implementation(libs.openfeature.sdk)
    implementation(libs.openfeature.providers.flagd)

    developmentOnly(libs.spring.boot.devtools)

    testFixturesImplementation(platform(libs.kotest.bom))
    testFixturesImplementation(libs.kotest.property)
    testFixturesImplementation(platform(libs.kolin.faker.bom))
    testFixturesImplementation(libs.kolin.faker)
    testFixturesImplementation(libs.dbsetup.kotlin)
    testFixturesImplementation(libs.mysql.connector.j)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.mockk)
    testImplementation(libs.spring.graphql.test)
    testImplementation(libs.reactor.test)

    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.r2dbc)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.generateJava {
    schemaPaths.add("$projectDir/src/main/resources/graphql")
    packageName = "com.yamachoo.server.handler.graphql"
    subPackageNameTypes = "type"
    typeMapping.putAll(mapOf("Email" to "String"))
}

detekt {
    source.setFrom(
        "src/main/kotlin",
        "src/test/kotlin",
        "src/testFixtures/kotlin",
    )
    parallel = true
    autoCorrect = true
}

spotless {
    val ktlintEditorConfig = mapOf("max_line_length" to "120")
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint().editorConfigOverride(ktlintEditorConfig)
    }
    kotlinGradle {
        ktlint().editorConfigOverride(ktlintEditorConfig)
    }
    format("graphql") {
        target("src/*/resources/graphql/**/*.graphqls", "src/*/resources/graphql-test/**/*.graphql")
        prettier()
    }
}

tasks.withType<BootRun> {
    systemProperties = mapOf("spring.output.ansi.enabled" to "always")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = project.name
    }
    container {
        user = "1100:1100"
    }
}

tasks.withType<JibTask> {
    notCompatibleWithConfigurationCache("because https://github.com/GoogleContainerTools/jib/issues/3132")
}
