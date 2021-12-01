package com.trucking.starter.routes.transporter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;

import java.time.LocalDate;

import com.trucking.starter.utilities.Utils;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

public class TransporterMember {
    private Router router;
    private SqlClient db;
    private Utils obj;

    public TransporterMember(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }

    public void routeSetup(){
        router.get("/transporter/gettransportermembers").handler(this::fetchtransporterMembers);
        router.get("/transporter/gettransportermember").handler(this::fetchtransporterMember);
        router.post("/transporter/updatetransportermember").handler(this::updatetransporterMember);
        router.post("/transporter/createtransportermember").handler(this::createtransporterMember);
        router.delete("/transporter/deletetransportermember").handler(this::deletetransporterMember);
    }

    public void fetchtransporterMembers(RoutingContext ctx) {
        try {

            db
            .query("SELECT * FROM public.transporter_members")
            .execute(ar->{
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

    public void fetchtransporterMember(RoutingContext ctx) {
        try {
            MultiMap params = ctx.queryParams();
            db
            .preparedQuery("SELECT * FROM public.transporter_members WHERE id=$1")
            .execute(Tuple.of(params.get("id")) , ar -> {
                if(ar.succeeded()){
                    ctx.json(
                        new JsonObject()
                        .put("success" ,  true)
                        .put("data" ,obj.RowSet_To_List(ar.result()))
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

    public void updatetransporterMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.transporter_members SET  name= $1 , designation=$2 , address=$3, joining_date=$4,end_date=$5, rating=$6  WHERE  id = $7")
            .execute(Tuple.of(
                    req.getValue("name"),
                    req.getValue("designation") ,
                    req.getValue("address"),
                    LocalDate.parse((req.getString("joining_date"))),
                    LocalDate.parse((req.getString("end_date"))),
                    req.getValue("rating"),
                    req.getValue("id") ), ar -> {
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
    
    public void createtransporterMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.transporter_members(transporter_id , name, joining_date) VALUES ($1 , $2 , $3)")
            .execute(Tuple.of(
                req.getValue("transporter_id"),
                req.getValue("name"),
                LocalDate.parse((req.getString("joining_date")))
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

    public void deletetransporterMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.transporter_members WHERE id = $1")
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
    
}
