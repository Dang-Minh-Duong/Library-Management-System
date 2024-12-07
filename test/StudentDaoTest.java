import dao.StudentDao;
import dao.DBconnection;
import model.Students;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class StudentDaoTest {

    private StudentDao studentDao;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        System.setProperty("DB_URL", "jdbc:mysql://localhost:3306/library_test");
        studentDao = new StudentDao();
        connection = DBconnection.getConnection(); // Kết nối cơ sở dữ liệu

        if (connection == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
        }

        // Xóa dữ liệu cũ trong bảng student trước khi thêm dữ liệu mẫu mới
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM student");  // Xóa tất cả dữ liệu trong bảng

            // Thêm dữ liệu mẫu
            stmt.execute("INSERT INTO student VALUES (1, 'Student A', 'University A')");
            stmt.execute("INSERT INTO student VALUES (2, 'Student B', 'University B')");
        }
    }

    @Test
    public void testGetAllStudents() {
        List<Students> students = studentDao.getAllStudents();
        assertNotNull("Danh sách sinh viên trả về null", students);
        assertEquals("Số lượng sinh viên không đúng", 2, students.size());
    }

    @Test
    public void testAddStudent() {
        Students newStudent = new Students(3, "Student C", "University C");
        boolean result = studentDao.addStudent(newStudent);
        assertTrue("Thêm sinh viên thất bại", result);

        // Kiểm tra sinh viên vừa thêm
        Students student = studentDao.getAStudent(3);
        assertNotNull("Không tìm thấy sinh viên vừa thêm", student);
        assertEquals("Tên sinh viên không đúng", "Student C", student.getName());
    }

    @Test
    public void testUpdateStudent() {
        Students updatedStudent = new Students(1, "Updated Student A", "Updated University A");
        boolean result = studentDao.updateStudent(updatedStudent);
        assertTrue("Cập nhật sinh viên thất bại", result);

        // Kiểm tra sinh viên sau khi cập nhật
        Students student = studentDao.getAStudent(1);
        assertNotNull("Không tìm thấy sinh viên sau khi cập nhật", student);
        assertEquals("Tên sinh viên không được cập nhật đúng", "Updated Student A", student.getName());
    }

    @Test
    public void testDeleteStudent() {
        boolean result = studentDao.deleteStudent(2);
        assertTrue("Xóa sinh viên thất bại", result);

        // Kiểm tra sinh viên sau khi xóa
        Students student = studentDao.getAStudent(2);
        assertNull("Sinh viên vẫn tồn tại sau khi xóa", student);
    }

    @Test
    public void testGetNumberOfStudent() {
        int count = studentDao.getNumberOfStudent();
        assertEquals("Số lượng sinh viên trong cơ sở dữ liệu không đúng", 2, count);
    }

    @Test
    public void testGetAStudent_Success() {
        Students student = studentDao.getAStudent(1);
        assertNotNull("Không tìm thấy sinh viên với ID hợp lệ", student);
        assertEquals("Tên sinh viên không đúng", "Student A", student.getName());
    }

    @Test
    public void testGetAStudent_Failure() {
        Students student = studentDao.getAStudent(99);
        assertNull("Tìm thấy sinh viên với ID không tồn tại", student);
    }

    @Test

    public void testIsStudentExist() {
        try {
            // Kiểm tra với ID sinh viên tồn tại
            boolean exists = studentDao.isStudentExist(1);
            assertTrue("Sinh viên với ID 1 không tồn tại, mặc dù phải tồn tại", exists);

            // Kiểm tra với ID sinh viên không tồn tại
            exists = studentDao.isStudentExist(99);
            assertFalse("Sinh viên với ID 99 tồn tại, mặc dù không tồn tại", exists);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Lỗi xảy ra khi gọi phương thức isStudentExist: " + e.getMessage());
        }
    }

}
