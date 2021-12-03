package com.trucking.starter.routes.union;

import com.trucking.starter.utilities.Utils;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;

public class UnionFilter {

    private Router router;
    private SqlClient db;
    private Utils obj;

    public UnionFilter(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }

    public void routeSetup(){
        router.post("/union/filter").handler(this::filterUnion);

    }

    public  void filterUnion(RoutingContext ctx) {

    }
    
}
