package io.github.lavabear.kline.db

import io.github.lavabear.kline.Metrics
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersistenceTest {

    private val metrics = Metrics()
    private val persistence = Persistence(DbConfig.dataSource("jdbc:h2:mem:test", metrics))

    init {
        persistence.prepareDatabase()
    }

    @Test
    fun `create user`() {
        assertTrue { persistence.createUser("fred").join() != null }
        transaction { Users.deleteAll() }
    }

    @Test
    fun `get all users - no users`() {
        assertEquals(0, persistence.allUsers().join().size)
    }
}
