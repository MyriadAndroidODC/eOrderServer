package com.androidodc.eorder.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

import com.androidodc.eorder.DataBaseHelper;

public class OrdersTable {
    public static String TABLE_NAME = "orders";

    /**
     * The columns of this table.
     */
    public static final String ID          = "_id";
    public static final String SUM         = "sum";
    public static final String CREATE_TIME = "create_time";
    public static final String STATUS      = "status";
    public static final String PAY_TIME    = "pay_time";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        SUM + " FLOAT DEFAULT NULL, " +
                        CREATE_TIME + " LONG, " +
                        STATUS + " INTEGER, " +
                        PAY_TIME + " LONG)";
        DataBaseHelper.createTable(conn, sqlStr);
    }

    public static String getOrders(Connection conn, Integer status) {
        String sqlStr = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + STATUS + "=" + status;
        return queryOrders(conn, sqlStr);
    }

    public static String getOrders(Connection conn) {
        String sqlStr = "SELECT * FROM " + TABLE_NAME;
        return queryOrders(conn, sqlStr);
    }

    private static String queryOrders(Connection conn, String sqlStr) {
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
                json.put(SUM, rs.getString(2));
                json.put(CREATE_TIME, rs.getLong(3));
                json.put(STATUS, rs.getInt(4));
                json.put(PAY_TIME, rs.getLong(5));
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

    public static long saveOrder(Connection conn, double sum) throws SQLException {
        long createTime = System.currentTimeMillis();
        String sqlStr = "INSERT INTO " + TABLE_NAME +
                        " VALUES (NULL, " + sum + ", " + createTime + ", 0, NULL)";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sqlStr);
            if (state.executeUpdate() > 0) {
                rs = state.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } finally {
            DataBaseHelper.closeResultSet(rs);
            DataBaseHelper.closeStatement(state);
        }
        return 0;
    }
}
