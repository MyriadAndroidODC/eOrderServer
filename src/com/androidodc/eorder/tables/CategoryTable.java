package com.androidodc.eorder.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

import com.androidodc.eorder.DataBaseHelper;

public class CategoryTable {
    public static String TABLE_NAME = "categories";

    /**
     * The columns of this table.
     */
    public static final String ID         = "_id";
    public static final String NAME       = "name";
    public static final String SORT_ORDER = "sort_order";

    public static void create(Connection conn) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                        NAME + " TEXT CHARACTER SET GB2312, " +
                        SORT_ORDER + " INTEGER)";
        DataBaseHelper.createTable(conn, sqlStr);
    }

    public static String getCategory(Connection conn) {
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
                json.put(SORT_ORDER, rs.getInt(3));
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
