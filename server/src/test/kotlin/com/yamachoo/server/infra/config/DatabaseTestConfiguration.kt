package com.yamachoo.server.infra.config

import com.mysql.cj.jdbc.MysqlDataSource
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.MountableFile.forHostPath
import javax.sql.DataSource

@TestConfiguration(proxyBeanMethods = false)
class DatabaseTestConfiguration {
    private val dockerImageName = System.getenv("MYSQL_DOCKER_IMAGE") ?: error("MYSQL_DOCKER_IMAGE not found")

    private val mySQLContainer = MySQLContainer<Nothing>(dockerImageName).apply {
        startupAttempts = 1
        withReuse(true)

        val databaseName = System.getenv("DB_NAME") ?: error("DB_NAME not found")
        withDatabaseName(databaseName)

        val sqlInitDir = "/docker-entrypoint-initdb.d/"
        withCopyToContainer(forHostPath("../db/schema.sql"), sqlInitDir + "000-schema.sql")
    }

    @Bean
    @ServiceConnection
    fun mySQLContainer(): MySQLContainer<Nothing> {
        return mySQLContainer
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
