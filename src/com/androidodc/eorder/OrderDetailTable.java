package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

public class OrderDetailTable {
    public static String TABEL_NAME = "order_detail";

    public static final String ID                  = "_id";
    public static final String DISH_ID             = "dish_id";
    public static final String DINING_TABLE_ID     = "dining_table_id";
    public static final String ORDER_ID            = "order_id";
    public static final String NUMBER              = "number";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABEL_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        DISH_ID + " INTEGER, " +
                        DINING_TABLE_ID + " INTEGER, " +
                        ORDER_ID + " INTEGER, " +
                        NUMBER + " INTEGER" +
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

    public static String getOrderDetail(Connection conn) {
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
                json.put(DISH_ID, rs.getString(2));
                json.put(DINING_TABLE_ID, rs.getInt(3));
                json.put(ORDER_ID, rs.getInt(4));
                json.put(NUMBER, rs.getInt(5));
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

    /**
     * orderDetailsArray format:
     * <table_id>_<dish_id>_<number>, <table_id>_<dish_id>_<number>, ...
     */
    public static int saveOrderDetail(Connection conn, long orderId, JSONArray orderDetails) throws SQLException, JSONException {
        int rowCount = 0;
        StringBuffer values = new StringBuffer();
        for (int i = 0; i < orderDetails.length(); i++) {
            JSONObject detail = orderDetails.getJSONObject(i);
            values.append("(NULL, ")
            .append(detail.getLong(OrderDetailTable.DISH_ID)).append(", ")
            .append(detail.getLong(OrderDetailTable.DINING_TABLE_ID)).append(", ")
            .append(orderId).append(", ")
            .append(detail.getInt(OrderDetailTable.NUMBER)).append("),");
        }
        values.deleteCharAt(values.length() - 1);
        String sqlStr = "INSERT INTO " + TABEL_NAME + " VALUES " + values;

        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sqlStr);
            rowCount = state.executeUpdate();
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
        return rowCount;
    }
}
