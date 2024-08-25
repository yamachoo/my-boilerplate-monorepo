package com.yamachoo.server.extension

import com.ninja_squad.dbsetup_kotlin.DbSetupBuilder
import com.ninja_squad.dbsetup_kotlin.dbSetup
import javax.sql.DataSource

object DbSetupExtension {
    private val tablesToExclude = listOf("schema_migrations")

    fun prepareDatabase(dataSource: DataSource, configure: DbSetupBuilder.() -> Unit) {
        dbSetup(to = dataSource) {
            // do the common stuff
            val tablesToTruncate = getAllTableNames(dataSource)
                .filterNot { tablesToExclude.contains(it) }
            truncate(tablesToTruncate)

            // then do the additional configuration with the lambda passed as argument
            configure()
        }.launch()
    }

    private fun getAllTableNames(dataSource: DataSource): List<String> {
        val tableNames = mutableListOf<String>()

        dataSource.connection.use { connection ->
            val metaData = connection.metaData
            val rs = metaData.getTables(null, null, "%", arrayOf("TABLE"))
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"))
            }
        }

        return tableNames
    }
}
