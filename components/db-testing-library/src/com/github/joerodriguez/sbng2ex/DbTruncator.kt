package com.github.joerodriguez.sbng2ex

import org.springframework.jdbc.core.JdbcTemplate

fun DbTruncator(jdbcTemplate: JdbcTemplate) {
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