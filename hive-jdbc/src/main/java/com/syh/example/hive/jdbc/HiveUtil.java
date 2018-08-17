package com.syh.example.hive.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuhangshen.
 * @create 2018/6/8
 */

public class HiveUtil implements AutoCloseable {

  private static String driverName = "org.apache.hive.jdbc.HiveDriver";

  private static final String CONNECTION_URL_FORMAT =
      "jdbc:hive2://%s:%d/%s";

  private String host;
  private Integer port;
  private String database;
  private String user;
  private String password;

  private Connection conn;

  public HiveUtil(String host, Integer port, String database, String user, String password)
      throws HiveConnectException {
    this.host = host;
    this.port = port;
    this.database = database;
    this.user = user;
    this.password = password;

    this.conn = connect();
  }

  private Connection connect() throws HiveConnectException {
    try {
      Class.forName(driverName);
      String connectionUrl = String.format(CONNECTION_URL_FORMAT, host, port, database);
      return DriverManager
          .getConnection(connectionUrl, user, password);
    } catch (ClassNotFoundException | SQLException e) {
      throw new HiveConnectException("Connect to hive server fail.", e);
    }
  }

  @Override
  public void close() {
    if (this.conn != null) {
      try {
        conn.close();
        conn = null;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public List<Object[]> select(String query) throws SQLException {
    try (Statement statement = conn.createStatement()) {
      ResultSet res = statement.executeQuery(query);
      int columnSize = res.getMetaData().getColumnCount();
      List<Object[]> ret = new ArrayList<>();
      while (res.next()) {
        Object[] results = new Object[columnSize];
        for (int i = 0; i < results.length; i++) {
          results[i] = res.getObject(i + 1);
        }
        ret.add(results);
      }
      return ret;
    }
  }
}