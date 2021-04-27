package com.main;

  import com.operations.PreparedStatementOperation;
  import com.util.Months;
  import io.vertx.core.AbstractVerticle;
  import io.vertx.core.MultiMap;
  import io.vertx.core.Promise;
  import io.vertx.core.Vertx;
  import io.vertx.core.json.JsonArray;
  import io.vertx.core.json.JsonObject;
  import io.vertx.ext.web.Router;
  import io.vertx.jdbcclient.JDBCConnectOptions;
  import io.vertx.jdbcclient.JDBCPool;
  import io.vertx.sqlclient.PoolOptions;
  import io.vertx.ext.sql.SQLConnection;

public class MainVerticle extends AbstractVerticle {

  //TEST URL: http://localhost:8888/?sign=Aquarius
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    PreparedStatementOperation pso = new PreparedStatementOperation();
    String aquariusDates = pso.select("Aquarius");

    router.route().handler(context -> {
      String address = context.request().connection().remoteAddress().toString();
      MultiMap queryParams = context.queryParams();

      JsonArray days = new JsonArray();
      for (int i = 1; i <= Months.JAN.getMonthDays(); i++) {
        days.add(i);
      }

      context.json(
        new JsonObject()
          .put("sign", queryParams.get("sign"))
          .put("dates", aquariusDates)
          .put("January", days)
      );
    });

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess(server -> System.out.println("-----------------\nVertx test: HTTP server started on port " + server.actualPort()));

  }//start
}//Main Verticle
