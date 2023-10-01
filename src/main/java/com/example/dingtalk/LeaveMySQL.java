package com.example.dingtalk;

import java.sql.SQLException;

import com.alibaba.fastjson.JSONObject;
import com.example.dingtalk.AbstractMySQLConnector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.alibaba.fastjson.JSONObject;
import com.example.dingtalk.Constant;

public class LeaveMySQL extends AbstractMySQLConnector{

    @Override
    public void ensureTableExists(String eventType) {

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +eventType+ " (" +
                "EVENT_TYPE VARCHAR(255)," +
                "TIMESTAMP BIGINT," +
                "TITLE VARCHAR(255)," +
                "STAFF_ID VARCHAR(255)" +
                ");";

        try (Connection con = connect();
             Statement stmt = con.createStatement()) {
            
            stmt.execute(createTableSQL);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void insertOrUpdateData(JSONObject jsonData, String eventType) {
        String insertQuery = "INSERT INTO " + eventType + " (EVENT_TYPE, TIMESTAMP, TITLE, STAFF_ID) VALUES (?, ?, ?, ?)";
    
        try (Connection con = connect();
             PreparedStatement psInsert = con.prepareStatement(insertQuery)) {
            
            psInsert.setString(1, eventType);
            psInsert.setLong(2, jsonData.getLong("createTime"));
            psInsert.setString(3, jsonData.getString("title"));
            psInsert.setString(4, jsonData.getString("staffId"));
            
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
