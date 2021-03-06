package com.trucking.starter;

import com.trucking.starter.routes.orders.Orders;
import com.trucking.starter.routes.shipper.Shipper;
import com.trucking.starter.routes.transporter.Transporter;
import com.trucking.starter.routes.transporter.TransporterFilter;
import com.trucking.starter.routes.transporter.TransporterMember;
import com.trucking.starter.routes.trucks.Trucks;
import com.trucking.starter.routes.union.Union;
import com.trucking.starter.routes.union.UnionFilter;
import com.trucking.starter.routes.union.UnionMembers;
import com.trucking.starter.routes.user.Auth;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.core.http.HttpServer;
import io.vertx.sqlclient.SqlClient;
import io.vertx.ext.auth.jwt.*;
import io.vertx.ext.auth.KeyStoreOptions;
import com.trucking.starter.utilities.*;
import io.vertx.ext.auth.PubSecKeyOptions;
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();
    JWTAuthOptions config = new JWTAuthOptions();

    JWTAuth jwt_provider = JWTAuth.create(vertx, config);

    Router router = Router.router(vertx);
    
    router.route().handler(CorsHandler.create(".*.")
    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
    .allowCredentials(true)
    .allowedHeader("Access-Control-Allow-Headers")
    .allowedHeader("Authorization")
    .allowedHeader("Access-Control-Allow-Method")
    .allowedHeader("Access-Control-Allow-Origin")
    .allowedHeader("Access-Control-Allow-Credentials")
    .allowedHeader("Content-Type"));

    router.route().handler(BodyHandler.create());

    // router.route("/orders/*").handler(JWTAuthHandler.create(jwt_provider));
    // router.route("/shipper/*").handler(JWTAuthHandler.create(jwt_provider));
    // router.route("/union/*").handler(JWTAuthHandler.create(jwt_provider));
    // router.route("/transporter/*").handler(JWTAuthHandler.create(jwt_provider));

    PgClient dbObj  = new PgClient(vertx);
    SqlClient db =  dbObj.getSqlClient();

    // routes
    Auth auth = new Auth(router , db , jwt_provider);
    auth.routeSetup();
    // TRANSPORTER
    Transporter transporter = new Transporter(router, db);
    transporter.routeSetup();
    TransporterFilter tfilter = new TransporterFilter(router, db);
    tfilter.routeSetup();
    TransporterMember tMember  = new TransporterMember(router, db);
    tMember.routeSetup();
    // UNION
    Union union = new Union(router , db);
    union.routeSetup();
    UnionFilter ufilter = new UnionFilter(router, db);
    ufilter.routeSetup();
    UnionMembers uMembers = new UnionMembers(router, db);
    uMembers.routeSetup();
    //  SHIPPER
    Shipper shipper = new Shipper(router, db);
    shipper.routeSetup();

    // Orders
    Orders orders = new Orders(router, db);
    orders.routeSetup();

    Trucks trucks = new Trucks(router, db);
    trucks.routeSetup();
    // Messages
    // To-do
    // starting the server
    server.requestHandler(router).listen( Integer.parseInt(System.getenv("PORT")), http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port ");
        } else {
          startPromise.fail(http.cause());
        }
      }); 
    
  }
}
