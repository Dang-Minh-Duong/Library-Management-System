/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author DMX
 */
public class DBconnection {
    private static final String URL = System.getProperty("DB_URL", "jdbc:mysql://localhost:3306/library");
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Phương thức kết nối
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Tải driver MySQL
        return DriverManager.getConnection(URL, USER, PASSWORD); // Tạo kết nối
    }
    
}
