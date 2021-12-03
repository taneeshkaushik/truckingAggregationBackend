package com.trucking.starter.routes.transporter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.time.*;
import java.util.*;

import com.trucking.starter.utilities.Utils;

import io.vertx.core.MultiMap;
import io.vertx.core.json.*;
import io.vertx.sqlclient.*;



public class Transporter {
    
    private Router router;
    private SqlClient db;
    private Utils obj;

    public Transporter(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }

    public void routeSetup(){
        router.get("/transporter/getall").handler(this::fetchAllTransporters);
        router.get("/transporter/getdetails").handler(this::fetchtransporterProfile);
        router.post("/transporter/update").handler(this::updatetransporter);
        router.post("/transporter/create").handler(this::createtransporter);
        router.delete("/transporter/delete").handler(this::deletetransporter);
    }

    public void fetchAllTransporters(RoutingContext ctx)
    {
        try {

            db
            .query("SELECT * FROM public.transporter")
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

    public void fetchtransporterProfile(RoutingContext ctx){
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.transporter WHERE user_id=$1")
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
    public void updatetransporter(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.transporter SET name=$1 , order_done= $2 , order_pending=$3 , no_of_trucks=$4, no_of_members=$5, rating=$6  WHERE  id = $7")
            .execute(Tuple.of(req.getValue("name"),req.getValue("order_done"),req.getValue("order_pending") ,req.getValue("no_of_trucks"),req.getValue("no_of_members"),req.getValue("rating") , req.getValue("id") ), ar -> {
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
    public void createtransporter(RoutingContext ctx) {

        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.transporter(name, date) VALUES ($1 , $2)")
            .execute(Tuple.of(req.getValue("name"), LocalDate.parse((req.getString("date")))), ar -> {
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
    public  void  deletetransporter(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            // List<String> params = ctx.queryParam("id");
            // System.out.println(params.get(0));
            db
            .preparedQuery("DELETE FROM public.transporter WHERE id = $1")
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
