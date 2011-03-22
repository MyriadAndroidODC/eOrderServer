package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

public class DishCategoryTable {
    public static String TABEL_NAME = "dish_category";

    /**
     * The columns of this table.
     */
    public static final String DISH_ID                  = "dish_id";
    public static final String CATEGORY_ID              = "category_id";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABEL_NAME + " ( " +
                        DISH_ID + " INTEGER, " +
                        CATEGORY_ID + " INTEGER" +
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

    public static String getDishCategory(Connection conn) {
        String sqlStr = "SELECT * FROM " + TABEL_NAME;
        PreparedStatement state = null;
        JSONObject ret = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            state = conn.prepareStatement(sqlStr);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put(DISH_ID, rs.getInt(1));
                json.put(CATEGORY_ID, rs.getString(2));
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
