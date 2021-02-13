package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class MainDAO {
    private String url = "jdbc:mysql://localhost:3307/shopify";
    private String user="root";
    private String password="root";
    public Connection conn;
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.print("In MainDao : " + e);
        }
    }
}
