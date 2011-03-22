package com.androidodc.eorder.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

import com.androidodc.eorder.DataBaseHelper;

public class DishesTable {
    public static String TABLE_NAME = "dishes";

    /**
     * The columns of this table.
     */
    public static final String ID          = "_id";
    public static final String IMAGE_URL   = "image_url";
    public static final String PRICE       = "price";
    public static final String NAME        = "name";
    public static final String DESCRIPTION = "description";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        IMAGE_URL + " TEXT, " +
                        PRICE + " FLOAT DEFAULT NULL, " +
                        NAME  + " TEXT CHARACTER SET GB2312, " +
                        DESCRIPTION + " TEXT CHARACTER SET GB2312, " +
                        CREATE_TIME + " LONG, " + 
                        UPDATE_TIME + " LONG)";
        DataBaseHelper.createTable(conn, sqlStr);
    }

    public static String getDishes(Connection conn) {
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
