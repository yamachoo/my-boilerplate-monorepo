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
    implementation(libs.bundles.spring.graphql)
    implementation(libs.bundles.graphql.java.extended)
    implementation(libs.kotlin.logging.jvm)

    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.bundles.komapper)
    ksp(libs.komapper.processor)

    developmentOnly(libs.spring.boot.devtools)

    testFixturesImplementation(platform(libs.kotest.bom))
    testFixturesImplementation(libs.kotest.property)
    testFixturesImplementation(platform(libs.kolin.faker.bom))
    testFixturesImplementation(libs.kolin.faker)
    testFixturesImplementation(libs.dbsetup.kotlin)
    testFixturesImplementation(libs.mysql.connector.j)

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.bundles.spring.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.mockk)

    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.bundles.testcontainers)
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
    format("toml") {
        target("gradle/**/*.toml")
        prettier(mapOf("prettier" to "3.0.3", "prettier-plugin-toml" to "2.0.1"))
            .config(mapOf("plugins" to listOf("prettier-plugin-toml")))
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
