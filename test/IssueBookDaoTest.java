
import dao.IssueBookDao;
import dao.DBconnection;
import model.IssueBook;
import org.junit.Before;
import org.junit.Test;
import model.ModelFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import model.Books;
import model.Students;

import static org.junit.Assert.*;

public class IssueBookDaoTest {

    private IssueBookDao issueBookDao;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        System.setProperty("DB_URL", "jdbc:mysql://localhost:3306/library_test");
        issueBookDao = ModelFactory.createIssueBookDao();
        connection = DBconnection.getConnection(); // Kết nối đến cơ sở dữ liệu

        if (connection == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
        }

        // Xóa dữ liệu cũ và thêm dữ liệu mẫu để chuẩn bị cho các bài kiểm thử
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM `student issued book`");
            stmt.execute("DELETE FROM `student`");
            stmt.execute("DELETE FROM `book`");

            // Thêm dữ liệu mẫu cho bảng `student`, `book` và `student issued book`
            stmt.execute("INSERT INTO `student` VALUES (1, 'Student A', 'UET'), (2, 'Student B', 'ULIS')");
            stmt.execute("INSERT INTO `book` VALUES ('123456789', 'Book A', 'Author A', 10)");
            stmt.execute("INSERT INTO `student issued book` VALUES (1, '123456789', '2024-01-01', '2024-01-12', 'Borrowing')");
        }
    }

    @Test
    public void testIsAlreadyIssue_Success() {
        boolean result = issueBookDao.isAlreadyIssue("123456789", 1);
        assertTrue("Sách đã được mượn nhưng trả về false", result);
    }

    @Test
    public void testIsAlreadyIssue_Failure() {
        boolean result = issueBookDao.isAlreadyIssue("000000000", 1);
        assertFalse("Sách không được mượn nhưng trả về true", result);
    }

    @Test
public void testAddNewStudentIssueBook() {
    // Tạo các đối tượng Student và Book
    Students student = new Students(2, "Student B", "ULIS");
    Books book = new Books("123456789", "Book A", "Author A", 10);

    // Tạo đối tượng IssueBook với các đối tượng Student và Book
    IssueBook newIssue = new IssueBook(student, book, "2024-02-01", "2024-02-10", "Borrowing");
    
    // Thêm bản ghi mượn sách
    issueBookDao.addNewStudentIssueBook(newIssue);

    // Kiểm tra xem sách đã được mượn chưa
    boolean result = issueBookDao.isAlreadyIssue("123456789", 2);
    assertTrue("Thêm bản ghi mượn sách thất bại", result);
}


    @Test
    public void testGetNumberOfIssueBook() {
        int count = issueBookDao.getNumberOfIssueBook();
        assertEquals("Số lượng bản ghi sách mượn không đúng", 1, count);
    }

    @Test
    public void testGetAllIssueBooks() {
        List<IssueBook> issues = issueBookDao.getAllIssueBooks();
        assertNotNull("Danh sách sách mượn trả về null", issues);
        assertEquals("Số lượng sách mượn không đúng", 1, issues.size());
    }

    @Test
public void testGetIssuedBooks() {
    List<IssueBook> issuedBooks = issueBookDao.getAllIssueBooksBorrowing();
    assertNotNull("Danh sách sách đang được mượn trả về null", issuedBooks);
    assertEquals("Số lượng sách đang mượn không đúng", 1, issuedBooks.size());
}


    @Test
    public void testGetNumberOfDefaulter() {
        int count = issueBookDao.getNumberOfDefaulter();
        assertEquals("Số lượng người mượn quá hạn không đúng", 1, count);
    }

    @Test
    public void testUpdateBookStatus() {
        issueBookDao.updateBookStatus("123456789", 1);
        boolean result = issueBookDao.isAlreadyIssue("123456789", 1);
        assertFalse("Cập nhật trạng thái sách thất bại", result);
    }

    @Test
public void testSearchDefaulter() {
    List<IssueBook> defaulters = issueBookDao.searchDefaulter(1);
    assertNotNull("Danh sách người mượn quá hạn trả về null", defaulters);
    assertEquals("Danh sách người mượn quá hạn không đúng", 1, defaulters.size());
}


    @Test
public void testSearchIssuedBooks() {
    List<IssueBook> issues = issueBookDao.searchIssuedBooks("2024-01-01", "2024-01-31");
    assertNotNull("Danh sách sách mượn theo thời gian trả về null", issues);
    assertEquals("Số lượng sách mượn theo thời gian không đúng", 1, issues.size());
}

    @Test
public void testGetAIssueBookDetail() {
    IssueBook issueBook = issueBookDao.getAIssueBookDetail("123456789", 1);
    assertNotNull("Không tìm thấy chi tiết sách mượn", issueBook);
    assertEquals("ISBN không khớp", "123456789", issueBook.getBook().getISBN());
}


    @Test
public void testGetAllRecords() {
    List<IssueBook> records = issueBookDao.getAllRecords();
    assertNotNull("Danh sách tất cả bản ghi sách mượn trả về null", records);
    assertEquals("Số lượng bản ghi không đúng", 1, records.size());
}


    @Test
public void testGetAIssueBookBorrowing_Success() {
    IssueBook issueBook = issueBookDao.getAIssueBookBorrowing("123456789", 1);
    assertNotNull("Không tìm thấy bản ghi sách mượn", issueBook);
    assertEquals("ISBN không khớp", "123456789", issueBook.getBook().getISBN());
    assertEquals("Student ID không khớp", 1, issueBook.getStudent().getId());
    assertEquals("Ngày mượn không khớp", "2024-01-01", issueBook.getIssueDate());
    assertEquals("Ngày trả không khớp", "2024-01-12", issueBook.getDueDate());
    assertEquals("Trạng thái không khớp", "Borrowing", issueBook.getStatus());
}


    @Test
    public void testGetAIssueBookBorrowing_Failure() {
        IssueBook issueBook = issueBookDao.getAIssueBookBorrowing("000000000", 1);
        assertNull("Tìm thấy bản ghi sách mượn không tồn tại", issueBook);
    }
}