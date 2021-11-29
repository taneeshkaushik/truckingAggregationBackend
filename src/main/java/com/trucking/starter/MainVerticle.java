package com.trucking.starter;

import com.trucking.starter.routes.orders.Orders;
import com.trucking.starter.routes.transporter.Transporter;
import com.trucking.starter.routes.transporter.TransporterFilter;
import com.trucking.starter.routes.transporter.TransporterMember;
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
    
    router.route().handler(BodyHandler.create());
    router.route().handler(CorsHandler.create(".*." ));

    // router.route("/auth/*").handler(JWTAuthHandler.create(jwt_provider));

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
    // To-do

    // Orders
    Orders orders = new Orders(router, db);
    orders.routeSetup();

    // Messages
    // To-do
    // starting the server
    // int port = System.getenv('PORT') as int;
    server.requestHandler(router).listen( Integer.parseInt(System.getenv("PORT")) , http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      }); 
    
  }
}
