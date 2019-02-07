package io.github.lavabear.kline.api

import com.fasterxml.jackson.annotation.JsonInclude

fun notFound() = { SimpleMessage("Not Found") }
fun errorMessage() = { SimpleMessage("Something went wrong") }

data class SimpleMessage(val message: String)

enum class StatusType {
    OK, WARN
}

data class Status(
    val type: StatusType,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val details : Map<String, Any> = emptyMap()
)