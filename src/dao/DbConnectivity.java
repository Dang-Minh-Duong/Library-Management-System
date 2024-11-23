package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import model.*;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class DbConnectivity {
    private Connection con;
    private Statement stmt;

    public DbConnectivity() //cons
    {
        try {
            
            String s = "jdbc:mysql://localhost:3306/library";
            con = DriverManager.getConnection(s, "root", "");

            stmt = con.createStatement();

        } catch (SQLException e) {
            System.out.println(e);
        }
        
    }
    public boolean checkInfoLogin(String email, String password) {
        String query = String.format("Select * from admins where email = '%s' and password = '%s'",
                email, password);
        try{ 
            ResultSet rs = stmt.executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    public boolean insertInfoLogin(String fullName, String email, String password) {
        String insertQuery = String.format("Insert into admins values('%s','%s','%s')",
                fullName,email, password);
        String selectQuery = "Select Email from admins";
        try {
            ResultSet rs = stmt.executeQuery(selectQuery);
            while (rs.next()) {
                if (email.equals(rs.getString(1))) return false;
                
            }
            stmt.execute(insertQuery);
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            
        }
        return false;
        
        
    }
    
    public void closeConnection()
    {
    
        try
        {
            con.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
    
    }
    public int getNumber(String s) {
        int count = 0;
        String countQuery = String.format("Select COUNT(*) from %s", s);
        try {
            ResultSet rs = stmt.executeQuery(countQuery);
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    public Books getABook(String isbn) {
        Books book;
        String query = String.format("Select * from book where isbn = '%s'", isbn);
        
        try {
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
             book = new Books(isbn, rs.getString(2), rs.getString(3),rs.getInt(4)); }
            else book = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;

    }
    public Students getAStudent(int id) {
        Students student;
        String query = String.format("Select * from student where id = %d", id);
        
        try {
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
             student  = new Students(id, rs.getString(2), rs.getString(3));}
            else student  = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;

    }
    public boolean isAlreadyIssue(String isbn, int id) {
        String query = String.format("select * from `student issued book` where `Student ID` = %d and ISBN = '%s' and status='%s'", id, isbn, "Borrowing");
        boolean result = true;
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) result = false;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result ;
    }
    public void updateQuantity(String isbn) {
        String query = String.format("update book set quantity=quantity-1 where isbn = '%s'", isbn);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addNewStudentIssueBook (StudentIssueBook studentIssueBook) {
        String query = String.format("Insert into `student issued book` values (%d, '%s', '%s', '%s', '%s')",
                studentIssueBook.getId(), studentIssueBook.getIsbn(),
                studentIssueBook.getIssueDate(),studentIssueBook.getDueDate(),"Borrowing");
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getDefaulter() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(currentTime);
        String query = String.format("Select count(*) from `student issued book` where `Due date` < '%s' and status = 'Borrowing'", todayDate);
        int count = 0;
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) count = rs.getInt(1);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    public ArrayList<Books> getAllBook() {
        ArrayList<Books> list = new ArrayList<>();
        String query = ("Select * from book");
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(new Books(rs.getString(1), rs.getString(2),rs.getString(3),rs.getInt(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public ArrayList<Students> getAllStudent() {
        ArrayList<Students> list = new ArrayList<>();
        String query = ("Select * from student");
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(new Students(rs.getInt(1), rs.getString(2),rs.getString(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
