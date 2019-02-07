package io.github.lavabear.kline.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import io.github.lavabear.kline.db.Persistence

class Mutation(private val persistence: Persistence) {
    @GraphQLDescription("Create User")
    fun createUser(
        @GraphQLDescription("The name of the user") name: String
    ) = persistence.createUser(name)
}