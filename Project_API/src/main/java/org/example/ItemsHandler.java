package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class ItemsHandler {
    private DatabaseKeren database;

    public ItemsHandler(DatabaseKeren database){
        this.database = database;
    }

    public String getProductsMethod(String[] path, String query){
        String response = "";
        if(path.length == 2){
            response = getItems(0, query);
        }else if(path.length == 3){
            response = getItems(Integer.parseInt(path[2]), query);
        }
        return response;
    }

    public String getItems(int productId, String addedQuery){
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

            querySQL = "SELECT * FROM items WHERE " + queryField + queryCondition + " " + queryValue + " ";
        } else {
            switch (productId){
                case 0 :
                    querySQL = "SELECT * FROM items";
                    break;
                default:
                    querySQL = "SELECT * FROM items WHERE id_items=" + productId;
                    break;
            }
        }
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()){
                JSONObject jsonUSer = new JSONObject();
                jsonUSer.put("id_items", resultSet.getInt("id_items"));
                jsonUSer.put("name", resultSet.getString("name"));
                jsonUSer.put("price", resultSet.getInt("price"));
                jsonUSer.put("type", resultSet.getString("type"));
                jsonUSer.put("is_active", resultSet.getInt("is_active"));
                jsonArray.put(jsonUSer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


        return jsonArray.toString();
    }
    public String postMethod(JSONObject requestBodyJson){
        String name = requestBodyJson.optString("name");
        int price = requestBodyJson.optInt("price");
        String type = requestBodyJson.optString("type");
        int is_active = requestBodyJson.optInt("is_active");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "INSERT INTO items(name,price,type,is_active) VALUES(?,?,?,?)";
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, price);
            statement.setString(3, type);
            statement.setInt(4, is_active);
            rowsAffected = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows inserted!";
    }
    public String deleteMethod(int itemsId){
        PreparedStatement statement = null;
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM items WHERE id_items=" + itemsId;
            statement = this.database.getConnection().prepareStatement(query);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowsAffected + " rows deleted!";
    }
    public String putMethod(String itemsId, JSONObject requestBodyJson){
        String name = requestBodyJson.optString("name");
        int price = requestBodyJson.optInt("price");
        String type = requestBodyJson.optString("type");
        int is_active = requestBodyJson.optInt("is_active");
        PreparedStatement statement = null;
        int rowsAffected = 0;

        String query = "UPDATE items SET name = ?, price = ?, type = ?, is_active = ? WHERE id_items=" + itemsId;
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, price);
            statement.setString(3, type);
            statement.setInt(4, is_active);
            rowsAffected = statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows updated!";
    }
}
