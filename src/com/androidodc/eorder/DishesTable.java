package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

public class DishesTable {
    public static String TABEL_NAME = "dinning_tables";

    /**
     * The columns of this table.
     */
    public static final String ID                  = "_id";
    public static final String NAME                 = "name";
    public static final String SORT_ORDER           = "sort_order";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE " + TABEL_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        NAME + " text CHARACTER SET gb2312, " +
                        SORT_ORDER + " INTEGER"
                        + ")";
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

    public static String getDish(Connection conn) {
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
                json.put(NAME, rs.getString(2));
                json.put(SORT_ORDER, rs.getInt(3));
                jArray.put(json);
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
