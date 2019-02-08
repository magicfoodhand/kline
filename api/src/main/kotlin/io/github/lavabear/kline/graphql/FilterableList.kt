package io.github.lavabear.kline.graphql

import com.expedia.graphql.annotations.GraphQLDescription
import com.expedia.graphql.annotations.GraphQLIgnore
import io.github.lavabear.kline.api.User

import io.github.lavabear.kline.db.FutureList

@GraphQLDescription("List Filtering")
class UserList(@GraphQLIgnore private val resolver: FutureList<User>) {

/*}
interface FilterableList<T> {
*/
    @GraphQLDescription("Get Everything")
    fun all() = resolver.join()

    @GraphQLDescription("Skip the First `number` Elements")
    fun skip(number: Int) = all().drop(number)

    @GraphQLDescription("Take the First `number` Elements")
    fun first(number: Int = 1) = all().take(number)

    @GraphQLDescription("Take the Last `number` Elements")
    fun last(number: Int = 1) = all().takeLast(number)

    @GraphQLDescription("Take All Except for the Last `number` of Elements")
    fun leave(number: Int) = all().dropLast(number)

    @GraphQLDescription("Get Item at `index`")
    fun get(index: Int) = all()[index]

    @GraphQLDescription("Take Elements Between `start` and `end`")
    fun subset(start: Int, end: Int) = all().subList(start, end)
}