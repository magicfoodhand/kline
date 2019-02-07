package io.github.lavabear.kline.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.CompletableFuture

typealias FutureList<T> = CompletableFuture<List<T>>

fun <T> ColumnSet.query(
    vararg columns: Column<*>,
    where: SqlExpressionBuilder.() -> Op<Boolean>,
    adapter: (rs: ResultRow) -> T
): FutureList<T> = CompletableFuture.supplyAsync {
    transaction { this@query.slice(*columns).select(where).map(adapter) }
}

fun <T : Table, U> T.save(
    idColumn: Column<U>,
    body: T.(InsertStatement<Number>) -> Unit
): CompletableFuture<U> = CompletableFuture.supplyAsync {
    transaction { this@save.insert(body) get idColumn }
}