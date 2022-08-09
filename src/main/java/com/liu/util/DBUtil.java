package com.liu.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/liu?characterEncoding=UTF-8&useSSL=false";
//    private static final String URL = "jdbc:mysql://localhost:3306/liu?user=root&password=123456&characterEncoding=UTF-8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static DataSource dataSource = null;

    static {
        dataSource = new MysqlDataSource();
        ((MysqlDataSource) dataSource).setUrl(URL);
        ((MysqlDataSource) dataSource).setUser(USERNAME);
        ((MysqlDataSource) dataSource).setPassword(PASSWORD);
    }

    public static Connection getConnection() {
        try {
            if(dataSource != null) {
                return dataSource.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection, Statement statement) {
        close(connection, statement, null);
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if(resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
