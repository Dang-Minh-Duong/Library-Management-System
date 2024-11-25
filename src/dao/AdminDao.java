package dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminDao {

    // Phương thức kiểm tra thông tin đăng nhập
    public boolean checkInfoLogin(String email, String password) {
        String query = String.format("SELECT * FROM admins WHERE email = '%s' AND password = '%s'", email, password);
        try (Connection con = DBconnection.getConnection(); 
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            return rs.next();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
            return false;
        }
    }

    // Phương thức chèn thông tin đăng nhập
    public boolean insertInfoLogin(String fullName, String email, String password) {
        String insertQuery = String.format("INSERT INTO admins VALUES ('%s', '%s', '%s')", fullName, email, password);
        String selectQuery = "SELECT email FROM admins";
        try (Connection con = DBconnection.getConnection(); 
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            while (rs.next()) {
                if (email.equals(rs.getString(1))) return false;
            }
            stmt.execute(insertQuery);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
            return false;
        }
    }
}
