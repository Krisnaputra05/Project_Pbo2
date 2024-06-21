package org.example;

import org.example.DatabaseKeren;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class ShippingAddresses {
    private DatabaseKeren database;
    public ShippingAddresses(DatabaseKeren database){
        this.database = database;
    }

    public String getShippingAddressesMethod(String[] path, String query){
        String response = "";
        if(path.length == 2){
            response = getShippingAdresses(0, query);
        }else if(path.length == 3){
            response = getShippingAdresses(Integer.parseInt(path[2]), query);
        }
        return response;
    }

    public String getShippingAdresses(int ShippingAddressesId, String addedQuery){
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

            querySQL = "SELECT * FROM shipping_addresses WHERE " + queryField + queryCondition + " " + queryValue + " ";
        } else {
            switch (ShippingAddressesId){
                case 0 :
                    querySQL = "SELECT * FROM shipping_addresses";
                    break;
                default:
                    querySQL = "SELECT * FROM shipping_addresses WHERE id_shipping =" + ShippingAddressesId;
                    break;
            }
        }
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()){
                JSONObject jsonUSer = new JSONObject();
                jsonUSer.put("id_shipping", resultSet.getInt("id_shipping"));
                jsonUSer.put("id_customers", resultSet.getInt("id_customers"));
                jsonUSer.put("title", resultSet.getString("title"));
                jsonUSer.put("line1", resultSet.getString("line1"));
                jsonUSer.put("line2", resultSet.getString("line2"));
                jsonUSer.put("city", resultSet.getString("city"));
                jsonUSer.put("province", resultSet.getString("province"));
                jsonUSer.put("postcode", resultSet.getString("postcode"));
                jsonArray.put(jsonUSer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return jsonArray.toString();
    }
    public String postMethod(JSONObject requestBodyJson){
        int id_customers = requestBodyJson.optInt("id_customers");
        String title = requestBodyJson.optString("title");
        String line1 = requestBodyJson.optString("line1");
        String line2 = requestBodyJson.optString("line2");
        String city = requestBodyJson.optString("city");
        String province = requestBodyJson.optString("province");
        String postcode = requestBodyJson.optString("postcode ");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "INSERT INTO shipping_addresses(id_customers,title,line1,line2,city,province,postcode) VALUES(?,?,?,?,?,?,?)";
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_customers);
            statement.setString(2, title);
            statement.setString(3, line1);
            statement.setString(4, line2);
            statement.setString(5, city);
            statement.setString(6, province);
            statement.setString(7, postcode);
            rowsAffected = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows inserted!";
    }
    public String deleteMethod(int shippingId){
        PreparedStatement statement = null;
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM shipping_addresses WHERE id_shipping =" + shippingId;
            statement = this.database.getConnection().prepareStatement(query);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowsAffected + " rows deleted!";
    }
    public String putMethod(String ShippingId, JSONObject requestBodyJson){
        int id_customers = requestBodyJson.optInt("id_customers");
        String title = requestBodyJson.optString("title");
        String line1 = requestBodyJson.optString("line1");
        String line2 = requestBodyJson.optString("line2");
        String city = requestBodyJson.optString("city");
        String province = requestBodyJson.optString("province");
        String postcode = requestBodyJson.optString("postcode ");
        PreparedStatement statement = null;
        int rowsAffected = 0;

        String query = "UPDATE shipping_addresses  SET id_customers = ?, title = ?, line1 = ?, line2 = ?,city = ?,province= ?,postcode=  ? WHERE id_shipping=" + ShippingId;
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_customers);
            statement.setString(2, title);
            statement.setString(3, line1);
            statement.setString(4, line2);
            statement.setString(5, city);
            statement.setString(6, province);
            statement.setString(7, postcode);
            rowsAffected = statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows updated!";
    }
}

