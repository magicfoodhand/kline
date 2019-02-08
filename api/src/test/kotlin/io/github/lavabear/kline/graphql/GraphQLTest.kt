package io.github.lavabear.kline.graphql

import io.github.lavabear.kline.api.User
import io.github.lavabear.kline.db.Persistence
import io.mockk.*
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private typealias GraphQLData = Map<String, List<Map<String, Any>>>

class GraphQLTest {

    private val persistence = mockk<Persistence>()
    // All Tests in this class will fail if GraphQL object cannot be built
    private val graphql = graphql(persistence)

    private val usersNameQuery = """
            {
                users {
                    all {
                        name
                    }
                }
            }
        """.trimIndent()

    @Test
    fun `base specification - bad query`() {
        val badQuery = graphql.execute("")

        val specification = badQuery.toSpecification()
        assertFalse(specification.isEmpty())
        assertEquals(setOf("extensions", "data", "errors"), specification.keys)

        assertFalse(badQuery.errors.isEmpty())
    }

    @Test
    fun `request all users - no users`() {
        every { persistence.allUsers() } returns CompletableFuture.completedFuture(emptyList())

        val userResult = graphql.execute(usersNameQuery)

        verify(exactly = 1) { persistence.allUsers() }

        val specification = userResult.toSpecification()

        val userSpec = specification["data"] as GraphQLData
        val users = (userSpec["users"] as Map<String, List<Map<String, Any>>>)["all"]

        assertNotNull(users)
        assertTrue(users.isEmpty())

        clearMocks(persistence)
    }

    @Test
    fun `request all users - one user`() {
        every { persistence.allUsers() } returns CompletableFuture.completedFuture(listOf(User(UUID.randomUUID(), "test", DateTime.now())))

        val userResult = graphql.execute(usersNameQuery)

        verify(exactly = 1) { persistence.allUsers() }

        val specification = userResult.toSpecification()

        val userSpec = specification["data"] as GraphQLData
        val users = (userSpec["users"] as Map<String, List<Map<String, Any>>>)["all"]
        assertNotNull(users)
        assertEquals(1, users.size)

        val user = users.first()
        assertEquals(setOf("name"), user.keys)
        assertEquals("test", user["name"])

        clearMocks(persistence)
    }
}