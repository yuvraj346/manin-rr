import java.sql.*;

public class UserManager {

    public static Citizen registerUser(String username, String password, String name, String countryCode, String phone,
                                       String state, String city, String landmark, String houseNo, int age, String email) {
        int userId = -1;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Input validation
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                countryCode == null || countryCode.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                state == null || state.trim().isEmpty() ||
                city == null || city.trim().isEmpty() ||
                landmark == null || landmark.trim().isEmpty() ||
                houseNo == null || houseNo.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
                System.out.println("❌ All fields are required and cannot be null or empty.");
                return null;
            }

            con = DBConnection.connect();
            if (con == null) {
                System.out.println("❌ Database connection failed.");
                return null;
            }

            // Check if username already exists
            String checkSql = "SELECT user_id FROM users WHERE username = ?";
            ps = con.prepareStatement(checkSql);
            ps.setString(1, username.trim());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                System.out.println("❌ Username already exists. Please choose a different username.");
                return null;
            }

            // Insert new user
            String sql = "INSERT INTO users (username, password, name, country_code, phone, state, city, landmark, house_no, age, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            ps.setString(3, name.trim());
            ps.setString(4, countryCode.trim());
            ps.setString(5, phone.trim());
            ps.setString(6, state.trim());
            ps.setString(7, city.trim());
            ps.setString(8, landmark.trim());
            ps.setString(9, houseNo.trim());
            ps.setInt(10, age);
            ps.setString(11, email.trim());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                    System.out.println("✅ User registered with ID: " + userId);
                }
            } else {
                System.out.println("❌ No rows inserted. Something went wrong.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
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

        if (userId != -1) {
            return new Citizen(userId, username, password, name, countryCode, phone, state, city, landmark, houseNo, age, email);
        } else {
            return null;
        }
    }

    public static Citizen getUser(String username, String password) {
        Citizen c = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            // Input validation
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                System.out.println("❌ Username and password are required.");
                return null;
            }

            con = DBConnection.connect();
            if (con == null) {
                System.out.println("❌ Database connection failed.");
                return null;
            }

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String countryCode = rs.getString("country_code");
                String phone = rs.getString("phone");
                String state = rs.getString("state");
                String city = rs.getString("city");
                String landmark = rs.getString("landmark");
                String houseNo = rs.getString("house_no");
                int age = rs.getInt("age");
                String email = rs.getString("email");

                c = new Citizen(userId, username, password, name, countryCode, phone, state, city, landmark, houseNo, age, email);
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error fetching user: " + e.getMessage());
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

        return c;
    }

    public static Admin getAdmin(String username, String password) {
        Admin admin = null;
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
                String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'admin'";
                ps = con.prepareStatement(sql);
                ps.setString(1, username.trim());
                ps.setString(2, password.trim());
                rs = ps.executeQuery();
                if (rs.next()) {
                    int adminId = rs.getInt("user_id");
                    String uname = rs.getString("username");
                    String pwd = rs.getString("password");
                    admin = new Admin(adminId, uname, pwd);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error fetching admin: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("❌ Error closing database resources: " + e.getMessage());
            }
        }
        // Fallback admin credentials if DB fails or not found
        if (admin == null) {
            if ((username.equals("ravi") && password.equals("123456")) || (username.equals("manin") && password.equals("654321"))) {
                admin = new Admin(-1, username, password); // -1 as dummy ID
            }
        }
        return admin;
    }

    public static int getCitizenCount() {
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

            String sql = "SELECT COUNT(*) FROM users";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
            
        } catch (SQLException e) {
            System.out.println("❌ Database error counting citizens: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error counting citizens: " + e.getMessage());
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
}
