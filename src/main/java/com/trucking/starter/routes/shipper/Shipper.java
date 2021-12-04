package com.trucking.starter.routes.shipper;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.time.*;
import java.util.*;

import com.trucking.starter.utilities.Utils;

import io.vertx.core.MultiMap;
import io.vertx.core.json.*;
import io.vertx.sqlclient.*;



public class Shipper {
    
    private Router router;
    private SqlClient db;
    private Utils obj;

    public Shipper(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }

    public void routeSetup(){
        router.get("/shipper/getdetails").handler(this::fetchshipperProfile);
        router.post("/shipper/update").handler(this::updateshipper);
        router.post("/shipper/create").handler(this::createshipper);
        router.delete("/shipper/delete").handler(this::deleteshipper);
    }


    public void fetchshipperProfile(RoutingContext ctx){
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.shipper WHERE user_id=$1")
            .execute(Tuple.of(Integer.parseInt(params.get("user_id"))) , ar -> {
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
    public void updateshipper(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.shipper SET name=$1 , order_done= $2 , order_pending=$3  WHERE  id = $7")
            .execute(Tuple.of(req.getValue("name"),req.getValue("order_done"),req.getValue("order_pending"), req.getValue("id") ), ar -> {
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
    public void createshipper(RoutingContext ctx) {

        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.shipper(name ) VALUES ($1 )")
            .execute(Tuple.of(req.getValue("name")), ar -> {
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
    public  void  deleteshipper(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.shipper WHERE id = $1")
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
