import dao.AdminDao;
import dao.DBconnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class AdminDaoTest {
    private AdminDao adminDao;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        System.setProperty("DB_URL", "jdbc:mysql://localhost:3306/library_test");
        adminDao = new AdminDao();
        connection = DBconnection.getConnection(); // Kết nối qua DBconnection

        // Kiểm tra kết nối
        if (connection == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
        }

        // Xóa dữ liệu cũ và thêm dữ liệu mẫu
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM admins"); // Xóa dữ liệu mẫu cũ
            stmt.execute("INSERT INTO admins VALUES ('John Doe', 'john@example.com', '12345')"); // Thêm dữ liệu mẫu
        }
    }

    @Test
    public void testCheckInfoLogin_Success() {
        boolean result = adminDao.checkInfoLogin("john@example.com", "12345");
        assertTrue("Đăng nhập thành công nhưng trả về false", result);
    }

    @Test
    public void testCheckInfoLogin_Failure() {
        boolean result = adminDao.checkInfoLogin("john@example.com", "wrongpassword");
        assertFalse("Đăng nhập thất bại nhưng trả về true", result);
    }

    @Test
    public void testInsertInfoLogin_Success() {
        boolean result = adminDao.insertInfoLogin("Jane Doe", "jane@example.com", "67890");
        assertTrue("Thêm tài khoản mới thành công nhưng trả về false", result);

        // Kiểm tra lại đăng nhập với tài khoản vừa thêm
        boolean loginResult = adminDao.checkInfoLogin("jane@example.com", "67890");
        assertTrue("Tài khoản vừa thêm không thể đăng nhập", loginResult);
    }

    @Test
    public void testInsertInfoLogin_DuplicateEmail() {
        boolean result = adminDao.insertInfoLogin("John Smith", "john@example.com", "newpassword");
        assertFalse("Thêm tài khoản với email trùng lặp nhưng trả về true", result);
    }
}
