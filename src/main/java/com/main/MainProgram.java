package com.main;

import com.operations.PreparedStatementOperation;
import io.vertx.core.Vertx;
import com.util.DBConnection;

import java.sql.SQLException;

public class MainProgram {
  public static void main(String[] args) throws SQLException {

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());

    DBConnection dbc = new DBConnection();
    System.out.println(dbc.getConnection());

    /*JDBCPool pool = JDBCPool.pool(vertx,
        new JDBCConnectOptions()
          .setJdbcUrl("jdbc:mysql://localhost:3306")
          .setUser("root")
          .setPassword("guevara88"),
        new PoolOptions()
          .setMaxSize(16)
      );

    pool
      .query("SELECT * FROM vertx.horoscope")
      .execute()
      .onFailure(e -> {
        System.out.println("Error: " + e);
      })
      .onSuccess(rows -> {
        for (Row row : rows) {
          System.out.println(row.getString("sign"));
        }
      });*/

  }//psvm
}//MainProgram
