package org.example;

import org.example.DatabaseKeren;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class SubscriptionsHandler {
    private DatabaseKeren database;
    public SubscriptionsHandler(DatabaseKeren database){
        this.database = database;
    }

    public String getSubscriptionsMethod(String[] path, String query){
        String response = "";
        if(path.length == 2){
            response = getSubscriptions(0, query);
        }else if(path.length == 3){
            response = getSubscriptions(Integer.parseInt(path[2]), query);
        }
        return response;
    }

    public String getSubscriptions(int SubscriptionsId, String addedQuery){
        JSONArray jsonArray = new JSONArray();
        String querySQL = "";
        if(addedQuery != null) {
            String[] query = addedQuery.split("&");
            String queryField = "";
            String queryCondition = "";
            int queryValue = 0;
            for (int i = 0; i < query.length; i++) {
                if (query[i].contains("field")) {
                    queryField = query[i].substring(query[i].lastIndexOf("=") + 1);
                } else if (query[i].contains("val")) {
                    queryValue = Integer.parseInt(query[i].substring(query[i].lastIndexOf("=") + 1));
                } else if (query[i].contains("cond")) {
                    String cond = query[i].substring(query[i].lastIndexOf("=") + 1);
                    if (cond.equals("larger")) {
                        queryCondition = ">";
                    } else if (cond.equals("largerEqual")) {
                        queryCondition = ">=";
                    } else if (cond.equals("smaller")) {
                        queryCondition = "<";
                    } else if (cond.equals("smallerEqual")) {
                        queryCondition = "<=";
                    }
                }
            }

            querySQL = "SELECT * FROM Subscriptions WHERE " + queryField + queryCondition + " " + queryValue + " ";
        } else {
            switch (SubscriptionsId){
                case 0 :
                    querySQL = "SELECT * FROM Subscriptions";
                    break;
                default:
                    querySQL = "SELECT * FROM Subscriptions WHERE id_Subscriptions =" + SubscriptionsId;
                    break;
            }
        }
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()){
                JSONObject jsonUSer = new JSONObject();
                jsonUSer.put("id_Subscriptions", resultSet.getInt("id_Subscriptions"));
                jsonUSer.put("id_customers", resultSet.getInt("id_customers"));
                jsonUSer.put("billing_period", resultSet.getInt("billing_period"));
                jsonUSer.put("billing_period_unit", resultSet.getString("billing_period_unit"));
                jsonUSer.put("total_due", resultSet.getInt("total_due"));
                jsonUSer.put("activated_at", resultSet.getString("activated_at"));
                jsonUSer.put("current_term_start", resultSet.getString("current_term_start"));
                jsonUSer.put("current_term_end", resultSet.getString("current_term_end"));
                jsonUSer.put("status", resultSet.getString("status"));
                jsonArray.put(jsonUSer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return jsonArray.toString();
    }
    public String postMethod(JSONObject requestBodyJson){
        int id_customers = requestBodyJson.optInt("id_customers");
        int billing_period = requestBodyJson.optInt("billing_period");
        String billing_period_unit = requestBodyJson.optString("billing_period_unit");
        int total_due = requestBodyJson.optInt("total_due");
        String activated_at = requestBodyJson.optString("activated_at");
        String current_term_start = requestBodyJson.optString("current_term_start");
        String current_term_end = requestBodyJson.optString("current_term_end");
        String status = requestBodyJson.optString("status");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "INSERT INTO Subscriptions (id_customers,billing_period,billing_period_unit,total_due,activated_at,current_term_start,current_term_end,status) VALUES(?,?,?,?,?,?,?,?)";
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_customers);
            statement.setInt(2, billing_period);
            statement.setString(3, billing_period_unit);
            statement.setInt(4, total_due);
            statement.setString(5, activated_at);
            statement.setString(6, current_term_start);
            statement.setString(7, current_term_end);
            statement.setString(8,status);
            rowsAffected = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows inserted!";
    }
    public String deleteMethod(int subscriptionsId){
        PreparedStatement statement = null;
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM Subscriptions WHERE id_Subscriptions =" + subscriptionsId;
            statement = this.database.getConnection().prepareStatement(query);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowsAffected + " rows deleted!";
    }
    public String putMethod(String subscriptionsId, JSONObject requestBodyJson){
        int id_customers = requestBodyJson.optInt("id_customers");
        int billing_period = requestBodyJson.optInt("billing_period");
        String billing_period_unit = requestBodyJson.optString("billing_period_unit");
        int total_due = requestBodyJson.optInt("total_due");
        String activated_at = requestBodyJson.optString("activated_at");
        String current_term_start = requestBodyJson.optString("current_term_start");
        String current_term_end = requestBodyJson.optString("current_term_end");
        String status = requestBodyJson.optString("status ");
        PreparedStatement statement = null;
        int rowsAffected = 0;

        String query = "UPDATE Subscriptions  SET id_customers = ?, billing_period = ?, billing_period_unit = ?, total_due = ?,activated_at = ?,current_term_start= ?,current_term_end=  ? status = ? WHERE id_subscriptions=" + subscriptionsId;
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_customers);
            statement.setInt(2, billing_period);
            statement.setString(3, billing_period_unit);
            statement.setInt(4, total_due);
            statement.setString(5, activated_at);
            statement.setString(6, current_term_start);
            statement.setString(7, current_term_end);
            statement.setString(8,status);
            rowsAffected = statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows updated!";
    }
}

