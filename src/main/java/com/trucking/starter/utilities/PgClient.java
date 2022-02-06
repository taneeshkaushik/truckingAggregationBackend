package com.trucking.starter.utilities;

import io.vertx.core.Vertx;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.pgclient.SslMode;
import io.vertx.sqlclient.SqlClient;

public class PgClient {
    
    private Vertx vertx;
    

    public PgClient(Vertx vertx) {
        this.vertx = vertx;
    }


    public SqlClient getSqlClient(){
        
        new PgConnectOptions();
        PgConnectOptions connectOptions = PgConnectOptions.fromUri(System.getenv("DATABASE_URL"));
        // .fromUri("postgres://aeidgicn:EZO6CzcFWnPyyoqAj5Prpf8nQSLbTn6d@satao.db.elephantsql.com/aeidgicn");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);

        PgPool pool = PgPool.pool(vertx, connectOptions, poolOptions);
        return pool;

    }

}
