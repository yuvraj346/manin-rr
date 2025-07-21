import java.util.Scanner;

public class Admin extends User {

    public Admin(int userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            String[] options = {
                "1. View All Complaints",
                "2. View All Crime Reports",
                "3. Generate .txt Report",
                "4. View Logs",
                "5. FAQs",
                "6. Logout"
            };
            CLIUtils.printSingleBlockHeading();
            CLIUtils.printBoxedMenu("Admin Menu", options);

            choice = CLIUtils.promptInt(sc, "Enter your choice: ", 1, 6);

            switch (choice) {
                case 1:
                    CLIUtils.printInfo("Showing all complaints...");
                    ComplaintManager.viewAllComplaints();
                    break;
                case 2:
                    CLIUtils.printInfo("Showing all crime reports...");
                    CrimeManager.viewAllCrimes();
                    break;
                case 3:
                    handleReportGeneration();
                    break;
                case 4:
                    CLIUtils.printInfo("Viewing system logs...");
                    ActionTracker.viewLog();
                    ActionTracker.log("Admin", "Viewed logs");
                    break;
                case 5:
                    showFAQs();
                    break;
                case 6:
                    CLIUtils.printInfo("Logged out successfully.");
                    ActionTracker.log("Admin", "Logged out");
                    break;
                default:
                    CLIUtils.printError("Invalid choice. Please select 1-6.");
            }
            if (choice != 6) CLIUtils.waitForEnter();
        } while (choice != 6);
    }

    private void handleReportGeneration() {
        CLIUtils.printInfo("Generating report file...");
        try {
            ReportGenerator.generateAllComplaintsReport();
            ActionTracker.log("Admin", "Generated system-wide .txt report");
            CLIUtils.printSuccess("Report generated successfully!");
        } catch (Exception e) {
            CLIUtils.printError("Error generating report: " + e.getMessage());
        }
    }

    private void showFAQs() {
        String[] faqs = {
            "Q: How do I generate a report?",
            "A: Use the 'Generate .txt Report' option.",
            "Q: How do I view system logs?",
            "A: Use the 'View Logs' option to see all system activities.",
            "Q: Can I modify system settings?",
            "A: Use the 'System Settings' option (coming soon)."
        };
        CLIUtils.printBoxedInfo("Admin FAQs", faqs);
    }
}
