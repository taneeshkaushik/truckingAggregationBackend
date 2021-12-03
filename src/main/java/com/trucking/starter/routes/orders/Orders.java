package com.trucking.starter.routes.orders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

import java.time.LocalDate;

import com.trucking.starter.utilities.Utils;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

public class Orders {

    private Router router;
    private SqlClient db;
    protected Utils obj;
    public Orders(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();

    }

    public void routeSetup()
    {

        router.get("/orders/transporter/getallorders").handler(this::gettransporterOrders);
        router.get("/orders/union/getallorders").handler(this::getUnionOrders);
        router.get("/orders/getorder").handler(this::getOrder);
        router.delete("/orders/cancelorder").handler(this::cancelOrder);
        router.post("/orders/createorder").handler(this::createOrder);
        router.post("/orders/editorder").handler(this::editOrder);
        router.get("/orders/getorderqueue").handler(this::getOrderQueue);
        router.post("/orders/searchorders").handler(this::searchOrders);
    }

    public void getUnionOrders(RoutingContext ctx){
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.orders WHERE union_id = $1")
            .execute(Tuple.of(params.get("id")) , ar->{
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                        .put("data" , obj.RowSet_To_List(ar.result()))

                    );

                }
                else {
                    ctx.json(
                        new JsonObject().put("success" , false)
                        .put("data" , "Some error happened")
                    );
                }

            });
            
        } catch (Exception e) {
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }

    public void gettransporterOrders(RoutingContext ctx) {
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.orders WHERE transporter_id = $1")
            .execute(Tuple.of(params.get("id")) , ar->{
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
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

    public void getOrder(RoutingContext ctx) {
        try{
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.orders WHERE id=$1")
            .execute(Tuple.of(params.get("id")) , ar->{
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
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
    
    public void cancelOrder(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.orders WHERE id = $1")
            .execute(Tuple.of(req.getValue("id") ), ar -> {
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
    
    public void createOrder(RoutingContext ctx) {

        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.orders(type, status , pickup_date , drop_date , transaction_id , weight, material , cost ,shipper_id , driver_id , truck_id , start_loc , end_loc , review ) VALUES ($1 , $2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14)")
            .execute(Tuple.of(
                req.getValue("type"),
                req.getValue("status"),
                LocalDate.parse(req.getString("pickup_date")),
                LocalDate.parse(req.getString("drop_date")),
                req.getValue("transaction_id"),
                req.getValue("weight"),
                req.getValue("material"),
                req.getValue("cost"),
                req.getValue("shipper_id"),
                req.getValue("driver_id"),
                req.getValue("truck_id"),
                req.getValue("start_loc"),
                req.getValue("end_loc"),
                req.getValue("review")
            
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
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }

    public void editOrder(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.orders SET"+
            "type=$1"+
            "status=$2"+
            "pickup_date=$3"+
            "drop_date=$4"+
            "transaction_id=$5"+
            "weight=$6"+
            "material=$7"+
            "cost=$8"+
            "shipper_id=$9"+
            "driver_id=$10"+
            "truck_id=$11"+
            "start_loc=$12"+
            "end_loc=$13"+
            "review=$14"+
            "WHERE  id = $")
            .execute(Tuple.of(
                req.getValue("type"),
                req.getValue("status"),
                LocalDate.parse(req.getString("pickup_date")),
                LocalDate.parse(req.getString("drop_date")),
                req.getValue("transaction_id"),
                req.getValue("weight"),
                req.getValue("material"),
                req.getValue("cost"),
                req.getValue("shipper_id"),
                req.getValue("driver_id"),
                req.getValue("truck_id"),
                req.getValue("start_loc"),
                req.getValue("end_loc"),
                req.getValue("review")
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
            //TODO: handle exception
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }

    public void getOrderQueue(RoutingContext ctx) {

    }

    public void searchOrders(RoutingContext ctx) {
        try {

            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("SELECT * FROM public.orders WHERE"+
             "column_name LIKE '%fox%' OR"+
             "column_name LIKE '%fox%' OR"+
             "column_name LIKE '%fox%' OR"+
             "column_name LIKE '%fox%' OR"

             )
            .execute(Tuple.of(req.getValue("id")) , ar -> {
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
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
}
