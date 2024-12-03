package dao;

import model.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IssueBookDao {

    // Kiểm tra xem sách đã được mượn bởi sinh viên chưa
    public boolean isAlreadyIssue(String isbn, int id) {
        String query = String.format("SELECT * FROM `student issued book` WHERE `Student ID` = %d AND ISBN = '%s' AND status = 'Borrowing'", id, isbn);
        boolean result = false;
        
        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            
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
        
        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement()) {
            
            stmt.executeUpdate(query);
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public int getNumberOfIssueBook() {
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM `student issued book`";
        
        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(countQuery)) {
            
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
        String query = "Select * from `Student issued book`";
        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new IssueBook(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
            
        }
        return list;
    }

    
    // Phương thức để lấy danh sách các sách đã mượn chưa trả
    public List<ExtendedIssueBook> getIssuedBooks() {
        List<ExtendedIssueBook> issuedBooks = new ArrayList<>();
        try (Connection con = DBconnection.getConnection()) {
            String query = "SELECT sib.`ISBN`, b.name AS book_name, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` " +
                           "FROM `student issued book` sib " +
                           "INNER JOIN book b on sib.`ISBN` = b.isbn " +
                           "INNER JOIN student s on sib.`Student ID` = s.ID " +
                            "Where sib.`status` = 'Borrowing'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ExtendedIssueBook issueBook = new ExtendedIssueBook(
                    rs.getString("ISBN"),
                    rs.getString("book_name"),
                    rs.getString("ID"),
                    rs.getString("student_name"),
                    rs.getString("Issue date"),
                    rs.getString("Due date"),
                    rs.getString("status")
                );
                issuedBooks.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return issuedBooks;
    }
    
    public int getNumberOfDefaulter() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(currentTime);
        String query = String.format("SELECT COUNT(*) FROM `student issued book` WHERE `Due date` < '%s' AND status = 'Borrowing'", todayDate);
        int count = 0;

        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
    public List<ExtendedIssueBook> getDefaulterList() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(currentTime);
        List<ExtendedIssueBook> issuedBooks = new ArrayList<>();
        try (Connection con = DBconnection.getConnection()) {
            String query = String.format("SELECT sib.`ISBN`, b.name AS book_name, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` " +
                           "FROM `student issued book` sib " +
                           "INNER JOIN book b on sib.`ISBN` = b.isbn " +
                           "INNER JOIN student s on sib.`Student ID` = s.ID " +
                            "Where sib.`status` = 'Borrowing' and sib.`Due date` < '%s'", todayDate);
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ExtendedIssueBook issueBook = new ExtendedIssueBook(
                    rs.getString("ISBN"),
                    rs.getString("book_name"),
                    rs.getString("ID"),
                    rs.getString("student_name"),
                    rs.getString("Issue date"),
                    rs.getString("Due date"),
                    rs.getString("status")
                );
                issuedBooks.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return issuedBooks;
    }
    public List<ExtendedIssueBook> searchDefaulter(int id) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(currentTime);
        List<ExtendedIssueBook> issuedBooks = new ArrayList<>();
        try (Connection con = DBconnection.getConnection()) {
            String query = String.format("SELECT sib.`ISBN`, b.name AS book_name, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` " +
                           "FROM `student issued book` sib " +
                           "INNER JOIN book b on sib.`ISBN` = b.isbn " +
                           "INNER JOIN student s on sib.`Student ID` = s.ID " +
                            "Where sib.`status` = 'Borrowing' and sib.`Due date` < '%s' and s.ID = %d", todayDate, id);
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
                
            while (rs.next()) {
                ExtendedIssueBook issueBook = new ExtendedIssueBook(
                    rs.getString("ISBN"),
                    rs.getString("book_name"),
                    rs.getString("ID"),
                    rs.getString("student_name"),
                    rs.getString("Issue date"),
                    rs.getString("Due date"),
                    rs.getString("status")
                );
                issuedBooks.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return issuedBooks;
    }
    // Phương thức trả về tất cả các bản ghi
    public List<ExtendedIssueBook> getAllRecords() {
        List<ExtendedIssueBook> issuedBooks = new ArrayList<>();
        try (Connection con = DBconnection.getConnection()) {
            String query = "SELECT sib.`ISBN`, b.name AS book_name, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` " +
                           "FROM `student issued book` sib " +
                           "INNER JOIN book b on sib.`ISBN` = b.isbn " +
                           "INNER JOIN student s on sib.`Student ID` = s.ID ";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ExtendedIssueBook issueBook = new ExtendedIssueBook(
                    rs.getString("ISBN"),
                    rs.getString("book_name"),
                    rs.getString("ID"),
                    rs.getString("student_name"),
                    rs.getString("Issue date"),
                    rs.getString("Due date"),
                    rs.getString("status")
                );
                issuedBooks.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return issuedBooks;
    }
    // Tìm kiếm theo khoảng thời gian
    public List<ExtendedIssueBook> searchIssuedBooks(String fromDate, String toDate) {
    List<ExtendedIssueBook> issuedBooks = new ArrayList<>();
    try (Connection con = DBconnection.getConnection()) {
        String sql = "SELECT sib.`ISBN`, b.name AS book_name, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` " +
                     "FROM `student issued book` sib " +
                     "INNER JOIN book b on sib.`ISBN` = b.isbn " +
                     "INNER JOIN student s on sib.`Student ID` = s.ID " +
                     "WHERE sib.`Issue date` BETWEEN ? AND ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, fromDate);
        pst.setString(2, toDate);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            ExtendedIssueBook issueBook = new ExtendedIssueBook(
                rs.getString("ISBN"),
                rs.getString("book_name"),
                rs.getString("ID"),
                rs.getString("student_name"),
                rs.getString("Issue date"),
                rs.getString("Due date"),
                rs.getString("status")
            );
            issuedBooks.add(issueBook);
        }
    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
    }
    return issuedBooks;
    }

    // Phương thức để lấy chi tiết sách được mượn và trả về IssueBook
    public IssueBook getAIssueBookDetail(String isbn, int studentId) {
        IssueBook issueBook = null;
        try (Connection con = DBconnection.getConnection()) {
            String query = "SELECT * FROM `student issued book` WHERE `ISBN` = ? AND `Student ID` = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, isbn);
            pst.setInt(2, studentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                issueBook = new IssueBook(
                    studentId,
                    isbn,
                    rs.getString("Issue date"),
                    rs.getString("Due date"),
                    rs.getString("status")
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return issueBook;
    }
    // IssueBookDao.java

    // Cập nhật trạng thái sách
    public void updateBookStatus(String isbn, int studentId) {
        try (Connection con = DBconnection.getConnection()) {
            String query = "UPDATE `student issued book` SET `Status` = ? WHERE `ISBN` = ? AND `Student ID` = ? AND `Status` = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "Returned");
            pst.setString(2, isbn);
            pst.setInt(3, studentId);
            pst.setString(4, "Borrowing");
            pst.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}








