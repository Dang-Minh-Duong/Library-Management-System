package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class DefaulterDao {

    public int getNumberOfDefaulter() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(currentTime);
        String query = String.format("SELECT COUNT(*) FROM `student issued book` WHERE `Due date` < '%s' AND status = 'Borrowing'", todayDate);
        int count = 0;

        try (Connection con = DBconnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
}
