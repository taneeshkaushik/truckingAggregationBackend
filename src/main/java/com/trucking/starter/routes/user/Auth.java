package com.trucking.starter.routes.user;

import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.*;
import java.util.*;

import com.trucking.starter.utilities.Utils;

import io.vertx.core.json.*;

public class Auth {

  private Router router;
  private SqlClient db;
  private JWTAuth jwt;
  private Utils obj;

  public Auth(Router router, SqlClient db, JWTAuth jwt_provider) {
    this.router = router;
    this.db = db;
    this.jwt = jwt_provider;
    this.obj = new Utils();
  }

  public void routeSetup() {

    router.post("/auth/isauth").handler(this::authToken);

    router.post("/auth/login").handler(this::login);

    router.post("/auth/logout").handler(this::logout);

    router.post("/auth/refresh").handler(this::refreshToken);

    router.post("/auth/register").handler(this::register);

  }

  public void refreshToken(RoutingContext ctx) {
    try {
      JsonObject req = ctx.getBodyAsJson();
      Object refresh_token = req.getValue("refresh_token");
      if (refresh_token == null) {
        ctx.json(
            new JsonObject().put("success", false).put("data", "Invalid Token"));
        ctx.fail(400);
      }

      db.preparedQuery("SELECT token from public.jwt WHERE token=$1")
          .execute(Tuple.of(refresh_token),
              ar -> {
                if (ar.result().rowCount() == 0) {
                  ctx.json(new JsonObject()
                      .put("success", false)
                      .put("data", "Token NOT FOUND"));

                } else {
                  // verify the token here and create a new access token
                  jwt.authenticate(new JsonObject().put("token", refresh_token))
                      .onSuccess(user -> {

                        // System.out.println("User: " + user.principal());

                        ctx.json(new JsonObject().put("data", user.principal()));

                      })
                      .onFailure(err -> {
                        // Failed!
                        ctx.json(
                            new JsonObject().put("success", false)
                                .put("data", "Invalid Token"));
                      });

                }

              });

    } catch (Exception e) {
      ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
    }

  }

  public void login(RoutingContext ctx) {

    try {

      JsonObject req = ctx.getBodyAsJson();
      // System.out.println(req);
      db.preparedQuery("SELECT * FROM public.user WHERE email = $1 AND password=$2")
          .execute(Tuple.of(req.getValue("email"), req.getValue("password")),
              ar -> {
                // System.out.println(ar);

                if (ar.result().rowCount() == 0) {
                  ctx.json(new JsonObject()
                      .put("success", false)
                      .put("data", "User not found"));

                } else {
                  RowSet<Row> res = ar.result();
                  List<JsonObject> data = obj.RowSet_To_List(res);
                  // create access & refresh token here
                  String access_token = jwt.generateToken(
                      req, new JWTOptions().setExpiresInMinutes(60));

                  String refresh_token = jwt.generateToken(req, new JWTOptions()
                      .setExpiresInMinutes(1440));

                  // insert the refresh token in jwt table
                  db
                      .preparedQuery("INSERT INTO public.jwt(token) VALUES ($1)")
                      .execute(Tuple.of(refresh_token),
                          ar1 -> {

                            if (ar1.succeeded()) {
                              ctx.json(
                                  new JsonObject().put("success", true)
                                      .put("access_token", access_token).put("refresh_token", refresh_token)
                                      .mergeIn(data.get(0))
                                      );

                            } else {
                              ctx.json(
                                  new JsonObject().put("success", false)
                                      .put("data", "Unable to store JWT token"));
                            }

                          });

                }

              });

    } catch (Exception e) {
      // TODO: handle exception
      ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
    }

  }

  private void register(RoutingContext ctx) {
    try {

      JsonObject req = ctx.getBodyAsJson();
      // System.out.println(req);

      db
          .preparedQuery("INSERT INTO public.user(username, password , email , account_type) VALUES ($1, $2 ,$3, $4) returning id")
          .execute(
              Tuple.of(req.getValue("username"), req.getValue("password"), req.getValue("email"),
                  req.getValue("account_type")),
              ar -> {
                if (ar.succeeded()) {
                  RowSet<Row> res = ar.result();
                  List<JsonObject> data = obj.RowSet_To_List(res);
                  // System.out.println(data);
                  ctx.json(new JsonObject().put("success", true ).put("data" , data));
                } else {
                  System.out.println(ar.cause().getMessage());
                  ctx.fail(402);
                }
              });

    } catch (Exception e) {
      ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));

    }
  }

  public void logout(RoutingContext ctx) {

    try {
      JsonObject req = ctx.getBodyAsJson();

      Object refresh_token = req.getValue("refresh_token");
      if (refresh_token == null) {
        ctx.json(
            new JsonObject().put("success", false).put("data", "Invalid Token"));
        ctx.fail(400);
      }

      db
          .preparedQuery("DELETE FROM public.jwt WHERE token= $1")
          .execute(Tuple.of(refresh_token),
              ar -> {
                if (ar.succeeded()) {
                  ctx.json(
                      new JsonObject().put("success", true));
                } else {
                  ctx.fail(402);
                }
              });

    } catch (Exception e) {
      // TODO: handle exception
      ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
    }
  }

  public void authToken(RoutingContext ctx) {
    try {
      String authHeader = ctx.request().getHeader("authorization");
      System.out.println(authHeader);
      String token = authHeader != null ? authHeader.split(" ")[1] : null;

      if (token == null) {
        ctx.json(
            new JsonObject().put("success", false).put("data", "Invalid Token or Auth Header"));
        ctx.fail(400);
      }

      jwt.authenticate(new JsonObject().put("token", token))
          .onSuccess(user -> {

            System.out.println("User: " + user.principal());
            ctx.json(new JsonObject().put("success", true));

          })
          .onFailure(err -> {
            // Failed!
            ctx.json(
                new JsonObject().put("success", false)
                    .put("data", "Invalid Token"));
          });

    } catch (Exception e) {
      ctx.json(new JsonObject().put("success", false).put("data", e.getMessage()));
    }
  }

}