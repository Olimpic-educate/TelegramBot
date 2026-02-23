package Repository;

import database.DatabaseManager;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepository {
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, type, quality, category, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.userId());
            ps.setString(2, transaction.type());
            ps.setDouble(3, transaction.quality());
            ps.setString(4, transaction.category());
            ps.setString(5, transaction.data());
            ps.executeUpdate();
        } catch (SQLException exe) {
            exe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double sumByType(int userId, String type) {
        String sql = """
                SELECT COALESCE(SUM(quality),0)
                FROM transactions
                WHERE user_id = ? AND type = ?
                """;
        try (Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
