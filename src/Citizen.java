import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Citizen extends User {

    public Citizen(int userId, String username, String password, String name, String countryCode, String phone,
                   String state, String city, String landmark, String houseNo, int age, String email) {
        super(userId, username, password, name, countryCode, phone, state, city, landmark, houseNo, age, email);
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            String[] options = {
                "1. Raise a Request/Suggestion",
                "2. File a Complaint",
                "3. Check Complaint Status",
                "4. View Complaint Dashboard",
                "5. Know Your Officer",
                "6. Ahmedabad Helpline Number",
                "7. Snapshot (Current/History)",
                "8. FAQs",
                "9. Feedback",
                "10. Logout"
            };
            CLIUtils.printSingleBlockHeading();
            CLIUtils.printBoxedMenu("Citizen Dashboard", options);

            choice = CLIUtils.promptInt(sc, "Enter your choice: ", 1, 10);

            switch (choice) {
                case 1:
                    handleSuggestionSubmission(sc);
                    break;
                case 2:
                    handleComplaintFiling(sc);
                    break;
                case 3:
                    CLIUtils.printInfo("Checking complaint status...");
                    ComplaintManager.showComplaintTracker(userId);
                    break;
                case 4:
                    CLIUtils.printInfo("Your Complaint Dashboard:");
                    ComplaintManager.viewComplaintsByUser(userId);
                    break;
                case 5:
                    handleOfficerLookup(sc);
                    break;
                case 6:
                    CLIUtils.printInfo("Ahmedabad Helpline: 100, 108, 181, 1091");
                    break;
                case 7:
                    handleSnapshotView(sc);
                    break;
                case 8:
                    showFAQs();
                    break;
                case 9:
                    handleFeedbackSubmission(sc);
                    break;
                case 10:
                    CLIUtils.printInfo("Logged out successfully.");
                    ActionTracker.log("Citizen_" + userId, "Logged out");
                    break;
                default:
                    CLIUtils.printError("Invalid choice. Please select 1-10.");
            }
            if (choice != 10) CLIUtils.waitForEnter();
        } while (choice != 10);
    }

    private void handleSuggestionSubmission(Scanner sc) {
        String suggestion = CLIUtils.promptString(sc, "Suggestion", true, 10, 200, null, null);
        try {
            Connection con = DBConnection.connect();
            String sql = "INSERT INTO suggestions (user_id, suggestion) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, suggestion);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                CLIUtils.printSuccess("✅ Suggestion submitted successfully!");
                ActionTracker.log("Citizen_" + userId, "Submitted suggestion");
            } else {
                CLIUtils.printError("❌ Failed to save suggestion.");
            }
            con.close();
        } catch (Exception e) {
            CLIUtils.printError("❌ Error while saving suggestion: " + e.getMessage());
        }
    }

    private void handleComplaintFiling(Scanner sc) {
        CLIUtils.printInfo("Choose Complaint Type:");
        CLIUtils.printInfo("1. Civil");
        CLIUtils.printInfo("2. Criminal");
        int complaintType = CLIUtils.promptInt(sc, "Enter type (1-2): ", 1, 2);
        String type = (complaintType == 1) ? "Civil" : "Criminal";
        try {
            ComplaintManager.fileComplaint(userId, type);
            int latestComplaintId = ComplaintManager.getLatestComplaintId(userId);
            if (latestComplaintId > 0) {
                CLIUtils.printInfo("Generating QR Code for your complaint...");
                QRGenerator.generateQRCode(latestComplaintId);
                CLIUtils.printSuccess("Complaint filed successfully! QR Code generated.");
            } else {
                CLIUtils.printError("Failed to file complaint. Please try again.");
            }
        } catch (Exception e) {
            CLIUtils.printError("Error filing complaint: " + e.getMessage());
        }
    }

    private void handleOfficerLookup(Scanner sc) {
        String area = CLIUtils.promptString(sc, "Enter area/city to view officer details", true, 2, 40, null, null);
        // TODO: Implement area-specific officer details if needed
        OfficerManager.viewAllOfficers();
    }

    private void handleSnapshotView(Scanner sc) {
        CLIUtils.printInfo("Snapshot Options:");
        CLIUtils.printInfo("1. Current");
        CLIUtils.printInfo("2. Incident History (6 months)");
        CLIUtils.printInfo("3. Incident History (3 months)");
        CLIUtils.printInfo("4. Incident History (15 days)");
        int snap = CLIUtils.promptInt(sc, "Enter option (1-4): ", 1, 4);
        try {
            CrimeManager.showSnapshot(snap);
        } catch (Exception e) {
            CLIUtils.printError("Error loading snapshot: " + e.getMessage());
        }
    }

    private void showFAQs() {
        String[] faqs = {
            "Q: How do I file a complaint?",
            "A: Use the 'File a Complaint' option and follow the prompts.",
            "Q: How do I check my complaint status?",
            "A: Use the 'Check Complaint Status' option.",
            "Q: Who can see my complaints?",
            "A: Only authorized officers and admins.",
            "Q: How long does it take to resolve a complaint?",
            "A: Resolution time varies based on complaint type and complexity.",
            "Q: Can I update my complaint details?",
            "A: Contact your assigned officer for any updates."
        };
        CLIUtils.printBoxedInfo("FAQs", faqs);
    }

    private void handleFeedbackSubmission(Scanner sc) {
        String feedback = CLIUtils.promptString(sc, "Feedback", true, 10, 200, null, null);
        CLIUtils.printSuccess("Thank you for your feedback!");
        ActionTracker.log("Citizen_" + userId, "Submitted feedback");
    }
}
