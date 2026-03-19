package Repository;

import database.DatabaseManager;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public void save(User user) {
        String sql = "INSERT OR IGNORE INTO users (telegram_id, username)VALUES( ?,?)";

        try (Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, user.tgId());
            ps.setString(2, user.username());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User findByTelegramId(long tgId) {
        String sql = "SELECT * FROM users WHERE telegram_id = ?";
        try (Connection conn = DatabaseManager.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, tgId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getLong("telegram_id"),
                        rs.getString("username"),
                        rs.getInt("id"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }


}
