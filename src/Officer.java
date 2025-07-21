import java.util.Scanner;

public class Officer extends User {

    public Officer(int userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            String[] options = {
                "1. View Assigned Complaints",
                "2. Update Complaint Status",
                "3. File Crime Record",
                "4. View Crime Records",
                "5. FAQs",
                "6. Logout"
            };
            CLIUtils.printSingleBlockHeading();
            CLIUtils.printBoxedMenu("Officer Menu", options);

            choice = CLIUtils.promptInt(sc, "Enter your choice: ", 1, 6);

            switch (choice) {
                case 1:
                    CLIUtils.printInfo("Viewing assigned complaints...");
                    ComplaintManager.viewComplaintsByOfficer(userId);
                    break;
                case 2:
                    CLIUtils.printInfo("Updating complaint status...");
                    ComplaintManager.updateComplaintStatus(userId);
                    break;
                case 3:
                    CLIUtils.printInfo("Filing new crime record...");
                    CrimeManager.fileCrime(userId);
                    break;
                case 4:
                    CLIUtils.printInfo("Viewing crime records...");
                    CrimeManager.viewCrimesByOfficer(userId);
                    break;
                case 5:
                    showFAQs();
                    break;
                case 6:
                    CLIUtils.printInfo("Logged out successfully.");
                    ActionTracker.log("Officer_" + userId, "Logged out");
                    break;
                default:
                    CLIUtils.printError("Invalid choice. Please select 1-6.");
            }
            if (choice != 6) CLIUtils.waitForEnter();
        } while (choice != 6);
    }

    private void showFAQs() {
        String[] faqs = {
            "Q: How do I update complaint status?",
            "A: Use the 'Update Complaint Status' option and follow prompts.",
            "Q: How do I file a crime record?",
            "A: Use the 'File Crime Record' option.",
            "Q: How do I view my profile?",
            "A: Use the 'View Profile' option.",
            "Q: How are complaints assigned to me?",
            "A: Complaints are assigned based on area and workload.",
            "Q: Can I reassign complaints?",
            "A: Contact admin for complaint reassignment."
        };
        CLIUtils.printBoxedInfo("Officer FAQs", faqs);
    }
}
