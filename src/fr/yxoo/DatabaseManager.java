package fr.yxoo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    private final String dbFilePath;
    private static KetheriumTools plugin = null;

    public DatabaseManager(KetheriumTools plugin, String dbFilePath) {
        this.dbFilePath = dbFilePath;
        this.plugin = plugin;
    }

    private void createCollectedTableIfNotExists(UUID playerUUID, String job) {
        String collectedTable = job + "_" + playerUUID.toString().replace("-", "");
        String sql = "CREATE TABLE IF NOT EXISTS " + collectedTable + " (" +
                "reward INTEGER PRIMARY KEY)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addReward(UUID playerUUID, String job, int number) {
        String collectedTable = job + "_" + playerUUID.toString().replace("-", "");
        createCollectedTableIfNotExists(playerUUID, job);

        String sql = "INSERT OR IGNORE INTO " + collectedTable + " (reward) VALUES (?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, number);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasReward(UUID playerUUID, String job, int number) {
        String collectedTable = job + "_" + playerUUID.toString().replace("-", "");
        createCollectedTableIfNotExists(playerUUID, job);

        String sql = "SELECT 1 FROM " + collectedTable + " WHERE reward = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, number);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
