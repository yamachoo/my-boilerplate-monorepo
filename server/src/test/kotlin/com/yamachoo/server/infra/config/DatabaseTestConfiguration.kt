package com.yamachoo.server.infra.config

import com.mysql.cj.jdbc.MysqlDataSource
import org.komapper.dialect.mysql.r2dbc.MySqlR2dbcDialect
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.MySQLR2DBCDatabaseContainer
import org.testcontainers.utility.MountableFile.forHostPath
import javax.sql.DataSource

@TestConfiguration
class DatabaseTestConfiguration {
    private val dockerImageName = System.getenv("MYSQL_DOCKER_IMAGE") ?: error("MYSQL_DOCKER_IMAGE not found")

    private val mySQLContainer = MySQLContainer<Nothing>(dockerImageName).apply {
        startupAttempts = 1
        withReuse(true)

        val sqlInitDir = "/docker-entrypoint-initdb.d/"
        withCopyToContainer(forHostPath("../db/schema.sql"), sqlInitDir + "000-schema.sql")
        start()
    }

    @Bean
    fun r2dbcDatabase(): R2dbcDatabase {
        val connectionFactory = MySQLR2DBCDatabaseContainer.getOptions(mySQLContainer)

        return R2dbcDatabase(
            options = connectionFactory,
            dialect = MySqlR2dbcDialect(),
        )
    }

    @Bean
    fun dataSource(): DataSource {
        return MysqlDataSource().apply {
            setUrl(mySQLContainer.jdbcUrl)
            user = mySQLContainer.username
            password = mySQLContainer.password
        }
    }
}
