package model;

public class ExtendedIssueBook {
    private String isbn;
    private String bookName;
    private String studentId;
    private String studentName;
    private String issueDate;
    private String dueDate;
    private String status;

    // Constructor đầy đủ
    public ExtendedIssueBook(String isbn, String bookName, String studentId, String studentName, String issueDate, String dueDate, String status) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters cho tất cả các thuộc tính
    public String getIsbn() { return isbn; }
    public String getBookName() { return bookName; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getIssueDate() { return issueDate; }
    public String getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}
