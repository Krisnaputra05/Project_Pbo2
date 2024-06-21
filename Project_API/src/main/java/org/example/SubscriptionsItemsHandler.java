package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class SubscriptionsItemsHandler {
    private DatabaseKeren database;
    public SubscriptionsItemsHandler(DatabaseKeren database){
        this.database = database;
    }
    public String getSubscriptionsItemsMethod(String[] path, String query){
        String response = "";
        if(path.length == 2){
            response = getSubscriptionsItems(0, query);
        }else if(path.length == 3){
            response = getSubscriptionsItems(Integer.parseInt(path[2]), query);
        }
        return response;
    }

    public String getSubscriptionsItems(int subscriptionItemsId, String addedQuery){
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

            querySQL = "SELECT * FROM subscriptions_items WHERE " + queryField + queryCondition + " " + queryValue + " ";
        } else {
            switch (subscriptionItemsId){
                case 0 :
                    querySQL = "SELECT * FROM subscriptions_items";
                    break;
                default:
                    querySQL = "SELECT * FROM subscriptions_items WHERE id_subcriptions_items =" + subscriptionItemsId;
                    break;
            }
        }
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()){
                JSONObject jsonUSer = new JSONObject();
                jsonUSer.put("id_subcriptions_items", resultSet.getInt("id_subcriptions_items"));
                jsonUSer.put("id_subscriptions", resultSet.getInt("id_subscriptions"));
                jsonUSer.put("id_items", resultSet.getInt("id_items"));
                jsonUSer.put("quantity", resultSet.getInt("quantity"));
                jsonUSer.put("price", resultSet.getInt("price"));
                jsonUSer.put("amount", resultSet.getInt("amount"));
                jsonArray.put(jsonUSer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
    public String postMethod(JSONObject requestBodyJson) {
        int id_subscriptions = requestBodyJson.optInt("id_subscriptions");
        int id_items = requestBodyJson.optInt("id_items");
        int quantity = requestBodyJson.optInt("quantity");
        int price = requestBodyJson.optInt("price");
        int amount = requestBodyJson.optInt("amount");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "INSERT INTO subscriptions_items(id_subscriptions,id_items,quantity,price,amount) VALUES(?,?,?,?,?)";
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_subscriptions);
            statement.setInt(2, id_items);
            statement.setInt(3, quantity);
            statement.setInt(4, price);
            statement.setInt(5, amount);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected + " rows inserted!";
    }
    public String deleteMethod(int subscriptionsItemsId){
        PreparedStatement statement = null;
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM subscriptions_items WHERE id_subcriptions_items =" + subscriptionsItemsId;
            statement = this.database.getConnection().prepareStatement(query);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowsAffected + " rows deleted!";
    }
    public String putMethod(String SubscriptionsItemsId, JSONObject requestBodyJson){
        int id_subscriptions = requestBodyJson.optInt("id_subscriptions");
        int id_items = requestBodyJson.optInt("id_items");
        int quantity = requestBodyJson.optInt("quantity");
        int price = requestBodyJson.optInt("price");
        int amount = requestBodyJson.optInt("amount");
        PreparedStatement statement = null;
        int rowsAffected = 0;

        String query = "UPDATE subscriptions_items  SET id_subscriptions = ?, id_items = ?, quantity = ?, price = ?,amount = ? WHERE id_shipping=" + SubscriptionsItemsId;
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_subscriptions);
            statement.setInt(2, id_items);
            statement.setInt(3, quantity);
            statement.setInt(4, price);
            statement.setInt(5, amount);
            rowsAffected = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows updated!";
    }
}

