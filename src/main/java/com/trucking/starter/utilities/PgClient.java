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
        .setHost("cohortdb.cwof1drw7dnv.us-east-2.rds.amazonaws.com")
        .setDatabase("trucking_database")
        .setUser("cohortUser")
        .setPassword("cintra2021");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);
        // Create the client pool
        SqlClient client = PgPool.client(vertx, connectOptions, poolOptions);
        return client;

    }

}
