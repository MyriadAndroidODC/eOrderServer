package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.util.json.JSONObject;

public class DataBaseHelper {
    private static Connection sConnection;
    private static boolean sInitialized;

    public synchronized static void getConnection() {
        InitialContext initCtx;
        DataSource ds = null;
        try {
            initCtx = new InitialContext();
            ds = (DataSource) initCtx.lookup("java:/eOrderDS");
            sConnection = ds.getConnection();
            if (!sInitialized) {
                init();
            }
        } catch (NamingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public synchronized static void init() {
        sInitialized = true;
        CategoryTable.create(sConnection);
        DiningTable.create(sConnection);
    }
    public static String getCategory() {
        String ret = null;
        getConnection();
        ret = CategoryTable.getCategory(sConnection);
        closeConnection();
        return ret;
    }

    public static String getDiningTable() {
        String ret = null;
        getConnection();
        ret = DiningTable.getDiningTable(sConnection);
        closeConnection();
        return ret;
    }

    public static void closeConnection() {
        if (sConnection != null) {
            try {
                sConnection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
