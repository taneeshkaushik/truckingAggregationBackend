package com.trucking.starter.routes.transporter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;


public class Transporter {
    
    private Router router;
    private SqlClient db;

    public Transporter(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
    }

    public void routeSetup(){
        router.get("/transporter/getalltransporters").handler(this::fetchAllTransporters);
        router.post("/transporter/gettransporter").handler(this::fetchtransporterProfile);
        router.post("/transporter/updatetransporter").handler(this::updatetransporter);
        router.post("/transporter/createtransporter").handler(this::createtransporter);
        router.post("/transporter/deletetransporter").handler(this::deletetransporter);
    }

    public void fetchAllTransporters(RoutingContext ctx)
    {
        try {

            db
            .query("SELECT * FROM public.transporter")
            .execute(ar->{
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
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }

    public void fetchtransporterProfile(RoutingContext ctx){
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("SELECT * FROM public.transporter WHERE id=$1")
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
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }
    public void updatetransporter(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.transporter SET name=$1 , order_done= $2 , order_pending=$3 , no_of_trucks=$4, no_of_members=$5, rating=$6  WHERE  id = $7")
            .execute(Tuple.of(req.getValue("name"),req.getValue("order_done"),req.getValue("order_pending") ,req.getValue("no_of_trucks"),req.getValue("no_of_members"),req.getValue("rating") ), ar -> {
                if (ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                    );
                }else{
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , " Some error happened")
                    );
                }
            });

        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }
    public void createtransporter(RoutingContext ctx) {

        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.transporter(name, date) VALUES ($1 , $2)")
            .execute(Tuple.of(req.getValue("name"),req.getValue("date") ), ar -> {
                if (ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                    );
                }else{
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , " Some error happened")
                    );
                }
            });
        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }
    public  void  deletetransporter(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.transporter WHERE id = $1")
            .execute(Tuple.of(req.getValue("id") ), ar -> {
                if (ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                    );
                }else{
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , " Some error happened")
                    );
                }
            });
        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        } 
    }
}
