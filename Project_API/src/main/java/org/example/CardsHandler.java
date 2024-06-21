package org.example;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class CardsHandler {
    private DatabaseKeren database;

    public CardsHandler(DatabaseKeren database){
        this.database = database;
    }

    public String getCardsMethod(String[] path, String query){
        String response = "";
        if(path.length == 2){
            response = getCards(0, query);
        }else if(path.length == 3){
            response = getCards(Integer.parseInt(path[2]), query);
        }
        return response;
    }
    public String getCards(int CardsId, String addedQuery){
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
            querySQL = "SELECT * FROM Cards WHERE " + queryField + queryCondition + " " + queryValue + " ";
        } else {
            switch (CardsId){
                case 0 :
                    querySQL = "SELECT * FROM Cards";
                    break;
                default:
                    querySQL = "SELECT * FROM Cards WHERE id_Cards=" + CardsId;
                    break;
            }
        }
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()){
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
    public String postMethod(JSONObject requestBodyJson){
        int id_customers = requestBodyJson.optInt("id_customers");
        String card_type = requestBodyJson.optString("card_type");
        String masked_number = requestBodyJson.optString("masked_number");
        int expiry_month = requestBodyJson.optInt("expiry_month");
        int expiry_year = requestBodyJson.optInt("expiry_year");
        String status = requestBodyJson.optString("status");
        int is_primary = requestBodyJson.optInt("is_primary");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "INSERT INTO Cards(id_customers,card_type,masked_number,expiry_month,expiry_year,status,is_primary) VALUES(?,?,?,?,?,?,?)";
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_customers);
            statement.setString(2, card_type);
            statement.setString(3, masked_number);
            statement.setInt(4, expiry_month);
            statement.setInt(5, expiry_year);
            statement.setString(6, status);
            statement.setInt(7, is_primary);
            rowsAffected = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows inserted!";
    }
    public String deleteMethod(int cardsId){
        PreparedStatement statement = null;
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM cards WHERE id_cards=" + cardsId;
            statement = this.database.getConnection().prepareStatement(query);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowsAffected + " rows deleted!";
    }
    public String putMethod(String cardsId, JSONObject requestBodyJson){
        int id_customers = requestBodyJson.optInt("id_customers");
        String card_type = requestBodyJson.optString("card_type");
        String masked_number = requestBodyJson.optString("masked_number");
        int expiry_month = requestBodyJson.optInt("expiry_month");
        int expiry_year = requestBodyJson.optInt("expiry_year");
        String status = requestBodyJson.optString("status");
        int is_primary = requestBodyJson.optInt("is_primary");
        PreparedStatement statement = null;
        int rowsAffected = 0;
        String query = "UPDATE Cards SET id_customers = ?, card_type = ?, masked_number = ?, expiry_month = ?,expiry_year = ?,status = ?,is_primary =? WHERE id_cards=" + cardsId;
        try {
            statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id_customers);
            statement.setString(2, card_type);
            statement.setString(3, masked_number);
            statement.setInt(4, expiry_month);
            statement.setInt(5, expiry_year);
            statement.setString(6, status);
            statement.setInt(7, is_primary);

            rowsAffected = statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return rowsAffected + " rows updated!";
    }
}
