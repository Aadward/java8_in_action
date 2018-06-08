package com.syh.example.hive.jdbc;

import java.sql.SQLException;

/**
 * @author yuhangshen.
 * @create 2018/6/8
 */
public class Main {

  public static void main(String[] args) {
    try (HiveUtil hiveUtil = new HiveUtil("hostname", 10000, "default", "hive", "")) {
      String sql = "SELECT id, name FROM user LIMIT 1";
      hiveUtil.select(sql).forEach(row -> {
        for (Object o : row) {
          System.out.print(o + "\t");
        }
        System.out.println("");
      });
    } catch (HiveConnectException | SQLException e) {
      e.printStackTrace();
    }
  }
}