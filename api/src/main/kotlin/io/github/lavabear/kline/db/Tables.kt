package io.github.lavabear.kline.db

import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import java.util.*

object Users : Table() {
    val id = uuid("id").primaryKey().clientDefault { UUID.randomUUID() }
    val name = text("name").uniqueIndex()
    val created = datetime("created").index().clientDefault { DateTime.now() }
}