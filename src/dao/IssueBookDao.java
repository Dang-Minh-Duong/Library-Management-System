package dao;

import model.IssueBook;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IssueBookDao {

    // Kiểm tra xem sách đã được mượn bởi sinh viên chưa
    public boolean isAlreadyIssue(String isbn, int id) {
        String query = String.format("SELECT * FROM `student issued book` WHERE `Student ID` = %d AND ISBN = '%s' AND status = 'Borrowing'", id, isbn);
        boolean result = false;

        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                result = true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    // Thêm thông tin sách mượn mới của sinh viên
    public void addNewStudentIssueBook(IssueBook studentIssueBook) {
        String query = String.format("INSERT INTO `student issued book` VALUES (%d, '%s', '%s', '%s', 'Borrowing')",
                studentIssueBook.getId(), studentIssueBook.getIsbn(),
                studentIssueBook.getIssueDate(), studentIssueBook.getDueDate());

        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(query);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfIssueBook() {
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM `student issued book`";

        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(countQuery)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
    public List<IssueBook> getAllIssueBooks() {
        List<IssueBook> list = new ArrayList<>();
        String query = "SELECT sib.`ISBN`, b.name, s.ID, s.Name, sib.`Issue date`, sib.`Due date`, sib.`status`" +
            "FROM `student issued book` sib" +
            "INNER JOIN book b on sib.`ISBN` = b.isbn" +
            "INNER JOIN student s on sib.`Student ID` = s.ID";
        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new IssueBook(rs.getString(1),rs.getString(2),
                        rs.getInt(3),rs.getString(4),
                        rs.getString(5),rs.getString(6),rs.getString(7)));
            }
            }

        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
        return list;
    }
}
