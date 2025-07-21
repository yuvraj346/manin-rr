import java.util.*;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;

public class ReportGenerator {

    public static void generateAllComplaintsReport() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PrintWriter writer = null;
        
        try {
            con = DBConnection.connect();
            if (con == null) {
                CLIUtils.printError("Database connection failed.");
                return;
            }
            
            String sql = "SELECT c.*, u.name as user_name, u.phone as user_phone FROM complaints c " +
                        "LEFT JOIN users u ON c.user_id = u.user_id " +
                        "ORDER BY c.filed_on DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            String filename = "All_Complaints_Report.txt";
            writer = new PrintWriter(new FileWriter(filename));

            writer.println("=".repeat(80));
            writer.println("                    RAPIDRESOLVE - ALL COMPLAINTS REPORT");
            writer.println("=".repeat(80));
            writer.println("Generated on: " + LocalDate.now());
            writer.println("Total Complaints: " + ComplaintManager.getTotalComplaintCount());
            writer.println("-".repeat(80));

            while (rs.next()) {
                writer.println("Complaint ID: " + rs.getInt("complaint_id"));
                writer.println("Filed by: " + rs.getString("user_name") + " (" + rs.getString("user_phone") + ")");
                writer.println("Area: " + rs.getString("area"));
                writer.println("Type: " + rs.getString("type"));
                writer.println("Description: " + rs.getString("description"));
                writer.println("Status: " + rs.getString("status"));
                writer.println("Officer ID: " + rs.getInt("officer_id"));
                writer.println("Filed on: " + rs.getString("filed_on"));
                writer.println("-".repeat(40));
            }

            writer.close();
            CLIUtils.printSuccess("✅ Report generated successfully: " + filename);
            ActionTracker.log("Admin", "Generated all complaints report");

        } catch (SQLException e) {
            CLIUtils.printError("Database error generating report: " + e.getMessage());
        } catch (IOException e) {
            CLIUtils.printError("File error generating report: " + e.getMessage());
        } catch (Exception e) {
            CLIUtils.printError("Error generating report: " + e.getMessage());
        } finally {
            try {
                if (writer != null) writer.close();
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                CLIUtils.printError("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void generateUserComplaintsReport(int userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PrintWriter writer = null;
        
        try {
            con = DBConnection.connect();
            if (con == null) {
                CLIUtils.printError("Database connection failed.");
                return;
            }
            
            String sql = "SELECT * FROM complaints WHERE user_id = ? ORDER BY filed_on DESC";
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            String filename = "User_" + userId + "_Complaints.txt";
            writer = new PrintWriter(new FileWriter(filename));

            writer.println("=".repeat(60));
            writer.println("           RAPIDRESOLVE - USER COMPLAINTS REPORT");
            writer.println("=".repeat(60));
            writer.println("User ID: " + userId);
            writer.println("Generated on: " + LocalDate.now());
            writer.println("-".repeat(60));

            boolean hasComplaints = false;
            while (rs.next()) {
                hasComplaints = true;
                writer.println("Complaint ID: " + rs.getInt("complaint_id"));
                writer.println("Area: " + rs.getString("area"));
                writer.println("Type: " + rs.getString("type"));
                writer.println("Description: " + rs.getString("description"));
                writer.println("Status: " + rs.getString("status"));
                writer.println("Officer ID: " + rs.getInt("officer_id"));
                writer.println("Filed on: " + rs.getString("filed_on"));
                writer.println("-".repeat(30));
            }

            if (!hasComplaints) {
                writer.println("No complaints found for this user.");
            }

            writer.close();
            CLIUtils.printSuccess("✅ Report generated successfully: " + filename);
            ActionTracker.log("Citizen_" + userId, "Generated personal complaints report");

        } catch (SQLException e) {
            CLIUtils.printError("Database error generating user report: " + e.getMessage());
        } catch (IOException e) {
            CLIUtils.printError("File error generating user report: " + e.getMessage());
        } catch (Exception e) {
            CLIUtils.printError("Error generating user report: " + e.getMessage());
        } finally {
            try {
                if (writer != null) writer.close();
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                CLIUtils.printError("Error closing resources: " + e.getMessage());
            }
        }
    }
}
