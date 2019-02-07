package io.github.lavabear.kline.server

import graphql.GraphQL
import io.github.lavabear.kline.Metrics
import io.github.lavabear.kline.api.Status
import io.github.lavabear.kline.api.StatusType
import io.github.lavabear.kline.api.errorMessage
import io.github.lavabear.kline.api.notFound
import io.github.lavabear.kline.graphql.GraphqlRequest
import io.javalin.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Routes(val metrics: Metrics, val graphQL: GraphQL) {
    fun status(context: Context) {
        metrics.statusCount.mark()
        val healthStatus = metrics.healthCheck.get()
        val status = if (healthStatus.filter { (_, v) -> !v.isHealthy }.isNotEmpty())
            Status(StatusType.WARN, healthStatus)
        else
            Status(StatusType.OK, healthStatus)
        LOG.info("Current Status - {}", status)
        context.json(status)
    }

    fun graphql(context: Context)  {
        metrics.requestTimer.time {
            val request = context.bodyAsClass(GraphqlRequest::class.java)
            graphQL.execute(request.prepared())
        }
    }

    fun error404(context: Context)  {
        metrics.meter404.mark()
        context.json(notFound())
    }

    fun error500(context: Context)  {
        metrics.meter500.mark()
        context.json(errorMessage())
    }

    companion object {
        private val LOG : Logger = LoggerFactory.getLogger(Routes::class.java)
    }
}