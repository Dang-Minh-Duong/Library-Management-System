/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import dao.AdminDao;
import dao.BookDao;
import dao.IssueBookDao;
import dao.StudentDao;

/**
 *
 * @author DMX
 */
public class ModelFactory {
    // Phương thức tạo đối tượng BookDao
    public static BookDao createBookDao() {
        return new BookDao();
    }

    // Phương thức tạo đối tượng AdminDao
    public static AdminDao createAdminDao() {
        return new AdminDao();
    }

    // Phương thức tạo đối tượng IssueBookDao
    public static IssueBookDao createIssueBookDao() {
        return new IssueBookDao();
    }

    // Phương thức tạo đối tượng StudentDao
    public static StudentDao createStudentDao() {
        return new StudentDao();
    }
}
