package io.github.lavabear.kline

import com.codahale.metrics.Meter
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.health.HealthCheckRegistry
import com.codahale.metrics.Slf4jReporter
import com.codahale.metrics.Timer
import com.codahale.metrics.health.HealthCheck
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

data class Metrics(val metricRegistry: MetricRegistry = MetricRegistry(),
                   val healthCheckRegistry : HealthCheckRegistry = HealthCheckRegistry()) {
    val requestTimer : Timer = metricRegistry.timer("requests")
    val meter404 : Meter = metricRegistry.meter("not-found")
    val meter500 : Meter = metricRegistry.meter("error-count")
    val statusCount : Meter = metricRegistry.meter("status-calls")

    val failedDatabaseCount : Meter = metricRegistry.meter("failed-connection-count")

    val healthCheck : Supplier<SortedMap<String, HealthCheck.Result>>
            = Suppliers.memoizeWithExpiration(healthCheckRegistry::runHealthChecks, 1, TimeUnit.MINUTES)

    fun startReporting() {
        val reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LOG)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()

        reporter.start(5, TimeUnit.MINUTES)
    }

    companion object {
        private val LOG : Logger = LoggerFactory.getLogger(Metrics::class.java)
    }
}