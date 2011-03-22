package com.androidodc.eorder.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

import com.androidodc.eorder.DataBaseHelper;

public class DiningTable {
    public static String TABLE_NAME = "dining_tables";

    /**
     * The columns of this table.
     */
    public static final String ID       = "_id";
    public static final String NAME     = "name";
    public static final String STATUS   = "status";
    public static final String CAPACITY = "capacity";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        NAME + " TEXT CHARACTER SET GB2312, " +
                        CAPACITY + " INTEGER, " +
                        STATUS + " INTEGER)";
        DataBaseHelper.createTable(conn, sqlStr);
    }

    public static String getDiningTable(Connection conn) {
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
                json.put(NAME, rs.getString(2));
                json.put(STATUS, rs.getInt(3));
                json.put(CAPACITY, rs.getInt(4));
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
