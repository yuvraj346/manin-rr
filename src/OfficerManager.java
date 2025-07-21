import java.sql.*;

public class OfficerManager {

    public static int assignOfficer(String area) {
        int officerId = -1;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            // Input validation
            if (area == null || area.trim().isEmpty()) {
                System.out.println("❌ Area cannot be null or empty.");
                return -1;
            }

            con = DBConnection.connect();
            if (con == null) {
                System.out.println("❌ Database connection failed.");
                return -1;
            }

            // Find officer with least complaints
            String sql = "SELECT o.officer_id, COUNT(c.complaint_id) as complaint_count " +
                        "FROM officers o " +
                        "LEFT JOIN complaints c ON o.officer_id = c.officer_id " +
                        "GROUP BY o.officer_id " +
                        "ORDER BY complaint_count ASC " +
                        "LIMIT 1";
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                officerId = rs.getInt("officer_id");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Database error assigning officer: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error assigning officer: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("❌ Error closing database resources: " + e.getMessage());
            }
        }
        
        return officerId;
    }

    public static int getOfficerCount() {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBConnection.connect();
            if (con == null) {
                System.out.println("❌ Database connection failed.");
                return 0;
            }

            String sql = "SELECT COUNT(*) FROM officers";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
            
        } catch (SQLException e) {
            System.out.println("❌ Database error counting officers: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error counting officers: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("❌ Error closing database resources: " + e.getMessage());
            }
        }
        return count;
    }

    public static void viewAllOfficers() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBConnection.connect();
            if (con == null) {
                System.out.println("❌ Database connection failed.");
                return;
            }

            String sql = "SELECT * FROM officers";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("\n👮 All Officers:");
            while (rs.next()) {
                System.out.println("Officer ID: " + rs.getInt("officer_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Badge: " + rs.getString("badge_number"));
                System.out.println("-----------------------------");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Database error viewing officers: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error viewing officers: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("❌ Error closing database resources: " + e.getMessage());
            }
        }
    }

    public static Officer getOfficer(String username, String password) {
        Officer officer = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                System.out.println("❌ Username and password are required.");
                return null;
            }
            // Password must be exactly 6 numeric digits
            if (!password.matches("^\\d{6}$")) {
                System.out.println("❌ Password must be exactly 6 numeric digits.");
                return null;
            }
            con = DBConnection.connect();
            if (con == null) {
                System.out.println("❌ Database connection failed.");
            } else {
                String sql = "SELECT * FROM officers WHERE username = ? AND password = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, username.trim());
                ps.setString(2, password.trim());
                rs = ps.executeQuery();
                if (rs.next()) {
                    int officerId = rs.getInt("officer_id");
                    String uname = rs.getString("username");
                    String pwd = rs.getString("password");
                    officer = new Officer(officerId, uname, pwd);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error fetching officer: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("❌ Error closing database resources: " + e.getMessage());
            }
        }
        // Fallback officer credentials if DB fails or not found
        if (officer == null) {
            if (username.equals("manin") && password.equals("654321")) {
                officer = new Officer(-1, username, password); // -1 as dummy ID
            }
        }
        return officer;
    }
}
