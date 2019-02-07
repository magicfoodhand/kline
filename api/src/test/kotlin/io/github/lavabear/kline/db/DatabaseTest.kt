package io.github.lavabear.kline.db

import io.github.lavabear.kline.Metrics
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class DatabaseTest {

    private val metrics = Metrics()

    @Test
    fun `run database migrations`() {
        val persistence = Persistence(DbConfig.dataSource("jdbc:h2:mem:test", metrics))
        persistence.prepareDatabase()

        assertTrue(true)
    }
}
