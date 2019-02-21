package io.github.lavabear.kline.graphql

import graphql.schema.*
import org.joda.time.DateTime
import java.time.LocalDate
import java.util.*

class GraphQLTypes {

    val uuid = scalarType(
        "UUID", "A type representing a formatted java.util.UUID",
        UUID::fromString, { it is UUID }
    )

    val date = scalarType(
        "LocalDate", "A type representing a formatted java.time.LocalDate",
        LocalDate::parse, { it is LocalDate }
    )

    val dateTime = scalarType(
        "DateTime", "A type representing a formatted org.joda.time.DateTime",
        DateTime::parse, { it is DateTime }
    )

    private fun <T> scalarType(
        name: String, description: String,
        prepInput: (s: String) -> T,
        shouldConvert: (t: Any?) -> Boolean,
        output: (t: T) -> String = { it.toString() },
        isValidInput: (t: Any?) -> Boolean = { it is String}
    ) : GraphQLScalarType {
        val d = object : Coercing<T, String> {
            override fun parseLiteral(input: Any?): T {
                return if (isValidInput(input))
                    prepInput(input as String)
                else
                    throw CoercingSerializeException("Unable to parseLiteral - $input")
            }

            override fun serialize(dataFetcherResult: Any?): String {
                return if (shouldConvert(dataFetcherResult))
                    output(dataFetcherResult as T)
                else
                    throw CoercingSerializeException("Unable to serialize - $dataFetcherResult")
            }

            override fun parseValue(input: Any?): T {
                return if (isValidInput(input))
                    prepInput(input as String)
                else
                    throw CoercingSerializeException("Unable to parseValue - $input")
            }
        }

        return GraphQLScalarType(name, description, d)
    }
}