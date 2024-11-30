/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import model.Books;
/**
 *
 * @author DMX
 */
public class GoogleBooksService {
    public Books searchBookByISBN(String isbn) throws Exception {
        String apiKey = "AIzaSyCVwgDkWJRhmnT7_M1cLNvaod46w2x7wPY";
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + apiKey;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        GoogleBooksResponse booksResponse = gson.fromJson(response.toString(), GoogleBooksResponse.class);
        if (booksResponse.getItems() != null && booksResponse.getItems().length > 0) {
            GoogleBooksResponse.Item item = booksResponse.getItems()[0];  // Lấy phần tử đầu tiên trong mảng items
            Books book = new Books();
            book.setISBN(isbn);
            book.setName(item.getVolumeInfo().getTitle());
            book.setAuthor(item.getVolumeInfo().getAuthors() != null ? String.join(", ", item.getVolumeInfo().getAuthors()) : "Unknown");
            book.setQuatity(0);
            
            return book;
        }

        return null; // Không tìm thấy thông tin sách
    }
}
