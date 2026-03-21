package Repository;

import database.DatabaseManager;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, type, amount, category, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.userId());
            ps.setString(2, transaction.type());
            ps.setDouble(3, transaction.amount());
            ps.setString(4, transaction.category());
            ps.setString(5, transaction.data());
            ps.executeUpdate();
        } catch (SQLException exe) {
            exe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> findByUserId(int user_id) {
        List<Transaction> list = new ArrayList();
        String sql = "SELECT * FROM transactions WHERE user_id =?";
        try (Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getString("date"));
                list.add(t);
            }
        }catch (SQLException exe){
            exe.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public Map<String,Double> getStats(int userId){
        Map<String, Double> map = new HashMap<>();
        String sql = """
                SELECT category, SUM(amount)
                FROM transactions
                WHERE user_id = ? AND type = 'expense'
                GROUP BY category
                """;
        try(Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String category = rs.getString(1);
                Double amount = rs.getDouble(2);
                map.put(category, amount);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch (Exception exe){
            exe.printStackTrace();
        }
        return map;
    }



    public double sumByType(int userId, String type) {
        String sql = """
                SELECT COALESCE(SUM(amount),0)
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
