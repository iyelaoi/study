package cn.wqz.algorithms.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDemo {
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://10.0.75.1:3306/ncov_test?useSSL=false&serverTimezone=UTC&CharacterEncoding=utf-8";
    private static String username = "root";
    private static String password = "461299";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println(connection);
    }
}
