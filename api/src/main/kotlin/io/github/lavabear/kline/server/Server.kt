package io.github.lavabear.kline.server

import com.fasterxml.jackson.databind.ObjectMapper

import io.javalin.ErrorHandler
import io.javalin.Handler
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import org.slf4j.LoggerFactory

interface Server {
    fun start()

    companion object {

        private class JavalinServer(private val javalin: Javalin): Server {
            override fun start() { javalin.start() }
        }

        private val LOG = LoggerFactory.getLogger(Server::class.java)

        @JvmStatic
        fun configure(routeSetup: Routes, objectMapper: ObjectMapper) : Server {
            JavalinJackson.configure(objectMapper)

            return JavalinServer(Javalin.create().apply {

                port(Integer.getInteger("port", 8080))
                disableStartupBanner()
                requestLogger { context, fl ->
                    LOG.info("{} {} ({}) took {}", context.method(), context.url(), context.status(), fl)
                }

                enableCorsForOrigin("*") // enables cors for the specified origin(s)

                routes {
                    get("/graphiql") {
                        it.render("index.html")
                    }

                    get("/status", simpleHandler(routeSetup::status))

                    post("/graphql", requestHandler(routeSetup::graphql))

                    error(404, errorHandler(routeSetup::error404))

                    error(500, errorHandler(routeSetup::error500))
                }
            })
        }

        private inline fun <reified I, O : Any> requestHandler(crossinline handler: (I) -> O) = Handler {
            val request : I = it.bodyAsClass(I::class.java)
            it.json(handler(request))
        }

        private fun <R : Any> simpleHandler(supplier: () -> R) = Handler { it.json(supplier()) }

        private fun <R : Any> errorHandler(supplier: () -> R) = ErrorHandler { it.json(supplier()) }
    }
}