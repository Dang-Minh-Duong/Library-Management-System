/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

/**
 *
 * @author Lenovo
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Frame Example");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo nút Exit
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Kết thúc chương trình
            }
        });

        // Thêm nút vào Frame
        frame.add(exitButton);
        frame.setLayout(null);
        exitButton.setBounds(150, 120, 100, 40);

        frame.setVisible(true);
    }
}
