/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import API.GoogleBooksService;
import dao.BookDao;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import model.Books;

/**
 *
 * @author DMX
 */
public class ManageBooks extends javax.swing.JFrame {

    /**
     * Creates new form ManageBooks
     */
    private DefaultTableModel model;

    public ManageBooks() {
        initComponents();
        setBookDetailsToTable();
    }

    // Invalid isbn.
    private boolean isValidISBN13(String isbn) {
        // Biểu thức chính quy để kiểm tra định dạng ISBN-13 có dấu gạch
        String regex = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d{1}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(isbn);
        return matcher.matches();
    }

    // To set the book details into the table.
    public void setBookDetailsToTable() {
        BookDao bookDao = new BookDao();
        List<Books> books = bookDao.getAllBooks();

        model = (DefaultTableModel) tb_booksDetail.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        for (Books book : books) {
            Object[] row = {book.getISBN(), book.getName(), book.getAuthor(), book.getQuantity()};
            model.addRow(row);
        }
    }

    // Phuong thuc xu ly them sach.
    private void addBook() {
        String isbn = txt_ISBN.getText();
        String name = txt_bookName.getText();
        String author = txt_authorName.getText();
        String quantityText = txt_quantity.getText();

        if (isbn.isEmpty() || name.isEmpty() || author.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all information");
            return;
        }
        // Yêu cầu nhập ISBN hợp lệ.
        if (!isValidISBN13(isbn)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ISBN-13 format (e.g., 978-3-16-148410-0).");
            return; // Dừng lại nếu ISBN không hợp lệ
        }
        // Kiểm tra dữ liệu nhập
        int quantity;
        try {
            quantity = Integer.parseInt(txt_quantity.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Dừng thực thi nếu số lượng không hợp lệ
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Books book = new Books(isbn, name, author, quantity);
        BookDao bookDAO = new BookDao();

        if (bookDAO.addBook(book)) {
            JOptionPane.showMessageDialog(this, "Add book successful!", "Message", JOptionPane.INFORMATION_MESSAGE);
            setBookDetailsToTable(); // Cập nhật bảng

            // Xóa nội dung các textField sau khi thêm sách
            txt_ISBN.setText("");
            txt_bookName.setText("");
            txt_authorName.setText("");
            txt_quantity.setText(""); // Xóa textField số lượng
        } else {
            JOptionPane.showMessageDialog(this, "Fail to add book", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update book.
    private void updateBook() {
        String isbn = txt_ISBN.getText();
        String name = txt_bookName.getText();
        String author = txt_authorName.getText();
        String quantityText = txt_quantity.getText();

        if (isbn.isEmpty() || name.isEmpty() || author.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all information");
            return;
        }

        // Kiểm tra dữ liệu nhập
        int quantity;
        try {
            quantity = Integer.parseInt(txt_quantity.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Dừng thực thi nếu số lượng không hợp lệ
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng Book từ dữ liệu nhập
        Books book = new Books(isbn, name, author, quantity);

        // Gọi phương thức updateBook từ BookDAO
        BookDao bookDAO = new BookDao();
        boolean success = bookDAO.updateBook(book);

        // Thông báo kết quả
        if (success) {
            JOptionPane.showMessageDialog(this, "Update book successful", "Message", JOptionPane.INFORMATION_MESSAGE);

            // Làm mới bảng hiển thị sách
            setBookDetailsToTable();

            // Xóa nội dung các textField sau khi thêm sách
            txt_ISBN.setText("");
            txt_bookName.setText("");
            txt_authorName.setText("");
            txt_quantity.setText(""); // Xóa textField số lượng
        } else {
            JOptionPane.showMessageDialog(this, "Fail to update book", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete book.
    private void deleteBook() {
        int selectedRow = tb_booksDetail.getSelectedRow(); // Lấy dòng được chọn trong bảng

        if (selectedRow == -1) { // Nếu không có dòng nào được chọn
            JOptionPane.showMessageDialog(this, "Please select a book to delete", "Message", JOptionPane.WARNING_MESSAGE);
            return; // Dừng lại nếu không có dòng được chọn
        }

        // Lấy ISBN từ dòng được chọn (cột đầu tiên là ISBN)
        String isbn = (String) model.getValueAt(selectedRow, 0);

        // Xác nhận người dùng muốn xóa
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            BookDao bookDAO = new BookDao(); // Khởi tạo đối tượng BookDAO
            boolean success = bookDAO.deleteBook(isbn); // Gọi phương thức deleteBook() trong BookDAO

            if (success) {
                JOptionPane.showMessageDialog(this, "Delete successful", "Message", JOptionPane.INFORMATION_MESSAGE);

                // Làm mới bảng sau khi xóa sách
                setBookDetailsToTable();

                // Xóa nội dung các textField sau khi thêm sách
                txt_ISBN.setText("");
                txt_bookName.setText("");
                txt_authorName.setText("");
                txt_quantity.setText(""); // Xóa textField số lượng
            } else {
                JOptionPane.showMessageDialog(this, "Fail to delete book", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Tìm kiếm sách trong cơ sở dữ liệu
    private Books searchBookInDatabase(String isbn) {
        BookDao bookDao = new BookDao();
        List<Books> books = bookDao.getAllBooks();

        for (Books book : books) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }

        return null; // Nếu không tìm thấy sách trong cơ sở dữ liệu
    }

    // Tìm kiếm sách từ Google Books API
    private Books searchBookInGoogleBooks(String isbn) {
        try {
            GoogleBooksService googleBooksService = new GoogleBooksService();
            return googleBooksService.searchBookByISBN(isbn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while fetching data from Google Books API: " + e.getMessage());
            return null;
        }
    }

    // Cập nhật thông tin sách vào giao diện
    private void updateBookDetails(Books book) {
        if (book != null) {
            txt_bookName.setText(book.getName());
            txt_authorName.setText(book.getAuthor());
            txt_quantity.setText(String.valueOf(book.getQuantity()));
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_back = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_ISBN = new app.bolivia.swing.JCTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_bookName = new app.bolivia.swing.JCTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_authorName = new app.bolivia.swing.JCTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_quantity = new app.bolivia.swing.JCTextField();
        jLabel8 = new javax.swing.JLabel();
        addBook = new rojerusan.RSMaterialButtonCircle();
        bookDelete = new rojerusan.RSMaterialButtonCircle();
        updateBook = new rojerusan.RSMaterialButtonCircle();
        searchBook = new rojerusan.RSMaterialButtonCircle();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_booksDetail = new rojeru_san.complementos.RSTableMetro();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(580, 830));

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));

        jLabel_back.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel_back.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Rewind_48px.png"))); // NOI18N
        jLabel_back.setText("Back");
        jLabel_back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_backMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_back, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_back, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Enter Book ISBN");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N

        txt_ISBN.setBackground(new java.awt.Color(102, 102, 255));
        txt_ISBN.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_ISBN.setPlaceholder("Enter book ISBN ...");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N

        txt_bookName.setBackground(new java.awt.Color(102, 102, 255));
        txt_bookName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_bookName.setPlaceholder("Enter book name ...");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter Book Name");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N

        txt_authorName.setBackground(new java.awt.Color(102, 102, 255));
        txt_authorName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_authorName.setPlaceholder("Author name ...");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Author Name");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N

        txt_quantity.setBackground(new java.awt.Color(102, 102, 255));
        txt_quantity.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_quantity.setPlaceholder("Quantity ...");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Quantity");

        addBook.setBackground(new java.awt.Color(255, 51, 51));
        addBook.setText("ADD");
        addBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookActionPerformed(evt);
            }
        });

        bookDelete.setBackground(new java.awt.Color(255, 51, 51));
        bookDelete.setText("DELETE");
        bookDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookDeleteActionPerformed(evt);
            }
        });

        updateBook.setBackground(new java.awt.Color(255, 51, 51));
        updateBook.setText("UPDATE");
        updateBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBookActionPerformed(evt);
            }
        });

        searchBook.setBackground(new java.awt.Color(255, 51, 51));
        searchBook.setText("Search");
        searchBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBookActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_bookName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_authorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBook, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBook, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bookDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBook, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(275, 275, 275)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(89, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(29, 29, 29)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_bookName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(28, 28, 28))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_authorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)))
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBook, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchBook, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBook, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 216, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(402, 402, 402)
                    .addComponent(jLabel10)
                    .addContainerGap(402, Short.MAX_VALUE)))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tb_booksDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ISBN", "Name", "Author", "Quantity"
            }
        ));
        tb_booksDetail.setColorBackgoundHead(new java.awt.Color(0, 102, 102));
        tb_booksDetail.setColorBordeFilas(new java.awt.Color(0, 102, 102));
        tb_booksDetail.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tb_booksDetail.setColorFilasForeground1(new java.awt.Color(0, 102, 102));
        tb_booksDetail.setRowHeight(40);
        tb_booksDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_booksDetailMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tb_booksDetail);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 51));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Books_52px_1.png"))); // NOI18N
        jLabel9.setText("Manage Books");

        jPanel4.setBackground(new java.awt.Color(255, 51, 51));
        jPanel4.setPreferredSize(new java.awt.Dimension(402, 5));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1118, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1087, 638));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_backMouseClicked
        Home home = new Home();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel_backMouseClicked

    private void addBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookActionPerformed
        addBook();
    }//GEN-LAST:event_addBookActionPerformed

    private int selectedRow = -1;

    private void tb_booksDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_booksDetailMouseClicked

        int rowNo = tb_booksDetail.getSelectedRow();

        if (rowNo == selectedRow) {
            // Nếu nhấn lại vào cùng một hàng, bỏ chọn
            tb_booksDetail.clearSelection();
            selectedRow = -1; // Reset trạng thái hàng được chọn

            // Xóa thông tin trong các textField
            txt_ISBN.setText("");
            txt_bookName.setText("");
            txt_authorName.setText("");
            txt_quantity.setText("");

            // Mở trường ISBN để không cho phép chỉnh sửa
            txt_ISBN.setEditable(true);
        } else {
            selectedRow = rowNo;

            TableModel model = tb_booksDetail.getModel();

            txt_ISBN.setText(model.getValueAt(rowNo, 0).toString());
            txt_bookName.setText(model.getValueAt(rowNo, 1).toString());
            txt_authorName.setText(model.getValueAt(rowNo, 2).toString());
            txt_quantity.setText(model.getValueAt(rowNo, 3).toString());

            // Khóa trường ISBN để không cho phép chỉnh sửa
            txt_ISBN.setEditable(false);
        }

    }//GEN-LAST:event_tb_booksDetailMouseClicked

    private void updateBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBookActionPerformed
        updateBook();
    }//GEN-LAST:event_updateBookActionPerformed

    private void bookDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookDeleteActionPerformed
        deleteBook();
    }//GEN-LAST:event_bookDeleteActionPerformed

    private void searchBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBookActionPerformed
        String isbn = txt_ISBN.getText().trim();

        // Yêu cầu người dùng nhập ISBN hợp lệ.
        if (!isValidISBN13(isbn)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ISBN-13 format (e.g., 978-3-16-148410-0).");
            return; // Dừng lại nếu ISBN không hợp lệ
        }
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter ISBN!");
            return;
        }

        // Tạo luồng để tìm kiếm trong cơ sở dữ liệu và Google Books API
        new Thread(() -> {
            Books book = searchBookInDatabase(isbn);
            if (book == null) {
                book = searchBookInGoogleBooks(isbn);
            }
            updateBookDetails(book);
        }).start();
    }//GEN-LAST:event_searchBookActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageBooks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle addBook;
    private rojerusan.RSMaterialButtonCircle bookDelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_back;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private rojerusan.RSMaterialButtonCircle searchBook;
    private rojeru_san.complementos.RSTableMetro tb_booksDetail;
    private app.bolivia.swing.JCTextField txt_ISBN;
    private app.bolivia.swing.JCTextField txt_authorName;
    private app.bolivia.swing.JCTextField txt_bookName;
    private app.bolivia.swing.JCTextField txt_quantity;
    private rojerusan.RSMaterialButtonCircle updateBook;
    // End of variables declaration//GEN-END:variables
}
