package io.github.lavabear.kline.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import io.github.lavabear.kline.api.User
import io.github.lavabear.kline.db.Persistence

class Query(private val persistence: Persistence) {

    @GraphQLDescription("Get User Info")
    fun users() = UserList(persistence.allUsers())
}