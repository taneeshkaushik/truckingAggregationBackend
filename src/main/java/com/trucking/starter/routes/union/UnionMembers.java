package com.trucking.starter.routes.union;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlClient;

import com.trucking.starter.utilities.Utils;

import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

public class UnionMembers {
    private Router router;
    private SqlClient db;
    private Utils obj;


    public UnionMembers(Router router, SqlClient db) {
        this.router = router;
        this.db = db;
        this.obj = new Utils();
    }

    public void routeSetup(){
        router.post("/union/getunionmembers").handler(this::fetchUnionMembers);
        router.post("/union/getunionmember").handler(this::fetchUnionMember);
        router.post("/union/updateunionmember").handler(this::updateUnionMember);
        router.post("/union/createunionmember").handler(this::createUnionMember);
        router.post("/union/deleteunionmember").handler(this::deleteUnionMember);
    }

    public void fetchUnionMembers(RoutingContext ctx) {
        try {
            db
            .query("SELECT * FROM public.union_members")
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
                        .put("data" , " Some error happened")
                    );
                }

            });
        
        } catch (Exception e) {
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }
    }

    public void fetchUnionMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("SELECT * FROM public.union_members WHERE id=$1")
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
                        .put("data" , " Some error happened")
                    );
                }

            });
        } catch (Exception e) {
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }

    public void updateUnionMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("UPDATE public.union_members SET union_id=$1 , name= $2 , designation=$3 , address=$4, joining_date=$5,end_date=$6, rating=$7  WHERE  id = $7")
            .execute(Tuple.of(
                req.getValue("union_id"),
                req.getValue("name"),
                req.getValue("designation") ,
                req.getValue("address"),
                req.getValue("joining_date"),
                req.getValue("end_date"),
                req.getValue("rating") 
                ), ar -> {
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
            ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
        }

    }
    
    public void createUnionMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("INSERT INTO public.union_members(union_id , name, date) VALUES ($1 , $2)")
            .execute(Tuple.of(
                req.getValue("union_id"),
                req.getValue("name"),
                req.getValue("date") ), ar -> {
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

    public void deleteUnionMember(RoutingContext ctx) {
        try {
            JsonObject req = ctx.getBodyAsJson();
            db
            .preparedQuery("DELETE FROM public.union_members WHERE id = $1")
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
