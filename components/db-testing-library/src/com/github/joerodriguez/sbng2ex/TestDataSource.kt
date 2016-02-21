package com.github.joerodriguez.sbng2ex

import org.postgresql.ds.PGPoolingDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class TestDataSource(dbName: String) {

    private val dataSource: PGPoolingDataSource

    init {
        dataSource = createDataSource(dbName)
        DbTruncator(jdbcTemplate);
    }

    val jdbcTemplate: JdbcTemplate
        get() = JdbcTemplate(dataSource)

    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
        get() = NamedParameterJdbcTemplate(dataSource)

    private fun createDataSource(dbName: String): PGPoolingDataSource {
        val dataSource: PGPoolingDataSource
        dataSource = PGPoolingDataSource()
        dataSource.user = "admin"
        dataSource.password = "admin"
        dataSource.serverName = "localhost"
        dataSource.databaseName = dbName
        return dataSource
    }
}
