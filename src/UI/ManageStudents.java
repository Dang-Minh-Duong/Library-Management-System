/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import dao.StudentDao;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import model.ModelFactory;
import model.Students;

/**
 *
 * @author DMX
 */
public class ManageStudents extends javax.swing.JFrame {

    /**
     * Creates new form ManageBooks
     */
    private DefaultTableModel model;
    private String idText, name, university;
    private int id;
    private final StudentDao studentDao = ModelFactory.createStudentDao();

    public ManageStudents() {
        initComponents();
        setStudentDetailsToTable();
    }

    private void clear() {
        txt_studentId.setText("");
        txt_studentName.setText("");
        txt_university.setText("");
    }

    // Đưa thông tin sinh viên vào bảng
    private void setStudentDetailsToTable() {
        List<Students> students = studentDao.getAllStudents(); // Lấy danh sách sinh viên từ cơ sở dữ liệu

        // Lấy mô hình dữ liệu của bảng
        model = (DefaultTableModel) tb_studentsDetail.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        // Lặp qua danh sách sinh viên và thêm từng sinh viên vào bảng
        for (Students student : students) {
            Object[] row = {student.getId(), student.getName(), student.getUniversity()};
            model.addRow(row);
        }
        tb_studentsDetail.setDefaultEditor(Object.class, null); // Không cho phép edit bảng

    }

    private void getInput() {
        idText = txt_studentId.getText().trim();
        name = txt_studentName.getText().trim();
        university = txt_university.getText().trim();

    }

    // Lấy dữ liệu và kiểm tra
    private boolean checkValidID() {

        // Kiểm tra dữ liệu nhập vào ID (phải là số nguyên dương)
        try {
            id = Integer.parseInt(idText); // Chuyển đổi ID từ String sang int
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }

    private boolean checkNullInfo() {
        if (idText.isEmpty() || name.isEmpty() || university.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all information", "Massage", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean checkValidNameNUniversity() {
        // Kiểm tra dữ liệu nhập vào tên sinh viên
        if (name.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Student name cannot contain numbers", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra dữ liệu nhập vào tên Trường
        if (university.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid university", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Add student.
    private void addStudent() {
        getInput();
        if (checkNullInfo() && checkValidID() && checkValidNameNUniversity()) {
            Students student = new Students(id, name, university);
            boolean success = studentDao.addStudent(student);

            if (success) {
                JOptionPane.showMessageDialog(this, "Add student successful", "Message", JOptionPane.INFORMATION_MESSAGE);

                // Làm mới bảng hiển thị thông tin sinh viên
                setStudentDetailsToTable();

                // Xóa nội dung các text field sau khi thêm
                clear();
            } else {
                JOptionPane.showMessageDialog(this, "ID exist", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Update student.
    private void updateStudent() {
        getInput();
        if (checkNullInfo() && checkValidID() && checkValidNameNUniversity()) {
            Students student = new Students(id, name, university);
            boolean success = studentDao.updateStudent(student);

            // Thông báo kết quả
            if (success) {
                JOptionPane.showMessageDialog(this, "Update student successful", "Message", JOptionPane.INFORMATION_MESSAGE);

                // Làm mới bảng hiển thị sinh viên
                setStudentDetailsToTable();

                // Xóa nội dung các textField sau khi cập nhật
                clear();
                // Cho phép chỉnh sửa lại trường ID khi hoàn thành cập nhật
                txt_studentId.setEditable(true);
            } else {
                JOptionPane.showMessageDialog(this, "ID exist", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    // Delete student.
    private void deleteStudent() {
        getInput();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all information", "Massage", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (checkValidID()) {

            // Xác nhận người dùng muốn xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                boolean success = studentDao.deleteStudent(id); // Gọi phương thức deleteStudent() trong StudentDao

                if (success) {
                    JOptionPane.showMessageDialog(this, "Delete student successful", "Message", JOptionPane.INFORMATION_MESSAGE);

                    // Làm mới bảng sau khi xóa sinh viên
                    setStudentDetailsToTable();

                    // Xóa nội dung các textField sau khi xóa sinh viên
                    txt_studentId.setText("");
                    txt_studentName.setText("");
                    txt_university.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Fail to delete this student because it has in issued book list", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Tìm kiếm sinh viên.
    private void searchStudent() {
        getInput();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all information", "Massage", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (checkValidID()) {
            // Kiểm tra sinh viên có tồn tại trong cơ sở dữ liệu không
            Students student = studentDao.getAStudent(id);
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên với ID " + id, "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Hiển thị thông tin sinh viên vào các textField
            txt_studentName.setText(student.getName());
            txt_university.setText(student.getUniversity());
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
        txt_studentId = new app.bolivia.swing.JCTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_studentName = new app.bolivia.swing.JCTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_university = new app.bolivia.swing.JCTextField();
        jLabel6 = new javax.swing.JLabel();
        addStudent = new rojerusan.RSMaterialButtonCircle();
        deleteStudent = new rojerusan.RSMaterialButtonCircle();
        updateStudent = new rojerusan.RSMaterialButtonCircle();
        searchStudent = new rojerusan.RSMaterialButtonCircle();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_studentsDetail = new rojeru_san.complementos.RSTableMetro();
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
                .addGap(14, 14, 14))
        );

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Enter student Id");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N

        txt_studentId.setBackground(new java.awt.Color(102, 102, 255));
        txt_studentId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_studentId.setPlaceholder("Enter student Id ...");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N

        txt_studentName.setBackground(new java.awt.Color(102, 102, 255));
        txt_studentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_studentName.setPlaceholder("Enter name ...");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter name");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N

        txt_university.setBackground(new java.awt.Color(102, 102, 255));
        txt_university.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_university.setPlaceholder("Enter university ...");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Enter university");

        addStudent.setBackground(new java.awt.Color(255, 51, 51));
        addStudent.setText("ADD");
        addStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentActionPerformed(evt);
            }
        });

        deleteStudent.setBackground(new java.awt.Color(255, 51, 51));
        deleteStudent.setText("DELETE");
        deleteStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStudentActionPerformed(evt);
            }
        });

        updateStudent.setBackground(new java.awt.Color(255, 51, 51));
        updateStudent.setText("UPDATE");
        updateStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStudentActionPerformed(evt);
            }
        });

        searchStudent.setBackground(new java.awt.Color(255, 51, 51));
        searchStudent.setText("Search");
        searchStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStudentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_university, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                                    .addComponent(txt_studentName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_studentId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_studentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_studentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(35, 35, 35)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_university, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 315, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tb_studentsDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student Id", "Name", "University"
            }
        ));
        tb_studentsDetail.setColorBackgoundHead(new java.awt.Color(0, 102, 102));
        tb_studentsDetail.setColorBordeFilas(new java.awt.Color(0, 102, 102));
        tb_studentsDetail.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tb_studentsDetail.setColorFilasForeground1(new java.awt.Color(0, 102, 102));
        tb_studentsDetail.setRowHeight(40);
        tb_studentsDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_studentsDetailMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tb_studentsDetail);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 51));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/AddNewBookIcons/icons8_Student_Male_100px.png"))); // NOI18N
        jLabel9.setText("Manage Students");

        jPanel4.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        setSize(new java.awt.Dimension(1203, 556));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_backMouseClicked
        Home home = new Home();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel_backMouseClicked

    private void addStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentActionPerformed
        addStudent();
    }//GEN-LAST:event_addStudentActionPerformed

    private int selectedRow = -1;

    private void tb_studentsDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_studentsDetailMouseClicked

        int rowNo = tb_studentsDetail.getSelectedRow();

        if (rowNo == selectedRow) {
            // Nếu nhấn lại vào cùng một hàng, bỏ chọn
            tb_studentsDetail.clearSelection();
            selectedRow = -1; // Reset trạng thái hàng được chọn

            // Xóa thông tin trong các textField
            clear();
            // mở trường studentId để không cho phép chỉnh sửa
            txt_studentId.setEditable(true);
        } else {
            selectedRow = rowNo;

            TableModel model = tb_studentsDetail.getModel();

            txt_studentId.setText(model.getValueAt(rowNo, 0).toString());
            txt_studentName.setText(model.getValueAt(rowNo, 1).toString());
            txt_university.setText(model.getValueAt(rowNo, 2).toString());

        }

    }//GEN-LAST:event_tb_studentsDetailMouseClicked

    private void updateStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStudentActionPerformed
        updateStudent();
    }//GEN-LAST:event_updateStudentActionPerformed

    private void deleteStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStudentActionPerformed
        deleteStudent();
    }//GEN-LAST:event_deleteStudentActionPerformed

    private void searchStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStudentActionPerformed
        searchStudent();
    }//GEN-LAST:event_searchStudentActionPerformed

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
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageStudents().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle addStudent;
    private rojerusan.RSMaterialButtonCircle deleteStudent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_back;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private rojerusan.RSMaterialButtonCircle searchStudent;
    private rojeru_san.complementos.RSTableMetro tb_studentsDetail;
    private app.bolivia.swing.JCTextField txt_studentId;
    private app.bolivia.swing.JCTextField txt_studentName;
    private app.bolivia.swing.JCTextField txt_university;
    private rojerusan.RSMaterialButtonCircle updateStudent;
    // End of variables declaration//GEN-END:variables
}
