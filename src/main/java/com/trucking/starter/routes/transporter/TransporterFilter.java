package com.trucking.starter.routes.transporter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

public class TransporterFilter {
    private Router router;
    private SqlClient db;

    public TransporterFilter(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
    }
    
    public void routeSetup(){
        router.post("/tranporter/filter").handler(this::filterTransporter);

    }

    public  void filterTransporter(RoutingContext ctx) {
//         SELECT * FROM table_name  
// WHERE  
//     column_name LIKE '%fox%' AND
//     column_name LIKE '%dog%';
        try {

            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("SELECT * FROM public.transporter_members WHERE ")
            .execute(Tuple.of(req.getValue("id")) , ar -> {
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                        .put("data" , ar.result())
                    );
                }
                else {
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , " Some error happened")
                    );
                }

            });
            
        } catch (Exception e) {
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }
}
