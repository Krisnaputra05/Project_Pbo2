package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class CustomersHandler {
    private DatabaseKeren database;
    public CustomersHandler(DatabaseKeren database){
        this.database = database;
    }

    public String getCustomersMethod(String[] path, String query) {
        String response = "";
        if (path.length == 2) {
            response = getCustomers(0, query);
        } else if (path.length == 3) {
            response = getCustomers(Integer.parseInt(path[2]), query);
        } else if (path.length == 4) {
            if (path[3].equals("cards")) {
                response = getCustomersCards(Integer.parseInt(path[2]));
            } else if (path[3].equals("subscriptions")) {
                response = getCustomersSubscriptions(Integer.parseInt(path[2]));
//            } else if (path[3].equals("reviews")) {
//                response = getCustomersC(path[2]);
            }
        }
        return response;
    }
    public String getCustomersCards(int customersId ) {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT * FROM cards WHERE id_customers =" + customersId;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                JSONObject jsonUSer = new JSONObject();
                jsonUSer.put("id_cards", resultSet.getInt("id_cards"));
                jsonUSer.put("id_customers", resultSet.getInt("id_customers"));
                jsonUSer.put("card_type", resultSet.getString("card_type"));
                jsonUSer.put("expiry_month", resultSet.getInt("expiry_month"));
                jsonUSer.put("expiry_year", resultSet.getInt("expiry_year"));
                jsonUSer.put("status", resultSet.getString("status"));
                jsonUSer.put("is_primary", resultSet.getInt("is_primary"));
                jsonArray.put(jsonUSer);
            }
    }catch (SQLException e){
        e.printStackTrace();
    }
        return jsonArray.toString();
    }
    public String getCustomersSubscriptions(int customersId ) {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT * FROM subscriptions WHERE id_customers=" + customersId;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
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
    public String getCustomers(int customersId, String addedQuery){
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

            querySQL = "SELECT * FROM customers WHERE " + queryField + queryCondition + " " + queryValue + " ";
        } else {
            switch (customersId){
                case 0 :
                    querySQL = "SELECT * FROM customers";
                    break;
                default:
                    querySQL = "SELECT * FROM customers WHERE id_customers=" + customersId;
                    break;
            }
        }
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()){
                JSONObject jsonUSer = new JSONObject();
                jsonUSer.put("id_customers", resultSet.getInt("id_customers"));
                jsonUSer.put("email", resultSet.getString("email"));
                jsonUSer.put("first_name", resultSet.getString("first_name"));
                jsonUSer.put("last_name", resultSet.getString("last_name"));
                jsonUSer.put("phone_number", resultSet.getString("phone_number"));
                jsonArray.put(jsonUSer);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return jsonArray.toString();
    }
    public String postMethod(JSONObject requestBodyJson){
        String email = requestBodyJson.optString("email");
        String first_name = requestBodyJson.optString("first_name");
        String last_name = requestBodyJson.optString("last_name");
        String phone_number = requestBodyJson.optString("phone_number");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "INSERT INTO customers(email,first_name,last_name,phone_number) VALUES(?,?,?,?)";
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, first_name);
            statement.setString(3, last_name);
            statement.setString(4,phone_number);
            rowsAffected = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows inserted!";
    }
    public String deleteMethod(int customersId){
        PreparedStatement statement = null;
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM customers WHERE id_customers=" + customersId;
            statement = this.database.getConnection().prepareStatement(query);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowsAffected + " rows deleted!";
    }
    public String putMethod(String customersId, JSONObject requestBodyJson){
        String email = requestBodyJson.optString("email");
        String first_name = requestBodyJson.optString("first_name");
        String last_name = requestBodyJson.optString("last_name");
        String phone_number = requestBodyJson.optString("phone_number");
        PreparedStatement statement = null;
        int rowsAffected = 0;

        String query = "UPDATE customers SET email = ?, first_name = ?, last_name = ?, phone_number = ? WHERE id_customers=" + customersId;
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, first_name);
            statement.setString(3, last_name);
            statement.setString(4,phone_number);
            rowsAffected = statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows updated!";
    }
}
