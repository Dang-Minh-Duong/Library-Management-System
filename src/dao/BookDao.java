/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Books;
/**
 *
 * @author DMX
 */
public class BookDao {
    public List<Books> getAllBooks() {
        List<Books> books = new ArrayList<>();
        String query = "SELECT * FROM book";

        try (Connection con = DBconnection.getConnection(); // Gọi kết nối từ DatabaseConnection
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String name = rs.getString("name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                books.add(new Books(isbn, name, author, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    public Books getABook(String isbn) { 
        Books book = null; 
        String query = String.format("SELECT * FROM book WHERE isbn = '%s'", isbn); 
        try (Connection con = DBconnection.getConnection(); 
                Statement stmt = con.createStatement(); 
                ResultSet rs = stmt.executeQuery(query)) 
        { if (rs.next()) { 
            book = new Books(isbn, rs.getString("name"), rs.getString("author"), rs.getInt("quantity")); 
        } 
        } catch (SQLException | ClassNotFoundException e) { 
            throw new RuntimeException(e); 
        } 
        return book; 
    }
    
    public int getNumberOfBook() {
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM book";

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
    public void updateQuantity(String isbn) {
        String query = String.format("UPDATE book SET quantity = quantity - 1 WHERE isbn = '%s'", isbn);

        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(query);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // Thêm sách vào csdl.
    public boolean addBook(Books book) {
        String query = "INSERT INTO book (isbn, name, author, quantity) VALUES (?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, book.getISBN());
            pst.setString(2, book.getName());
            pst.setString(3, book.getAuthor());
            pst.setInt(4, book.getQuantity());
            pst.executeUpdate();
            return true; // Thêm sách thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Thêm sách thất bại
        }
    }
    
    // Update book.
    public boolean updateBook(Books book) {
        String query = "UPDATE book SET name = ?, author = ?, quantity = ? WHERE isbn = ?";
        try (Connection con = DBconnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, book.getName());
            pst.setString(2, book.getAuthor());
            pst.setInt(3, book.getQuantity());
            pst.setString(4, book.getISBN());
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }
    
    // Delete book.
    public boolean deleteBook(String isbn) {
    String query = "DELETE FROM book WHERE isbn = ?";
    try (Connection con = DBconnection.getConnection();
        PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, isbn); // Gán ISBN vào câu lệnh SQL
        int rowsDeleted = pst.executeUpdate(); // Thực thi lệnh DELETE
        return rowsDeleted > 0; // Trả về true nếu có ít nhất một bản ghi bị xóa
    } catch (Exception e) {
        e.printStackTrace();
        return false; // Trả về false nếu có lỗi xảy ra
    }
}
}

