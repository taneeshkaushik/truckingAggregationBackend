package com.trucking.starter.routes.union;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;

public class UnionFilter {

    private Router router;
    private SqlClient db;

    public UnionFilter(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
    }

    public void routeSetup(){
        router.post("/union/filterunion").handler(this::filterUnion);

    }

    public  void filterUnion(RoutingContext ctx) {

    }
    
}
