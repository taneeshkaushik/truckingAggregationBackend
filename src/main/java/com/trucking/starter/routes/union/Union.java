package com.trucking.starter.routes.union;
import com.trucking.starter.utilities.Utils;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;

public class Union {

    private Router router;
    private SqlClient db;
    private Utils obj;

    public Union(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }
    
    public void routeSetup(){
        router.post("/union/getunion").handler(this::fetchUnionProfile);
        router.post("/union/updateunion").handler(this::updateUnion);
        router.post("/union/createunion").handler(this::createUnion);
        router.post("/union/deleteunion").handler(this::deleteUnion);
    }

    public void fetchUnionProfile(RoutingContext ctx){

    }
    public void updateUnion(RoutingContext ctx) {

    }
    public void createUnion(RoutingContext ctx) {

    }
    public  void  deleteUnion(RoutingContext ctx) {
        
    }
}
