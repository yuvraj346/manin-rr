import java.util.Scanner;

public class CLIUtils {
    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";

    // Print the block ASCII art heading ONCE
    public static void printSingleBlockHeading() {
        try {
            String[] art = {
                CYAN + "██████╗  █████╗ ██████╗ ██╗██████╗     ██████╗ ███████╗███████╗ ██████╗ ██╗    ██╗   ██╗███████╗    " + RESET,
                CYAN + "██╔══██╗██╔══██╗██╔══██╗██║██╔══██╗    ██╔══██╗██╔════╝██╔════╝██╔═══██╗██║    ██║   ██║██╔════╝    " + RESET,
                CYAN + "██████╔╝███████║██████╔╝██║██║  ██║    ██████╔╝█████╗  ███████╗██║   ██║██║    ██║   ██║█████╗      " + RESET,
                CYAN + "██╔══██╗██╔══██║██╔═══╝ ██║██║  ██║    ██╔══██╗██╔══╝  ╚════██║██║   ██║██║    ╚██╗ ██╔╝██╔══╝      " + RESET,
                CYAN + "██║  ██║██║  ██║██║     ██║██████╔╝    ██║  ██║███████╗███████║╚██████╔╝███████╗╚████╔╝ ███████╗    " + RESET,
                CYAN + "╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝╚═════╝     ╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝ ╚══════╝ ╚═══╝  ╚══════╝    " + RESET,
                CYAN + "                                                                                                    " + RESET,
                CYAN + "                    " + BLUE + "R  A  P  I  D  R  E  S  O  L  V  E" + RESET + CYAN + "                    " + RESET
            };
            for (String line : art) {
                if (line != null) {
                    System.out.println(center(line, 110));
                }
            }
            System.out.println();
        } catch (Exception e) {
            // Fallback to simple heading if ASCII art fails
            try {
                System.out.println("RAPIDRESOLVE - Complaint & Crime Management System");
                System.out.println();
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Center a string in a given width
    public static String center(String s, int width) {
        try {
            if (s == null) {
                s = "";
            }
            if (s.length() >= width) return s;
            int left = (width - s.length()) / 2;
            int right = width - s.length() - left;
            return " ".repeat(left) + s + " ".repeat(right);
        } catch (Exception e) {
            return s != null ? s : "";
        }
    }

    // Clear the screen (works on most ANSI terminals)
    public static void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // If clear screen fails, just print some newlines
            try {
                for (int i = 0; i < 50; i++) {
                    System.out.println();
                }
            } catch (Exception ex) {
                // If even newlines fail, do nothing
            }
        }
    }

    // Show a loading animation (animated dots)
    public static void showLoadingAnimation(String message, int dotCount, int delayMs) {
        try {
            if (message == null) message = "Loading";
            if (dotCount < 0) dotCount = 3;
            if (delayMs < 0) delayMs = 100;
            
            System.out.print(CYAN + message);
            for (int i = 0; i < dotCount; i++) {
                try { 
                    Thread.sleep(delayMs); 
                } catch (Exception ignored) {}
                System.out.print(".");
                System.out.flush();
            }
            System.out.println(RESET);
        } catch (Exception e) {
            try {
                System.out.println("Loading...");
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Typewriter effect for text
    public static void typewriterPrint(String text, int delayMs) {
        try {
            if (text == null) text = "";
            if (delayMs < 0) delayMs = 50;
            
            for (char c : text.toCharArray()) {
                System.out.print(c);
                System.out.flush();
                try { 
                    Thread.sleep(delayMs); 
                } catch (Exception ignored) {}
            }
            System.out.println();
        } catch (Exception e) {
            try {
                System.out.println(text != null ? text : "");
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a random tip of the day
    public static void printTipOfTheDay() {
        try {
            String[] tips = {
                "Tip: Use strong passwords for your account!",
                "Tip: You can check complaint status anytime from your dashboard.",
                "Tip: Officers are assigned based on area workload.",
                "Tip: Use the feedback option to help us improve!",
                "Tip: You can export your complaint history as a report.",
                "Tip: For emergencies, use the helpline numbers shown in the menu.",
                "Tip: Keep your profile updated for better service.",
                "Tip: Use the FAQs section for quick help.",
                "Tip: Admins can generate system-wide reports from their dashboard.",
                "Tip: All actions are logged for transparency and security."
            };
            int idx = (int)(Math.random() * tips.length);
            System.out.println(MAGENTA + "\n💡 " + tips[idx] + RESET);
        } catch (Exception e) {
            try {
                System.out.println("\n💡 Tip: Keep your account secure!");
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Improved boxed menu with neat borders, more padding, and animation, plus loading and tip
    public static void printBoxedMenu(String title, String[] options) {
        try {
            showLoadingAnimation("Loading menu", 4, 180);
            if (title == null) title = "Menu";
            if (options == null || options.length == 0) {
                options = new String[]{"No options available"};
            }
            
        int width = title.length();
        for (String opt : options) {
                if (opt != null && opt.length() > width) width = opt.length();
        }
            width += 14; // more padding for a bigger menu
            if (width < 20) width = 20; // minimum width
            
            String border = CYAN + "╔" + "═".repeat(width) + "╗" + RESET;
            String sep = CYAN + "╠" + "═".repeat(width) + "╣" + RESET;
            System.out.println(border);
            System.out.println(CYAN + "║      " + YELLOW + title + " ".repeat(width - title.length() - 6) + CYAN + "║" + RESET);
            System.out.println(sep);
            for (String opt : options) {
                try { 
                    Thread.sleep(70); 
                } catch (Exception ignored) {}
                if (opt != null) {
                    System.out.println(CYAN + "║   " + BLUE + "➤  " + opt + " ".repeat(width - opt.length() - 6) + CYAN + "║" + RESET);
                }
            }
            System.out.println(border);
            printTipOfTheDay();
        } catch (Exception e) {
            try {
                System.out.println("=== " + (title != null ? title : "Menu") + " ===");
                if (options != null) {
                    for (String opt : options) {
                        if (opt != null) {
                            System.out.println("- " + opt);
                        }
                    }
                }
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a boxed info section (for About, Help, etc.)
    public static void printBoxedInfo(String title, String[] lines) {
        try {
            if (title == null) title = "Information";
            if (lines == null || lines.length == 0) {
                lines = new String[]{"No information available"};
            }
            
            int width = title.length();
            for (String l : lines) {
                if (l != null && l.length() > width) width = l.length();
            }
            width += 6;
            if (width < 20) width = 20; // minimum width
            
        String border = CYAN + "╔" + "═".repeat(width) + "╗" + RESET;
        String sep = CYAN + "╠" + "═".repeat(width) + "╣" + RESET;
        System.out.println(border);
        System.out.println(CYAN + "║  " + YELLOW + title + " ".repeat(width - title.length() - 2) + CYAN + "║" + RESET);
        System.out.println(sep);
            for (String l : lines) {
                if (l != null) {
                    System.out.println(CYAN + "║  " + WHITE + l + " ".repeat(width - l.length() - 2) + CYAN + "║" + RESET);
                }
        }
        System.out.println(border);
        } catch (Exception e) {
            try {
                System.out.println("=== " + (title != null ? title : "Information") + " ===");
                if (lines != null) {
                    for (String l : lines) {
                        if (l != null) {
                            System.out.println(l);
                        }
                    }
                }
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Pause and wait for user to press Enter
    public static void waitForEnter() {
        try {
            System.out.print(MAGENTA + "\nPress Enter to continue..." + RESET);
            System.in.read();
        } catch (Exception e) {
            try {
                System.out.println("\nPress Enter to continue...");
                System.in.read();
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a colored prompt
    public static void printPrompt(String prompt) {
        try {
            if (prompt == null) prompt = "Enter: ";
        System.out.print(YELLOW + prompt + RESET);
        } catch (Exception e) {
            try {
                System.out.print(prompt != null ? prompt : "Enter: ");
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a colored error
    public static void printError(String error) {
        try {
            if (error == null) error = "An error occurred";
        System.out.println(RED + error + RESET);
        } catch (Exception e) {
            try {
                System.out.println("ERROR: " + (error != null ? error : "An error occurred"));
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a colored info message
    public static void printInfo(String info) {
        try {
            if (info == null) info = "";
        System.out.println(GREEN + info + RESET);
        } catch (Exception e) {
            try {
                System.out.println(info != null ? info : "");
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a colored warning
    public static void printWarning(String warning) {
        try {
            if (warning == null) warning = "Warning";
        System.out.println(MAGENTA + warning + RESET);
        } catch (Exception e) {
            try {
                System.out.println("WARNING: " + (warning != null ? warning : "Warning"));
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Print a colored success message
    public static void printSuccess(String msg) {
        try {
            if (msg == null) msg = "Success";
        System.out.println(GREEN + msg + RESET);
        } catch (Exception e) {
            try {
                System.out.println("SUCCESS: " + (msg != null ? msg : "Success"));
            } catch (Exception ex) {
                // If even fallback fails, do nothing
            }
        }
    }

    // Robust input utility: get validated integer input with re-prompt
    public static int promptInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            try {
                printPrompt(prompt);
                String input = sc.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    printError("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            } catch (Exception e) {
                printError("Input error: " + e.getMessage());
            }
        }
    }

    // Robust input utility: get validated string input with re-prompt
    public static String promptString(Scanner sc, String prompt, boolean required, int minLen, int maxLen, String regex, String errorMsg) {
        while (true) {
            try {
                printPrompt(prompt);
                String input = sc.nextLine().trim();
                if (required && input.isEmpty()) {
                    printError(prompt + " cannot be empty. Please try again.");
                    continue;
                }
                if (minLen > 0 && input.length() < minLen) {
                    printError(prompt + " must be at least " + minLen + " characters long.");
                    continue;
                }
                if (maxLen > 0 && input.length() > maxLen) {
                    printError(prompt + " must be at most " + maxLen + " characters long.");
                    continue;
                }
                if (regex != null && !regex.isEmpty() && !input.matches(regex)) {
                    printError(errorMsg != null ? errorMsg : (prompt + " is invalid."));
                    continue;
                }
                return input;
            } catch (Exception e) {
                printError("Input error: " + e.getMessage());
            }
        }
    }
} 