package com.androidodc.eorder.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

import com.androidodc.eorder.DataBaseHelper;

public class DishCategoryTable {
    public static String TABLE_NAME = "dish_category";

    /**
     * The columns of this table.
     */
    public static final String DISH_ID     = "dish_id";
    public static final String CATEGORY_ID = "category_id";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        DISH_ID + " INTEGER, " +
                        CATEGORY_ID + " INTEGER)";
        DataBaseHelper.createTable(conn, sqlStr);
    }

    public static String getDishCategory(Connection conn) {
        String sqlStr = "SELECT * FROM " + TABLE_NAME;
        PreparedStatement state = null;
        ResultSet rs = null;
        JSONObject ret = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            state = conn.prepareStatement(sqlStr);
            rs = state.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put(DISH_ID, rs.getInt(1));
                json.put(CATEGORY_ID, rs.getString(2));
                jArray.put(json);
            }
            if (jArray.length() > 0) {
                ret.put(TABLE_NAME, jArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            DataBaseHelper.closeResultSet(rs);
            DataBaseHelper.closeStatement(state);
        }
        return ret.toString();
    }
}
