package io.github.lavabear.kline.graphql

import com.expedia.graphql.SchemaGeneratorConfig
import com.expedia.graphql.TopLevelObject
import com.expedia.graphql.hooks.SchemaGeneratorHooks
import com.expedia.graphql.toSchema
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.analysis.MaxQueryComplexityInstrumentation
import graphql.analysis.MaxQueryDepthInstrumentation
import graphql.execution.instrumentation.ChainedInstrumentation
import graphql.execution.instrumentation.tracing.TracingInstrumentation
import graphql.schema.GraphQLType
import io.github.lavabear.kline.db.Persistence
import org.joda.time.DateTime
import java.time.LocalDate
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class GraphqlRequest(
    val query: String,
    val operationName: String?,
    val context: Any?,
    val root: Any?,
    val variables: Map<String, Any>?
) {
    fun prepared() = ExecutionInput(query, operationName, context, root, variables)
}

class GraphQLInitializationException(message: String, cause: Throwable?) : Exception(message, cause)

@Throws(GraphQLInitializationException::class)
fun graphql(persistence: Persistence): GraphQL {
    val graphqlPackages = listOf("io.github.lavabear.kline.api", "io.github.lavabear.kline.graphql")

    val schema = try {
        toSchema(
            SchemaGeneratorConfig(graphqlPackages, hooks = CustomSchemaGeneratorHooks(GraphQLTypes())),
            listOf(TopLevelObject(Query(persistence))),
            listOf(TopLevelObject(Mutation(persistence)))
        )
    } catch (e: Exception) {
        throw GraphQLInitializationException("Failed to create GraphQLSchema", e)
    }

    return GraphQL.newGraphQL(schema)
        .instrumentation(
            ChainedInstrumentation(
                listOf(
                    TracingInstrumentation(),
                    MaxQueryDepthInstrumentation(13),
                    MaxQueryComplexityInstrumentation(58)
                )
            )
        )
        .build()
}

class CustomSchemaGeneratorHooks(private val graphQLTypes: GraphQLTypes) : SchemaGeneratorHooks {

    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        UUID::class -> graphQLTypes.uuid
        DateTime::class -> graphQLTypes.dateTime
        LocalDate::class -> graphQLTypes.date
        else -> null
    }
}