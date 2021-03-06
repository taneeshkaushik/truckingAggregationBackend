package com.trucking.starter.routes.union;
import java.time.LocalDate;

import com.trucking.starter.utilities.Utils;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.*;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

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
        router.get("/union/getall").handler(this::fetchAllUnions);
        router.get("/union/getdetails").handler(this::fetchUnionProfile);
        router.post("/union/update").handler(this::updateUnion);
        router.post("/union/create").handler(this::createUnion);
        router.delete("/union/delete").handler(this::deleteUnion);
    }

    public void fetchAllUnions(RoutingContext ctx)
    {
        try {

            db
            .query("SELECT * FROM public.union")
            .execute(ar->{
                if(ar.succeeded()){


                    ctx.json(
                        new JsonObject().put("success" ,  true)
                        .put("data" , obj.RowSet_To_List(ar.result()))
                    );

                }
                else {
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , ar.cause().getMessage())
                    );
                }

            });
            
        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }

    public void fetchUnionProfile(RoutingContext ctx){
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.union WHERE user_id=$1")
            .execute(Tuple.of(params.get("user_id")) , ar -> {
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                        .put("data" , obj.RowSet_To_List(ar.result()).get(0))

                    );

                }
                else {
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , ar.cause().getMessage())
                    );
                }

            });

        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }
    public void updateUnion(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.union SET name=$1 , order_done= $2 , order_pending=$3, no_of_members=$4, rating=$5  WHERE  id = $6")
            .execute(Tuple.of(req.getValue("name"),req.getValue("order_done"),req.getValue("order_pending") ,req.getValue("no_of_members"),req.getValue("rating") , req.getValue("id") ), ar -> {
                if (ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                    );
                }else{
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , ar.cause().getMessage())
                    );
                }
            });

        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }
    public void createUnion(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.union(name , user_id) VALUES ($1 , $2 )")
            .execute(Tuple.of(req.getValue("name"),req.getValue("user_id")), ar -> {
                if (ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                    );
                }else{
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , ar.cause().getMessage())
                    );
                }
            });
        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }
    public  void  deleteUnion(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.union WHERE id = $1")
            .execute(Tuple.of( req.getValue("id") ), ar -> {
                if (ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                    );
                }else{
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , ar.cause().getMessage())
                    );
                }
            });
        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        } 
    }
}
