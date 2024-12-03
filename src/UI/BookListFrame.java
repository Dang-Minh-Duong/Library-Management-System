package UI;

import API.GoogleBooksService;
import model.Books;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class BookListFrame extends JFrame {

    public BookListFrame(String keyword) {
        setTitle("Book List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        try {
            GoogleBooksService service = new GoogleBooksService();
            List<Books> books = service.getBookList(keyword);

            if (books != null && !books.isEmpty()) {
                for (Books book : books) {
                    JPanel bookPanel = new JPanel();
                    bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));
                    JLabel isbnLabel = new JLabel("ISBN: " + book.getISBN());
                    JLabel titleLabel = new JLabel("Title: " + book.getName());
                    JLabel authorLabel = new JLabel("Author: " + book.getAuthor());
                    JLabel imageLabel = new JLabel();

                    if (book.getImageUrl() != null) {
                        ImageIcon icon = new ImageIcon(new URL(book.getImageUrl()));
                        imageLabel.setIcon(icon);
                    }
                    bookPanel.add(isbnLabel);
                    bookPanel.add(titleLabel);
                    bookPanel.add(authorLabel);
                    bookPanel.add(imageLabel);
                    resultPanel.add(bookPanel);
                    resultPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Thêm khoảng cách giữa các sách
                }
            } else {
                resultPanel.add(new JLabel("No books found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultPanel.add(new JLabel("Error occurred while retrieving book details"));
        }

        add(new JScrollPane(resultPanel), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String keyword = JOptionPane.showInputDialog(null, "Enter book keyword:", "Search Books", JOptionPane.QUESTION_MESSAGE);
            if (keyword != null && !keyword.trim().isEmpty()) {
                BookListFrame frame = new BookListFrame(keyword);
                frame.setVisible(true);
            }
        });
    }
}
