package io.github.lavabear.kline.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.lavabear.kline.Metrics
import java.net.URI
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource

object DbConfig {

    private val idGenerator = AtomicInteger(1)

    fun dataSource(
        jdbcUrl: String, metrics: Metrics
    ): DataSource {
        val config = HikariConfig()

        config.jdbcUrl = jdbcUrl
        config.maximumPoolSize = 10
        config.poolName = "kline.db.${idGenerator.getAndIncrement()}"
        config.connectionTestQuery = "SELECT 1"
        config.metricRegistry = metrics.metricRegistry
        config.healthCheckRegistry = metrics.healthCheckRegistry
        config.connectionTimeout = Duration.ofMinutes(1).toMillis()

        return HikariDataSource(config)
    }

    /***
     * @herokuUri postgres://<username>:<password>@<host>/<dbname>
     * @return jdbc:postgresql://<host>:<port>/<dbname>?sslmode=require&user=<username>&password=<password>
     */
    fun postgresJdbcUrl(dbUri: URI): String {
        val dbUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}"
        val userParts = dbUri.userInfo.split(":")
        val username = userParts[0]
        val base = "$dbUrl?user=$username"
        return if (userParts.size > 1) "$base&password=${userParts[1]}" else base
    }
}
