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
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLType
import io.github.lavabear.kline.db.Persistence
import org.joda.time.DateTime
import java.time.LocalDate
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class GraphqlRequest(
    private val query: String,
    private val operationName: String,
    private val context: Any,
    private val root: Any,
    private val variables: Map<String, Any>
) {
    fun prepared() = ExecutionInput(query, operationName, context, root, variables)
}

fun graphql(persistence: Persistence): GraphQL {
    val graphqlPackages = listOf("io.github.lavabear.kline.api", "io.github.lavabear.kline.graphql")

    val schema = toSchema(
        listOf(TopLevelObject(Query(persistence))),
        listOf(TopLevelObject(Mutation(persistence))),
        SchemaGeneratorConfig(graphqlPackages, hooks = CustomSchemaGeneratorHooks(GraphQLTypes(graphqlPackages)))
    )

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
        else -> {
            if(type.classifier.toString() == "T")
                graphQLTypes.generic
            else
                null
        }
    }
}