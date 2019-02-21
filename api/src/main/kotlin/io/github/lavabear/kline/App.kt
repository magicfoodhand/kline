package io.github.lavabear.kline

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import io.github.lavabear.kline.db.DbConfig
import io.github.lavabear.kline.db.Persistence
import io.github.lavabear.kline.graphql.graphql
import io.github.lavabear.kline.server.Routes
import io.github.lavabear.kline.server.Server

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.net.URI
import java.util.*
import javax.sql.DataSource

private val LOG: Logger = LoggerFactory.getLogger(::main.javaClass)

fun main() {
    val objectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        false
    )

    val routeSetup = prepApplication(databaseUrl())

    Server.configure(routeSetup, objectMapper).start()
}

private val waitTimes : Map<Int, Long> = mapOf<Int, Long>(
    5 to 5_000,
    4 to 10_000,
    3 to 30_000,
    2 to 60_000,
    1 to 120_000
)

class StartupException(override val message: String?): Error()

private val databaseHostMatcher = """jdbc:\w+://(\w+)""".toRegex()

fun hostFromDatabaseUrl(
    databaseUrl: String, prefix: String = "from "
) : String = Optional.ofNullable(databaseHostMatcher.find(databaseUrl))
    .map { it.groupValues[1] }
    .map { "$prefix$it" }
    .orElse("")


fun dataSourceRetryOnFailure(
    metrics: Metrics, databaseUrl: String,
    attempts: Int = 5, waitTimeMs: Long = waitTimes[attempts] ?: 1_000,
    dataSourceProvider: (String, Metrics) -> DataSource = DbConfig::dataSource
) : DataSource {
    if(attempts <= 0)
        throw StartupException("Could not obtain database connection: ${hostFromDatabaseUrl(databaseUrl)}")

    return try {
        dataSourceProvider(databaseUrl, metrics)
    } catch (e: Throwable) {
        val remainingAttempts = attempts - 1
        LOG.warn("Failed to connect ", e)
        LOG.warn("remaining attempts: {} - retry in {} seconds", remainingAttempts, waitTimeMs / 1_000)
        metrics.failedDatabaseCount.mark()
        Thread.sleep(waitTimeMs)
        dataSourceRetryOnFailure(metrics, databaseUrl, remainingAttempts)
    }
}

fun prepApplication(databaseUrl: String): Routes {
    val metrics = Metrics()

    val dataSource = dataSourceRetryOnFailure(metrics, databaseUrl)

    val persistence = Persistence(dataSource)
    persistence.prepareDatabase()

    metrics.startReporting()

    return Routes(metrics, graphql(persistence))
}

private fun databaseUrl(): String {
    val databaseUrl = System.getenv("DATABASE_URL")
    if (databaseUrl.isNullOrEmpty()) {
        LOG.error("`DATABASE_URL` is not set")
        System.exit(1)
    }
    return DbConfig.postgresJdbcUrl(URI(databaseUrl))
}
