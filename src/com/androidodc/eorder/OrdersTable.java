package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

public class OrdersTable {
    public static String TABEL_NAME = "orders";

    public static final String ID                  = "_id";
    public static final String SUM                 = "sum";
    public static final String CREATE_TIME         = "create_time";
    public static final String STATUS              = "status";
    public static final String PAY_TIME            = "pay_time";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABEL_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        SUM + " FLOAT DEFAULT NULL, " +
                        CREATE_TIME + " LONG, " +
                        STATUS + " INTEGER, " +
                        PAY_TIME + " LONG" +
                        " )";
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

    public static String getOrders(Connection conn, Integer status) {
        String sqlStr = "SELECT * FROM " + TABEL_NAME +
                        " WHERE " + STATUS + "=" + status;
        return queryOrders(conn, sqlStr);
    }

    public static String getOrders(Connection conn) {
        String sqlStr = "SELECT * FROM " + TABEL_NAME;
        return queryOrders(conn, sqlStr);
    }

    private static String queryOrders(Connection conn, String sqlStr) {
        PreparedStatement state = null;
        JSONObject ret = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            state = conn.prepareStatement(sqlStr);
            ResultSet rs = state.executeQuery();
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

    public static long saveOrder(Connection conn, double sum) throws SQLException {
        long createTime = System.currentTimeMillis();
        String sqlStr = "INSERT INTO " + TABEL_NAME +
            " VALUES(NULL, " + sum + ", " + createTime + ", 0, NULL)";
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
            try {
                if (rs != null) {
                    rs.close();
                }
                if (state != null) {
                    state.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
