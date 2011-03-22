package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

public class DataBaseHelper {
    private static String SUCCESS = "success";
    private static String ERROR = "error";

    public synchronized static Connection getConnection() {
        InitialContext initCtx = null;
        DataSource ds = null;
        Connection conn = null;
        try {
            initCtx = new InitialContext();
            ds = (DataSource) initCtx.lookup("java:/eOrderDS");
            conn = ds.getConnection();
            createTables(conn);
        } catch (NamingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    public synchronized static void createTables(Connection conn) {
        CategoryTable.create(conn);
        DiningTable.create(conn);
        OrdersTable.create(conn);
        DishesTable.create(conn);
        DishCategoryTable.create(conn);
        OrderDetailTable.create(conn);
    }
    public static String getCategory() {
        Connection conn = getConnection();
        String ret = null;
        ret = CategoryTable.getCategory(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getOrders(HttpServletRequest request) {
        Connection conn = getConnection();
        String ret = null;
        String status = request.getParameter(OrdersTable.STATUS);
        if (status != null) {
            try {
                ret = OrdersTable.getOrders(conn, Integer.valueOf(status));
            } catch (NumberFormatException e) {
                ret = ERROR;
            }
        } else {
            ret = OrdersTable.getOrders(conn);
        }
        closeConnection(conn);
        return ret;
    }

    public static String getOrderDetail() {
        Connection conn = getConnection();
        String ret = null;
        ret = OrderDetailTable.getOrderDetail(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getDiningTables() {
        Connection conn = getConnection();
        String ret = null;
        ret = DiningTable.getDiningTable(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getDishes() {
        Connection conn = getConnection();
        String ret = null;
        ret = DishesTable.getDishes(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getDishCategory() {
        Connection conn = getConnection();
        String ret = null;
        ret = DishCategoryTable.getDishCategory(conn);
        closeConnection(conn);
        return ret;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String handlePostOrder(HttpServletRequest request) {
        int rowCount = 0;
        Connection conn = null;
        try {
            String jsonString = request.getParameter(OrdersTable.TABEL_NAME);
            JSONObject orderData = new JSONObject(jsonString);

            double sum = orderData.getDouble(OrdersTable.SUM);
            JSONArray orderDetails = orderData.getJSONArray(OrderDetailTable.TABEL_NAME);

            conn = getConnection();
            conn.setAutoCommit(false);
            long orderId = OrdersTable.saveOrder(conn, sum);
            if (orderId != 0) {
                rowCount = OrderDetailTable.saveOrderDetail(conn, orderId, orderDetails);
            }
            conn.commit();
        } catch (JSONException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            closeConnection(conn);
        }
        return (rowCount > 0) ? SUCCESS : ERROR;
    }
}
