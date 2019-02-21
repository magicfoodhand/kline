package io.github.lavabear.kline.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import io.github.lavabear.kline.api.User
import io.github.lavabear.kline.extensions.FutureList

typealias T = User

@GraphQLDescription("List Filtering")
class UserList(
    private val resolver: FutureList<User>
) {
    @GraphQLDescription("Get Everything")
    fun all() : FutureList<T> = resolver

    @GraphQLDescription("Skip the First `number` Elements")
    fun skip(number: Int) = all().thenApply { it.drop(number) }

    @GraphQLDescription("Take the First `number` Elements")
    fun first(number: Int = 1) = all().thenApply { it.take(number) }

    @GraphQLDescription("Take the Last `number` Elements")
    fun last(number: Int = 1) = all().thenApply { it.takeLast(number) }

    @GraphQLDescription("Take All Except for the Last `number` of Elements")
    fun leave(number: Int) = all().thenApply { it.dropLast(number) }

    @GraphQLDescription("Get Item at `index`")
    fun get(index: Int) = all().thenApply { it[index] }

    @GraphQLDescription("Take Elements Between `start` and `end`")
    fun subset(start: Int, end: Int) = all().thenApply { it.subList(start, end) }
}