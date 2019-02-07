package io.github.lavabear.kline

import io.github.lavabear.kline.db.DbConfig
import io.github.lavabear.kline.db.Persistence
import io.github.lavabear.kline.graphql.graphql
import io.github.lavabear.kline.server.Routes
import io.github.lavabear.kline.server.configureServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

private val LOG: Logger = LoggerFactory.getLogger(::main.javaClass)

fun main() {
    val routeSetup = prepApplication(databaseUrl())

    configureServer(routeSetup).start()
}

fun prepApplication(databaseUrl: String): Routes {
    val metrics = Metrics()

    val dataSource = DbConfig.dataSource(databaseUrl, metrics)

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
