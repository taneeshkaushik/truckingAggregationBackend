package com.trucking.starter.routes.trucks;
import java.time.LocalDate;

import com.trucking.starter.utilities.Utils;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.*;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

/**
 * Trucks
 */
public class Trucks {

    private Router router;
    private SqlClient db;
    private Utils obj;

    public Trucks(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }
    
    public void routeSetup(){
        router.get("/transporter/fetchtrucks").handler(this::getTransporterTrucks);
        router.get("/transporter/gettruckdetails").handler(this::getTruckDetails);
        router.post("/transporter/addtruck").handler(this::addTruck);
        router.delete("/transporter/deletetruck").handler(this::deleteTruck);
    }

    public void getTransporterTrucks(RoutingContext ctx)
    {
        try {
            MultiMap params = ctx.queryParams();

            db
            .preparedQuery("SELECT * FROM public.trucks WHERE transporter_id = $1")
            .execute(Tuple.of(params.get("transporter_id")) , ar->{
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
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }

    public void getTruckDetails(RoutingContext ctx)
    {
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.trucks WHERE id=$1")
            .execute(Tuple.of(Integer.parseInt(params.get("id"))) , ar -> {
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

    public void addTruck(RoutingContext ctx)
    {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.trucks(name, type , truck_no ,driver_id , capacity , status , transporter_id) VALUES ($1 , $2 , $3 , $4 , $5 , $6 , $7)")
            .execute(
                Tuple.of(
                    req.getValue("name"),
                    req.getValue("type"),
                    req.getValue("truck_no"),
                    req.getValue("driver_id"),
                    req.getValue("capacity"),
                    req.getValue("status"),
                    req.getValue("transporter_id")
                 ), ar -> {
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
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }

    public void deleteTruck(RoutingContext ctx)
    {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.trucks WHERE id = $1")
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
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        } 
    }
    

}