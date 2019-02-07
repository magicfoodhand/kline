package io.github.lavabear.kline.db

import io.github.lavabear.kline.api.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class Persistence(private val dataSource: DataSource) {
    fun createUser(name: String) = Users.save(Users.id) { it[Users.name] = name }

    fun allUsers() = Users.query(
        Users.id,
        Users.name,
        Users.created, adapter = {
            User(
                it[Users.id],
                it[Users.name],
                it[Users.created]
            )
    }, where = { Users.id eq Users.id })

    fun prepareDatabase() {
        val currentTime = System.currentTimeMillis()
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(Users)
        }

        LOG.info("Database migrations took ${System.currentTimeMillis() - currentTime} ms")
    }

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Persistence::class.java)
    }
}