/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Students;

/**
 *
 * @author DMX
 */
public class StudentDao {

    public List<Students> getAllStudents() {
        List<Students> students = new ArrayList<>();
        String query = "SELECT * FROM student";

        try (Connection con = DBconnection.getConnection(); // Gọi kết nối từ DatabaseConnection
                 Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int Id = rs.getInt("ID");
                String name = rs.getString("Name");
                String university = rs.getString("University");
                students.add(new Students(Id, name, university));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    // Thêm sinh viên.
    public boolean addStudent(Students student) {
        String query = "INSERT INTO student (ID, Name, University) VALUES (?, ?, ?)";

        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, student.getId());          // Gán ID là kiểu int
            pst.setString(2, student.getName());    // Gán Name
            pst.setString(3, student.getUniversity()); // Gán University

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; // Nếu thêm thành công, trả về true
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu xảy ra lỗi, trả về false
        }
    }

    // Cập nhật sinh viên.
    public boolean updateStudent(Students student) {
        String query = "UPDATE student SET Name = ?, University = ? WHERE ID = ?";

        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, student.getName());        // Gán Name
            pst.setString(2, student.getUniversity()); // Gán University
            pst.setInt(3, student.getId());            // Điều kiện WHERE với ID

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; // Nếu cập nhật thành công, trả về true
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu xảy ra lỗi, trả về false
        }
    }

    // Xóa sinh viên.
    public boolean deleteStudent(int studentId) {
        String query = "DELETE FROM student WHERE ID = ?";

        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, studentId); // Gán ID vào câu truy vấn

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; // Nếu xóa thành công, trả về true
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu xảy ra lỗi, trả về false
        }
    }

    public int getNumberOfStudent() {
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM student";

        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(countQuery)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    public Students getAStudent(int id) {
        Students student = null;
        String query = String.format("SELECT * FROM student WHERE id = %d", id);

        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                student = new Students(id, rs.getString("name"), rs.getString("university"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return student;
    }

    public boolean isStudentExist(int studentId) throws ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM student WHERE ID = ?";
        try (Connection conn = DBconnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu có ít nhất một bản ghi, trả về true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không có sinh viên với ID này
    }
}
