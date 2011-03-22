package com.androidodc.eorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

import com.androidodc.eorder.tables.CategoryTable;
import com.androidodc.eorder.tables.DiningTable;
import com.androidodc.eorder.tables.DishCategoryTable;
import com.androidodc.eorder.tables.DishesTable;
import com.androidodc.eorder.tables.OrderDetailTable;
import com.androidodc.eorder.tables.OrdersTable;

public class DataBaseHelper {
    private static String SUCCESS = "success";
    private static String ERROR = "error";
    private static String JNDI_DS_NAME = "java:/eOrderDS";

    public synchronized static Connection getConnection() {
        InitialContext initCtx = null;
        DataSource ds = null;
        Connection conn = null;
        try {
            initCtx = new InitialContext();
            ds = (DataSource)initCtx.lookup(JNDI_DS_NAME);
            System.out.println("DataSource = " + ds);
            conn = ds.getConnection();
            System.out.println("Connection = " + conn);
            createTables(conn);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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

    public static void createTable(Connection conn, String sqlStr) {
        PreparedStatement state = null;
        try {
            state = conn.prepareStatement(sqlStr);
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(state);
        }
    }

    public static String getCategory() {
        Connection conn = getConnection();
        String ret = CategoryTable.getCategory(conn);
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
        String ret = OrderDetailTable.getOrderDetail(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getDiningTables() {
        Connection conn = getConnection();
        String ret = DiningTable.getDiningTable(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getDishes() {
        Connection conn = getConnection();
        String ret = DishesTable.getDishes(conn);
        closeConnection(conn);
        return ret;
    }

    public static String getDishCategory() {
        Connection conn = getConnection();
        String ret = DishCategoryTable.getDishCategory(conn);
        closeConnection(conn);
        return ret;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement state) {
        if (state != null) {
            try {
                state.close();
                state = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String handlePostOrder(HttpServletRequest request) {
        int rowCount = 0;
        Connection conn = null;
        try {
            String jsonString = request.getParameter(OrdersTable.TABLE_NAME);
            if (jsonString == null) {
                throw new JSONException("Parameter is null!");
            }
            JSONObject orderData = new JSONObject(jsonString);

            double sum = orderData.getDouble(OrdersTable.SUM);
            JSONArray orderDetails = orderData.getJSONArray(OrderDetailTable.TABLE_NAME);

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
