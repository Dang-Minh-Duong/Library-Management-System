import dao.BookDao;
import dao.DBconnection;
import model.Books;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class BookDaoTest {
    private BookDao bookDao;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        System.setProperty("DB_URL", "jdbc:mysql://localhost:3306/library_test");
        bookDao = new BookDao();
        connection = DBconnection.getConnection(); // Kết nối đến cơ sở dữ liệu

        // Kiểm tra kết nối không null
        if (connection == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
        }

        // Xóa dữ liệu cũ nếu có và thêm dữ liệu mẫu cho mỗi lần kiểm thử
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM book"); // Xóa dữ liệu cũ để đảm bảo kiểm thử sạch
            stmt.execute("INSERT INTO book VALUES ('123456789', 'Book A', 'Author A', 10)");
            stmt.execute("INSERT INTO book VALUES ('987654321', 'Book B', 'Author B', 5)");
        }
    }

    @Test
    public void testGetAllBooks() {
        List<Books> books = bookDao.getAllBooks();
        assertNotNull("Danh sách sách trả về null", books);
        assertEquals("Số lượng sách không đúng", 2, books.size());
    }

    @Test
    public void testGetABook_Success() {
        Books book = bookDao.getABook("123456789");
        assertNotNull("Không tìm thấy sách với ISBN hợp lệ", book);
        assertEquals("Tên sách không đúng", "Book A", book.getName());
    }

    @Test
    public void testGetABook_Failure() {
        Books book = bookDao.getABook("000000000");
        assertNull("Tìm thấy sách với ISBN không tồn tại", book);
    }

    @Test
    public void testGetNumberOfBook() {
        int count = bookDao.getNumberOfBook();
        assertEquals("Số lượng sách trong cơ sở dữ liệu không đúng", 2, count);
    }

    @Test
    public void testUpdateQuantity() {
        bookDao.updateQuantity("123456789", 5);
        Books book = bookDao.getABook("123456789");
        assertNotNull("Không tìm thấy sách sau khi cập nhật số lượng", book);
        assertEquals("Số lượng sách không cập nhật đúng", 15, book.getQuantity());
    }

    @Test
    public void testAddBook() {
        Books newBook = new Books("111111111", "Book C", "Author C", 20);
        boolean result = bookDao.addBook(newBook);
        assertTrue("Thêm sách thất bại", result);

        // Kiểm tra sách vừa thêm
        Books addedBook = bookDao.getABook("111111111");
        assertNotNull("Không tìm thấy sách vừa thêm", addedBook);
        assertEquals("Tên sách không đúng", "Book C", addedBook.getName());
    }

    @Test
    public void testUpdateBook() {
        Books updatedBook = new Books("123456789", "Updated Book A", "Updated Author A", 25);
        boolean result = bookDao.updateBook(updatedBook);
        assertTrue("Cập nhật sách thất bại", result);

        // Kiểm tra sách sau khi cập nhật
        Books book = bookDao.getABook("123456789");
        assertNotNull("Không tìm thấy sách sau khi cập nhật", book);
        assertEquals("Tên sách không được cập nhật đúng", "Updated Book A", book.getName());
    }

    @Test
    public void testDeleteBook() {
        boolean result = bookDao.deleteBook("987654321");
        assertTrue("Xóa sách thất bại", result);

        // Kiểm tra sách sau khi xóa
        Books book = bookDao.getABook("987654321");
        assertNull("Sách vẫn tồn tại sau khi xóa", book);
    }
    @Test
    public void testIsBookExist() {
        // Kiểm tra với ISBN tồn tại trong cơ sở dữ liệu
        boolean exists = bookDao.isBookExist("123456789");
        assertTrue("Book with ISBN 123456789 does not exist, but it should", exists);

        // Kiểm tra với ISBN không tồn tại trong cơ sở dữ liệu
        exists = bookDao.isBookExist("000000000");
        assertFalse("Book with ISBN 000000000 exists, but it should not", exists);
    }
    @Test
    public void testGetBookQuantity_Success() throws SQLException, ClassNotFoundException {
        // Kiểm tra số lượng sách với ISBN tồn tại
        int quantity = bookDao.getBookQuantity("123456789");
        assertEquals("Số lượng sách không đúng", 10, quantity);

        // Kiểm tra số lượng sách với ISBN khác tồn tại
        quantity = bookDao.getBookQuantity("987654321");
        assertEquals("Số lượng sách không đúng", 5, quantity);
    }

}
