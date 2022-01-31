package com.trucking.starter.utilities;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.SqlClient;

public class PgClient {
    
    private Vertx vertx;
    

    public PgClient(Vertx vertx) {
        this.vertx = vertx;
    }


    public SqlClient getSqlClient(){
        
        PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        // .setHost("ec2-52-87-81-98.compute-1.amazonaws.com")
        // .setDatabase("d6bpq7mth0lrfd")
        // .setUser("ybynpexjzvsror")
        // .setPassword("3c5dd67ab8206a0b1e119ab25aa15f4834bd653c4d893dabcd805ef150323c31");
        .setHost("localhost")
        .setDatabase("trucking_aggregation")
        .setUser("postgres")
        .setPassword("abhishek");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);
        // Create the client pool
        SqlClient client = PgPool.client(vertx, connectOptions, poolOptions);
        return client;

    }

}
