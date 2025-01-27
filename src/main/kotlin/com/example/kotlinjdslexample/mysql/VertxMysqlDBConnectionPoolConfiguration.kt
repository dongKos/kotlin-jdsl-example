package com.example.kotlinjdslexample.mysql

import io.vertx.core.Vertx
import io.vertx.jdbcclient.JDBCConnectOptions
import io.vertx.jdbcclient.JDBCPool
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlConnectOptions
import org.hibernate.internal.util.config.ConfigurationHelper
import org.hibernate.reactive.pool.impl.DefaultSqlClientPool
import org.hibernate.reactive.pool.impl.DefaultSqlClientPoolConfiguration
import org.hibernate.reactive.provider.Settings
import java.net.URI

class MYSQLConnectionPool : DefaultSqlClientPool() {
    override fun createPool(
        uri: URI,
        connectOptions: SqlConnectOptions,
        poolOptions: PoolOptions,
        vertx: Vertx
    ): Pool {
        return JDBCPool.pool(
            vertx,
            JDBCConnectOptions()
                .setJdbcUrl(connectOptions.host)
                .setUser(connectOptions.user)
                .setPassword(connectOptions.password)
                .setDatabase(connectOptions.database),
            poolOptions
        )
    }
}

class VertxMysqlDBConnectionPoolConfiguration : DefaultSqlClientPoolConfiguration() {
    private lateinit var user: String
    private var pass: String? = null
    private var cacheMaxSize: Int? = null
    private var sqlLimit: Int? = null
    override fun connectOptions(uri: URI): SqlConnectOptions {
        return SqlConnectOptions()
            .setHost("jdbc:$uri")
            .setUser(user)
            .setPassword(pass)
    }

    override fun configure(configuration: MutableMap<Any?, Any?>) {
        user = ConfigurationHelper.getString(Settings.USER, configuration)
        pass = ConfigurationHelper.getString(Settings.PASS, configuration)
        cacheMaxSize = ConfigurationHelper.getInteger(Settings.PREPARED_STATEMENT_CACHE_MAX_SIZE, configuration)
        sqlLimit = ConfigurationHelper.getInteger(Settings.PREPARED_STATEMENT_CACHE_SQL_LIMIT, configuration)
        super.configure(configuration)
    }
}
