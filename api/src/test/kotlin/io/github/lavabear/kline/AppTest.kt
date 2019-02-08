package io.github.lavabear.kline

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class AppTest {
    private val metrics = mockk<Metrics>()

    @Test
    fun `StartupException thrown for failed database connections`() {
        assertThrows<StartupException> {
            dataSourceRetryOnFailure(metrics, "", attempts = 0)
        }
    }

    @Test
    fun `database connection retry works - invalid url`() {
        val metrics = Metrics()
        assertThrows<StartupException> {
            dataSourceRetryOnFailure(metrics, "", attempts = 1, waitTimeMs = 1)
        }
        assertEquals(1, metrics.failedDatabaseCount.count)
    }

    @ParameterizedTest
    @MethodSource("testDatabaseUrls")
    fun `hostFromDatabaseUrl only returns hostname`(testCase: Pair<String, String>) {
        val (testUrl: String, expected: String) = testCase
        assertEquals(expected, hostFromDatabaseUrl(testUrl, prefix = ""))
    }

    fun testDatabaseUrls() : Stream<Pair<String, String>> = Stream.of(
        "jdbc:postgresql://test:5432/dbname?user=username&password=password" to "test"
    )
}