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
        String query = String.format("INSERT INTO `student issued book` (`Student ID`, `ISBN`, `Issue date`, `Due date`, `Status`) VALUES (?, ?, ?, ?, 'Borrowing')");

        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentIssueBook.getStudent().getId());
            pst.setString(2, studentIssueBook.getBook().getISBN());
            pst.setString(3, studentIssueBook.getIssueDate());
            pst.setString(4, studentIssueBook.getDueDate());

            pst.executeUpdate();
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
        String query = "SELECT si.*, s.name AS StudentName, s.university AS University, b.name AS BookTitle, b.author AS Author, b.quantity AS Quantity "
                + "FROM `student issued book` si "
                + "JOIN `student` s ON si.`Student ID` = s.`id` "
                + "JOIN `book` b ON si.`ISBN` = b.`isbn`";

        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Students student = new Students(rs.getInt("Student ID"), rs.getString("StudentName"), rs.getString("University"));
                Books book = new Books(rs.getString("ISBN"), rs.getString("BookTitle"), rs.getString("Author"), rs.getInt("Quantity"));

                IssueBook issueBook = new IssueBook(
                        student,
                        book,
                        rs.getString("Issue date"),
                        rs.getString("Due date"),
                        rs.getString("status")
                );

                list.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    // Phương thức để lấy danh sách các sách đã mượn chưa trả
    public List<IssueBook> getAllIssueBooksBorrowing() {
        List<IssueBook> list = new ArrayList<>();
        String query = "SELECT si.*, s.name AS StudentName, s.university AS University, b.name AS BookTitle, b.author AS Author, b.quantity AS Quantity "
                + "FROM `student issued book` si "
                + "JOIN `student` s ON si.`Student ID` = s.`id` "
                + "JOIN `book` b ON si.`ISBN` = b.`isbn` "
                + "WHERE si.`status` = 'Borrowing'";

        try (Connection con = DBconnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Students student = new Students(rs.getInt("Student ID"), rs.getString("StudentName"), rs.getString("University"));
                Books book = new Books(rs.getString("ISBN"), rs.getString("BookTitle"), rs.getString("Author"), rs.getInt("Quantity"));

                IssueBook issueBook = new IssueBook(
                        student,
                        book,
                        rs.getString("Issue date"),
                        rs.getString("Due date"),
                        rs.getString("status")
                );

                list.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    // Trả về số lượng chưa trả
    public int getNumberOfDefaulter() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(new java.util.Date(currentTime)); // Định dạng ngày hiện tại
        String query = "SELECT COUNT(*) FROM `student issued book` WHERE `Due date` < ? AND status = 'Borrowing'";

        int count = 0;

        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, todayDate); // Thiết lập giá trị ngày hiện tại vào truy vấn SQL
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1); // Lấy số lượng kết quả trả về
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    public List<IssueBook> getDefaulterList() {
        List<IssueBook> defaulterList = new ArrayList<>();
        String query = "SELECT sib.*, s.name AS student_name, s.university AS university, b.name AS book_title, b.author AS author, b.quantity AS quantity "
                + "FROM `student issued book` sib "
                + "JOIN `student` s ON sib.`Student ID` = s.`id` "
                + "JOIN `book` b ON sib.`ISBN` = b.`isbn` "
                + "WHERE sib.`Due date` < ? AND sib.`Status` = 'Borrowing'";
        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            // Lấy ngày hiện tại
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String todayDate = df.format(new java.util.Date());

            pst.setString(1, todayDate); // Thiết lập giá trị ngày hiện tại vào truy vấn SQL
            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    Students student = new Students(rs.getInt("Student ID"), rs.getString("student_name"), rs.getString("university"));
                    Books book = new Books(rs.getString("ISBN"), rs.getString("book_title"), rs.getString("author"), rs.getInt("quantity"));

                    IssueBook issueBook = new IssueBook(
                            student,
                            book,
                            rs.getString("Issue date"),
                            rs.getString("Due date"),
                            rs.getString("status")
                    );
                    defaulterList.add(issueBook);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return defaulterList;
    }

    public List<IssueBook> searchDefaulter(int id) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(new java.util.Date(currentTime));
        List<IssueBook> issuedBooks = new ArrayList<>();

        String query = "SELECT sib.*, s.name AS student_name, s.university AS university, b.name AS book_title, b.author AS author, b.quantity AS quantity "
                + "FROM `student issued book` sib "
                + "INNER JOIN book b ON sib.`ISBN` = b.isbn "
                + "INNER JOIN student s ON sib.`Student ID` = s.`id` "
                + "WHERE sib.`status` = 'Borrowing' AND sib.`Due date` < ? AND s.`id` = ?";
        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, todayDate);
            pst.setInt(2, id);
            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    Students student = new Students(rs.getInt("Student ID"), rs.getString("student_name"), rs.getString("university"));
                    Books book = new Books(rs.getString("ISBN"), rs.getString("book_title"), rs.getString("author"), rs.getInt("quantity"));

                    IssueBook issueBook = new IssueBook(
                            student,
                            book,
                            rs.getString("Issue date"),
                            rs.getString("Due date"),
                            rs.getString("status")
                    );
                    issuedBooks.add(issueBook);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return issuedBooks;
    }

    // Phương thức trả về tất cả các bản ghi
    public List<IssueBook> getAllRecords() {
        List<IssueBook> issuedBooks = new ArrayList<>();
        String query = "SELECT sib.`ISBN`, b.name AS book_title, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` "
                + "FROM `student issued book` sib "
                + "INNER JOIN book b ON sib.`ISBN` = b.isbn "
                + "INNER JOIN student s ON sib.`Student ID` = s.ID ";
        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Students student = new Students(rs.getInt("ID"), rs.getString("student_name"), "");  // Thêm thông tin university nếu cần thiết
                Books book = new Books(rs.getString("ISBN"), rs.getString("book_title"), "", 0);  // Thêm thông tin author và quantity nếu cần thiết

                IssueBook issueBook = new IssueBook(
                        student,
                        book,
                        rs.getString("Issue date"),
                        rs.getString("Due date"),
                        rs.getString("status")
                );

                issuedBooks.add(issueBook);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return issuedBooks;
    }

    // Tìm kiếm theo khoảng thời gian
    public List<IssueBook> searchIssuedBooks(String fromDate, String toDate) {
        List<IssueBook> issuedBooks = new ArrayList<>();
        String query = "SELECT sib.`ISBN`, b.name AS book_title, s.ID, s.Name AS student_name, sib.`Issue date`, sib.`Due date`, sib.`status` "
                + "FROM `student issued book` sib "
                + "INNER JOIN book b ON sib.`ISBN` = b.isbn "
                + "INNER JOIN student s ON sib.`Student ID` = s.ID "
                + "WHERE sib.`Issue date` BETWEEN ? AND ?";
        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, fromDate);
            pst.setString(2, toDate);
            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    Students student = new Students(rs.getInt("ID"), rs.getString("student_name"), "");  // Thêm thông tin university nếu cần thiết
                    Books book = new Books(rs.getString("ISBN"), rs.getString("book_title"), "", 0);  // Thêm thông tin author và quantity nếu cần thiết

                    IssueBook issueBook = new IssueBook(
                            student,
                            book,
                            rs.getString("Issue date"),
                            rs.getString("Due date"),
                            rs.getString("status")
                    );
                    issuedBooks.add(issueBook);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return issuedBooks;
    }

    // Phương thức để lấy chi tiết sách được mượn và trả về IssueBook
    public IssueBook getAIssueBookDetail(String isbn, int studentId) {
        IssueBook issueBook = null;
        String query = "SELECT sib.*, s.name AS student_name, s.university AS university, b.name AS book_title, b.author AS author, b.quantity AS quantity "
                + "FROM `student issued book` sib "
                + "JOIN `student` s ON sib.`Student ID` = s.`id` "
                + "JOIN `book` b ON sib.`ISBN` = b.`isbn` "
                + "WHERE sib.`ISBN` = ? AND sib.`Student ID` = ?";
        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, isbn);
            pst.setInt(2, studentId);
            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    Students student = new Students(studentId, rs.getString("student_name"), rs.getString("university"));
                    Books book = new Books(isbn, rs.getString("book_title"), rs.getString("author"), rs.getInt("quantity"));

                    issueBook = new IssueBook(
                            student,
                            book,
                            rs.getString("Issue date"),
                            rs.getString("Due date"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return issueBook;
    }

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

    public IssueBook getAIssueBookBorrowing(String isbn, int studentId) {
        IssueBook issueBook = null;
        String query = "SELECT sib.*, s.name AS student_name, s.university AS university, b.name AS book_title, b.author AS author, b.quantity AS quantity "
                + "FROM `student issued book` sib "
                + "JOIN `student` s ON sib.`Student ID` = s.`id` "
                + "JOIN `book` b ON sib.`ISBN` = b.`isbn` "
                + "WHERE sib.`ISBN` = ? AND sib.`Student ID` = ? AND sib.`Status` = 'Borrowing'";
        try (Connection con = DBconnection.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, isbn);
            pst.setInt(2, studentId);
            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    Students student = new Students(studentId, rs.getString("student_name"), rs.getString("university"));
                    Books book = new Books(isbn, rs.getString("book_title"), rs.getString("author"), rs.getInt("quantity"));

                    issueBook = new IssueBook(
                            student,
                            book,
                            rs.getString("Issue date"),
                            rs.getString("Due date"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return issueBook;
    }

}
