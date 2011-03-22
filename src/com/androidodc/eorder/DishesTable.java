package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

public class DishesTable {

    public static String TABEL_NAME = "dishes";

    /**
     * The columns of this table.
     */
    public static final String ID               = "_id";
    public static final String IMAGE_URL        = "image_url";
    public static final String PRICE            = "price";
    public static final String NAME             = "name";
    public static final String DESCRIPTION      = "description";
    public static final String CREATE_TIME      = "create_time";
    public static final String UPDATE_TIME      = "update_time";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABEL_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        IMAGE_URL + " TEXT, " +
                        PRICE + " FLOAT DEFAULT NULL, " +
                        NAME  + " TEXT CHARACTER SET gb2312, " +
                        DESCRIPTION + " TEXT CHARACTER SET gb2312, " +
                        CREATE_TIME + " LONG, " + 
                        UPDATE_TIME + " LONG" +
                        ")";
        PreparedStatement state = null;
        try {
            state = conn.prepareStatement(sqlStr);
            state.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getDishes(Connection conn) {
        String sqlStr = "SELECT * FROM " + TABEL_NAME;
        PreparedStatement state = null;
        JSONObject ret = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            state = conn.prepareStatement(sqlStr);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put(ID, rs.getInt(1));
                json.put(IMAGE_URL, rs.getString(2));
                json.put(PRICE, rs.getFloat(3));
                json.put(NAME, rs.getString(4));
                json.put(DESCRIPTION, rs.getString(5));
                json.put(CREATE_TIME, rs.getLong(6));
                json.put(UPDATE_TIME, rs.getLong(7));
                jArray.put(json);
            }
            if (jArray.length() > 0) {
                ret.put(TABEL_NAME, jArray);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret.toString();
    }
}
