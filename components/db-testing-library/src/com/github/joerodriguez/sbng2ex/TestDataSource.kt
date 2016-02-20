package com.github.joerodriguez.sbng2ex

import org.postgresql.ds.PGPoolingDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class TestDataSource(dbName: String) {

    private val dataSource: PGPoolingDataSource

    init {
        dataSource = createDataSource(dbName)
        truncateAll()
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

    private fun truncateAll() {
        jdbcTemplate.execute(
                """
                    DO
                    ${'$'}do${'$'}
                    DECLARE
                      table_record RECORD;
                      table_name   VARCHAR(50);
                    BEGIN
                      FOR table_record IN (SELECT tablename
                                           FROM pg_tables
                                           WHERE schemaname = 'public' AND tablename != 'schema_version')
                      LOOP
                        table_name := table_record.tablename;
                        EXECUTE 'TRUNCATE TABLE ' || quote_ident(table_name) || ' CASCADE;';
                      END LOOP;
                    END
                    ${'$'}do${'$'};
                """
        )
    }
}