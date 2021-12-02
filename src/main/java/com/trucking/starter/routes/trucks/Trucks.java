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

    }

    public void getTruckDetails(RoutingContext ctx)
    {

    }

    public void addTruck(RoutingContext ctx)
    {

    }

    public void deleteTruck(RoutingContext ctx)
    {

    }

}