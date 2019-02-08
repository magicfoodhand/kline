package io.github.lavabear.kline.api

import com.expedia.graphql.annotations.GraphQLDescription
import org.joda.time.DateTime
import java.util.UUID

@GraphQLDescription("The user")
data class User(val id: UUID, val name: String, val created: DateTime)