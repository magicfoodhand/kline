package io.github.lavabear.kline.api

import org.joda.time.DateTime
import java.util.UUID

data class User(val id: UUID, val name: String, val created: DateTime)