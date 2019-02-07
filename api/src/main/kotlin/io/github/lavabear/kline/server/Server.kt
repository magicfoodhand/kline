package io.github.lavabear.kline.server

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.core.util.Util
import io.javalin.translator.json.JavalinJacksonPlugin

fun configureServer(routeSetup: Routes) : Javalin {
    Util.noServerHasBeenStarted = false //  Hide Annoying Javalin Message if app takes more than a second to start

    JavalinJacksonPlugin.configure(
        jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
    )

    return Javalin.create().apply {
        port(Integer.getInteger("port", 8080))
        enableStandardRequestLogging()
        enableDynamicGzip()

        enableCorsForOrigin("*") // enables cors for the specified origin(s)

        routes {
            get("/status", routeSetup::status)

            post("/graphql", routeSetup::graphql)

            error(404, routeSetup::error404)

            error(500, routeSetup::error500)
        }
    }
}