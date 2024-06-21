package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseKeren {
    private Connection connection;

    public DatabaseKeren(){
        try {
            this.connection = null;
            String rootPath = System.getProperty("user.dir");
            String url = "jdbc:sqlite:" + rootPath + "/DatabaseKeren.db";
            this.connection = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}